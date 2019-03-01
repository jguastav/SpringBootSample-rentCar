package com.techstartingpoint.samples.rentCar.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.techstartingpoint.samples.rentCar.dao.ClientDao;
import com.techstartingpoint.samples.rentCar.model.Client;

@Repository
public class ClientDaoImpl implements ClientDao {
	private static Map<String, Client> clientStore=new HashMap<String, Client>();
	
	public void addClient(Client client) {
		clientStore.put(client.getDocument(),client);
	}
	
	public Client find(String document) {
		Client result=clientStore.get(document);
		return result;
	} 

	public void initRepository() {
		clientStore=new HashMap<String, Client>();
	}

}
