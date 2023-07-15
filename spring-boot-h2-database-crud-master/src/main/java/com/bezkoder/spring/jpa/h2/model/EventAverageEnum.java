package com.bezkoder.spring.jpa.h2.model;

public enum EventAverageEnum {
	ATTACK("Attack", 28.5, 1),
	SHOOT("Shoot", 21.5, 1),
	TURNOVER("Turnover", 23, 1),
	Score("Score", 13.5, 1),
	FOUL("Foul", 17, 1),
	Wide("Wide", 10, 1)
	;
	
	private String name;
	private double averageAmount;
	private int count;
	
	EventAverageEnum(String name, double averageAmount, int count){
		this.name = name;
		this.averageAmount = averageAmount;
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public double getAverageAmount() {
		return averageAmount;
	}

	public void setAverageAmount(double averageAmount) {
		this.averageAmount = averageAmount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
