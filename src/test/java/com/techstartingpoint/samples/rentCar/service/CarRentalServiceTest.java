package com.techstartingpoint.samples.rentCar.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.techstartingpoint.samples.rentCar.dao.ClientDao;
import com.techstartingpoint.samples.rentCar.model.CarRental;
import com.techstartingpoint.samples.rentCar.model.Client;
import com.techstartingpoint.samples.rentCar.model.RentalStatus;
import com.techstartingpoint.samples.rentCar.model.RentalType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarRentalServiceTest {


	final String CLIENT_DOC="11111111";

	/*
    @TestConfiguration
    static class CarRentalServiceImplTestContextConfiguration {
  
        @Bean
        public CarRentalService carRentalService() {
            return new CarRentalServiceImpl();
        }

        @Bean
        public ClientService clientService() {
            return new ClientServiceImpl();
        }
    
    
    }
*/
	
	
	@Before
	public void setup() {
		dao.initRepository();
	}
    
	@Autowired
    CarRentalService service;
    
	@Autowired
	ClientService clientService;	
	
	@Autowired
	ClientDao dao;
	
	@Test
	public void testCreateCarRental() {
		CarRental carRental = service.createCarRental("pablo","11111111");
		assert(carRental.getStatus().equals(RentalStatus.PENDING));
	}
	

	@Test
	public void testStartTripNoType() {
		CarRental carRental = service.createCarRental("pablo","11111111");
		boolean success=service.startTrip(carRental);
		// false because type is not setted
		assert(!success);
	}

	@Test
	public void testStartTrip() {
		CarRental carRental = service.createCarRental("pablo","11111111");
		carRental.setType(RentalType.HOUR);
		service.startTrip(carRental);
		assert(carRental.getStatus().equals(RentalStatus.STARTED));
	}

	
	
	@Test
	public void testStartTripStartingTwice() {
		CarRental carRental = service.createCarRental("pablo","11111111");
		carRental.setType(RentalType.HOUR);
		service.startTrip(carRental);
		service.startTrip(carRental);
		boolean success=service.startTrip(carRental);
		// false because it was not pending - started twice
		assert(!success);
	}
	
	
	@Test
	public void testFinishHourTrip() {
		CarRental carRental = service.createCarRental("pablo","11111111");
		carRental.setType(RentalType.HOUR);
		service.startTrip(carRental);
		service.finishHourTrip(carRental, 40);
		assert(carRental.getStatus().equals(RentalStatus.FINISHED));
	}

	@Test
	public void testFinishWeekTrip() {
		CarRental carRental = service.createCarRental("pablo","11111111");
		carRental.setType(RentalType.WEEK);
		service.startTrip(carRental);
		service.finishWeekTrip(carRental, 20,40);
		assert(carRental.getStatus().equals(RentalStatus.FINISHED));
	}

	@Test
	public void testFinishDistanceTrip() {
		CarRental carRental = service.createCarRental("pablo","11111111");
		carRental.setType(RentalType.DISTANCE);
		service.startTrip(carRental);
		service.finishDistanceTrip(carRental, 40);
		assert(carRental.getStatus().equals(RentalStatus.FINISHED));
	}

	@Test
	public void testSetPromotionOnlyOne() {
		CarRental carRental1 = service.createCarRental("pablo",CLIENT_DOC);
		carRental1.setType(RentalType.DISTANCE);
		service.startTrip(carRental1);
		service.finishDistanceTrip(carRental1, 40);

		CarRental carRental2 = service.createCarRental("pablo",CLIENT_DOC);
		carRental2.setType(RentalType.DISTANCE);
		service.startTrip(carRental2);
		service.finishDistanceTrip(carRental2, 40);
		
		CarRental carRental3 = service.createCarRental("pablo",CLIENT_DOC);
		carRental3.setType(RentalType.DISTANCE);
		service.startTrip(carRental3);
		service.finishDistanceTrip(carRental3, 40);
		
		
		Client client=clientService.findClient(CLIENT_DOC);
		long carRentalId = carRental3.getId();
		List<CarRental> rentals=service.setPromotion(client,true, carRentalId);
		assert(rentals.get(0).getDiscount()==0);
	}

}
