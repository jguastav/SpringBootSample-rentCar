package com.techstartingpoint.samples.rentCar.model;

import java.util.Date;

public class CarRental {
	
	
	
	
	static long lastId=0;
	
	public static int NO_PROMOTION=-1;
	
	RentalStatus status;
	RentalType type;
	
	/**
	 * id identifying the promotion to group the rentals participating on it
	 */
	int promotionGroup;
	
	private synchronized long getNextId() {
		lastId++;
		return lastId;
	}
	
	
	
	long id;
	
	/**
	 * in km
	 */
	long distance;
	int hours;
	float weeks;
	
	long clientId;
	Date date;
	/**
	 * Total Discount (in amount of money) applied on a promotion
	 * This discount is the total corresponding to all rentals with the same promotionGroup
	 */
	float discount;
	
	public CarRental(long clientId) {
		this.status=RentalStatus.PENDING;
		this.clientId=clientId;
		this.id = getNextId();
		this.promotionGroup = NO_PROMOTION;
	}
	
	
	
	public RentalStatus getStatus() {
		return status;
	}
	public void setStatus(RentalStatus status) {
		this.status = status;
	}
	public RentalType getType() {
		return type;
	}
	public void setType(RentalType type) {
		this.type = type;
	}
	public long getDistance() {
		return distance;
	}
	public void setDistance(long distance) {
		this.distance = distance;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public float getWeeks() {
		return weeks;
	}
	public void setWeeks(float weeks) {
		this.weeks = weeks;
	}
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getId() {
		return id;
	}
	public void setId(long ic) {
		this.id = id;
	}


	public int getPromotionGroup() {
		return promotionGroup;
	}


	public void setPromotionGroup(int promotionGroup) {
		this.promotionGroup = promotionGroup;
	}



	public float getDiscount() {
		return discount;
	}



	public void setDiscount(float discount) {
		this.discount = discount;
	}

	
	
	
}
