package com.bezkoder.spring.jpa.h2.model;

public class AverageData {
	private String event;
	private double averageAmount;
	private int homeTeamAmount;
	private int awayTeamAmount;
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public double getAverageAmount() {
		return averageAmount;
	}
	public void setAverageAmount(double averageAmount) {
		this.averageAmount = averageAmount;
	}
	public int getHomeTeamAmount() {
		return homeTeamAmount;
	}
	public void setHomeTeamAmount(int homeTeamAmount) {
		this.homeTeamAmount = homeTeamAmount;
	}
	public int getAwayTeamAmount() {
		return awayTeamAmount;
	}
	public void setAwayTeamAmount(int awayTeamAmount) {
		this.awayTeamAmount = awayTeamAmount;
	}
	
	
}
