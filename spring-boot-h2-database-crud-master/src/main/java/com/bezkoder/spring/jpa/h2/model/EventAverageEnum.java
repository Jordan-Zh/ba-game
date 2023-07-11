package com.bezkoder.spring.jpa.h2.model;

public enum EventAverageEnum {
	PASS("Pass", 200, 1),
	SHOOT("Shoot", 30, 1),
	ASSIST("Assist", 90, 1),
	Score("Score", 60, 1)
	;
	
	private String name;
	private int averageAmount;
	private int count;
	
	EventAverageEnum(String name, int averageAmount, int count){
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

	public int getAverageAmount() {
		return averageAmount;
	}

	public void setAverageAmount(int averageAmount) {
		this.averageAmount = averageAmount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
