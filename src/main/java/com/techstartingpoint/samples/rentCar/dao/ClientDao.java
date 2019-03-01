package com.techstartingpoint.samples.rentCar.dao;


import com.techstartingpoint.samples.rentCar.model.Client;

public interface ClientDao {
	public void addClient(Client client);
	public Client find(String document);
	public void initRepository();
	
}
