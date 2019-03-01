package com.techstartingpoint.samples.rentCar.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techstartingpoint.samples.rentCar.model.CarRental;
import com.techstartingpoint.samples.rentCar.model.Client;
import com.techstartingpoint.samples.rentCar.model.RentalStatus;
import com.techstartingpoint.samples.rentCar.model.RentalType;
import com.techstartingpoint.samples.rentCar.service.ClientService;
import com.techstartingpoint.samples.rentCar.service.CarRentalService;

@Service
public class CarRentalServiceImpl implements CarRentalService {
	
	private final static int MIN_PROMOTION_RENTALS=3; 
	private final static int MAX_PROMOTION_RENTALS=5;
	private final static Float PROMOTION_PERCENT=10f;
	
	
	@Autowired
	ClientService clientService;
	
	
	
	
	/**
	 * Calculate the price of an individual trip
	 * @param rental
	 * @return
	 */
	public Float calculateIndividualPrice(CarRental rental) {
		float result=0;
		switch (rental.getType()) {
			case DISTANCE: {
				result = rental.getDistance() *50;
				break;
			}
			case HOUR: {
				result = rental.getHours() *300;
				break;
			}
			case WEEK: {
				result = rental.getWeeks()*10000;
				if (rental.getDistance()>3000) {
					long additionalDistance = rental.getDistance()-3000;
					float plus =  100 * additionalDistance / 3;
					result += plus;
				}
				break;
			}
		}
		return result;
	}
	
	/**
	 * Client asks for a car rental
	 * @param client
	 * @return
	 * 
	 * No se puede realizar una solicitud de viaje si el cliente tiene un viaje no finalizado.
	 */
	private CarRental createCarRental(Client client) {
		boolean allFinished=true;
		CarRental result=null;
		for (int i=0;i<client.getRentals().size() && allFinished; i++ ) {
			allFinished=client.getRentals().get(i).getStatus().equals(RentalStatus.FINISHED);
		}
		if (allFinished) {
			result = new CarRental(client.getId());
			client.getRentals().add(result);
		}
		return result;
	}
	
	
	/**
	 * Create rental given the client data
	 * @param name
	 * @param document
	 * @return
	 */
	public CarRental createCarRental(String name,String document) {
		Client client=clientService.findClient(document);
		if (client==null) {
			client = clientService.createClient(name, document); 
		}
		CarRental result=createCarRental(client);
		return result;
	}
	
	

	/**
	 * Para pasarla a estado En Curso, se le debe asignar un tipo de Alquiler. 
	 * @param rental
	 * @return
	 */
	public boolean startTrip(CarRental rental) {
		if (rental.getType()!=null && rental.getStatus().equals(RentalStatus.PENDING)) {
			rental.setStatus(RentalStatus.STARTED);
			return true;
		} else {
			return false;
		}
	}


/*
 * 	Solo se pueden pasar solicitudes a finalizada si ya están en curso, se deberá registrar el tiempo insumido, 
		y según el tipo de alquiler elegido, 
		la cantidad de kilómetros o horas.
	
 */
	
	
	public boolean finishHourTrip(CarRental rental,int hours) {
		boolean result=false;
		if (rental.getStatus().equals(RentalStatus.STARTED)) {
			if (rental.getType().equals(RentalType.HOUR)) {
				rental.setStatus(RentalStatus.FINISHED);
				rental.setHours(hours);
				result=true;
			}
		}
		return result;
	}
	
	
	public boolean finishWeekTrip(CarRental rental,float weeks,long distance) {
		boolean result=false;
		if (rental.getStatus().equals(RentalStatus.STARTED)) {
			if (rental.getType().equals(RentalType.WEEK)) {
				rental.setStatus(RentalStatus.FINISHED);
				rental.setWeeks(weeks);
				rental.setDistance(distance);
				result=true;
			}
		}
		return result;
	}
	

	public boolean finishDistanceTrip(CarRental rental,long distance) {
		boolean result=false;
		if (rental.getStatus().equals(RentalStatus.STARTED)) {
			if (rental.getType().equals(RentalType.DISTANCE)) {
				rental.setStatus(RentalStatus.FINISHED);
				rental.setDistance(distance);
				result=true;
			}
		}
		return result;
	}
	

	private class PriceComparator implements Comparator<CarRental> {
	    @Override
	    public int compare(CarRental a, CarRental b) {
	        return calculateIndividualPrice(a).compareTo(calculateIndividualPrice(b));
	    }
	}	
	
	
	public Float applyPromotion(CarRental currentCarRental, List<CarRental> appliableRentals, int promotionId) {
		Collections.sort(appliableRentals,new PriceComparator() );
		int minIndex = appliableRentals.size()<MAX_PROMOTION_RENTALS ? 0 : appliableRentals.size()-MAX_PROMOTION_RENTALS;
		// calculate / mark rentals as promoted and apply discount
		Float total=0f;
		for (int i=minIndex;i<appliableRentals.size();i++) {
			total+=calculateIndividualPrice(appliableRentals.get(i));
			appliableRentals.get(i).setPromotionGroup(promotionId);
		}
		total+=calculateIndividualPrice(currentCarRental);
		Float discount = total*PROMOTION_PERCENT;
		currentCarRental.setPromotionGroup(promotionId);
		currentCarRental.setDiscount(discount);
		return discount;
	}
	
	
	/**
	 * Set the price including promotions 
	 * @param client
	 * 	the client
	 * @param applyPromotion
	 * 	true if client asked for promotion
	 * @param carRentalId
	 * 	the id of the rental the client is paying for
	 */
	public List<CarRental> setPromotion(Client client, boolean applyPromotion, long carRentalId) {
		if (applyPromotion) {
			List<CarRental> appliableRentals=new ArrayList<CarRental>();
			List<CarRental> allRentals=client.getRentals();
			CarRental currentCarRental=null;
			int promotionId=0;
			for (CarRental rental:allRentals) {
				promotionId=Math.max(promotionId, rental.getPromotionGroup());
				if (rental.getStatus().equals(RentalStatus.FINISHED) && rental.getPromotionGroup()==CarRental.NO_PROMOTION) {
					if (rental.getId()==carRentalId) {
						currentCarRental=rental;
					} else {
						appliableRentals.add(rental);
					}
				}
			}
			promotionId++;  // set new promotion id in case it could be applied
			// if could be applied promotion to current car rental 
			// promotions can be applied to 3 - 5 rentals
			if (currentCarRental!=null && appliableRentals.size()>=MIN_PROMOTION_RENTALS-1) {
				Float promotionApplied= applyPromotion(currentCarRental,appliableRentals,promotionId);
				// TODO: check if promotion id was applied.
			}
		}
		return client.getRentals();
		
	}

	
	/*
	Al finalizar el viaje el sistema informara al usuario el total a cobrar para el viaje realizado según la información provista por el usuario.
	Si el cliente uso una tarifa promocional se le descuenta de las que tiene disponible.
	Un cliente puede usar promoción como tipo de alquiler si tiene promociones disponibles.
	En el caso que un cliente haya seleccionado promoción el sistema utilizará la promoción disponible que mejor se ajuste al alquiler realizado y en caso de no encontrar alguna que se ajuste realizara el cobro como si fuera un alquiler por kilómetro.
*/

}
