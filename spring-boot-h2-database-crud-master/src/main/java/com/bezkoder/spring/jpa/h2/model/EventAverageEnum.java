package com.bezkoder.spring.jpa.h2.model;

public enum EventAverageEnum {
	ATTACK("Attack", 200, 1),
	SHOOT("Shoot", 30, 1),
	TURNOVER("Turnover", 90, 1),
	Score("Score", 60, 1),
	FOUL("Foul", 61, 1),
	Wide("Wide", 88, 1)
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
