package com.techstartingpoint.samples.rentCar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techstartingpoint.samples.rentCar.dao.ClientDao;
import com.techstartingpoint.samples.rentCar.model.Client;
import com.techstartingpoint.samples.rentCar.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	ClientDao dao;

	
	public Client findClient(String document) {
		Client result=dao.find(document);
		return result;
	}
	
	public Client createClient(String name,String document) {
		Client result = new Client(name, document);
		dao.addClient(result);
		return result;
	}
	
}
