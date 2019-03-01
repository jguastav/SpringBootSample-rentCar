package com.techstartingpoint.samples.rentCar.service;


import com.techstartingpoint.samples.rentCar.model.Client;

public interface ClientService {

	public Client findClient(String document);
	Client createClient(String name,String document);

}
