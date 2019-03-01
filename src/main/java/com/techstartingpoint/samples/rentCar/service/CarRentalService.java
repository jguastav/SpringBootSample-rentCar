package com.techstartingpoint.samples.rentCar.service;

import java.util.List;


import com.techstartingpoint.samples.rentCar.model.CarRental;
import com.techstartingpoint.samples.rentCar.model.Client;

public interface CarRentalService {

	public Float calculateIndividualPrice(CarRental rental);
	
	public CarRental createCarRental(String name,String document);
	public boolean startTrip(CarRental rental);
	public boolean finishHourTrip(CarRental rental,int hours);
	public boolean finishWeekTrip(CarRental rental,float weeks,long distance);
	public boolean finishDistanceTrip(CarRental rental,long distance);
	public Float applyPromotion(CarRental currentCarRental, List<CarRental> appliableRentals, int promotionId);
	public List<CarRental> setPromotion(Client client, boolean applyPromotion, long carRentalId);

}
