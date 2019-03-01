package com.techstartingpoint.samples.rentCar.model;

import java.util.ArrayList;
import java.util.List;

public class Client {
	long id;
	String name;
	String document;
	List<CarRental> rentals;
	
	
	public Client(String name, String document) {
		this.document=document;
		this.name=name;
		this.rentals=new ArrayList<CarRental>();
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<CarRental> getRentals() {
		return rentals;
	}
	public void setRentals(List<CarRental> rentals) {
		this.rentals = rentals;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	
	
	
	

}
