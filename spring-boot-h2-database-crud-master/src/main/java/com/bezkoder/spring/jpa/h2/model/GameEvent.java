package com.bezkoder.spring.jpa.h2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GameEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "team")
  private String team;

  @Column(name = "event")
  private String event;

  @Column(name = "minutes")
  private Integer minutes;

  @Column(name = "seconds")
  private Integer seconds;

  @Column(name = "score")
  private Integer score;

  @Column(name = "updateTime")
  private Long updateTime;
  
  public GameEvent() {

  }

  public GameEvent(String team, String event, Integer minutes, Integer seconds) {
    this.team = team;
    this.event = event;
    this.minutes = minutes;
    this.seconds = seconds;
  }

public long getId() {
	return id;
}

public void setId(long id) {
	this.id = id;
}

public String getTeam() {
	return team;
}

public void setTeam(String team) {
	this.team = team;
}

public String getEvent() {
	return event;
}

public void setEvent(String event) {
	this.event = event;
}

public Integer getMinutes() {
	return minutes;
}

public void setMinutes(Integer minutes) {
	this.minutes = minutes;
}

public Integer getSeconds() {
	return seconds;
}

public void setSeconds(Integer seconds) {
	this.seconds = seconds;
}

public Long getUpdateTime() {
	return updateTime;
}

public void setUpdateTime(Long updateTime) {
	this.updateTime = updateTime;
}

public Integer getScore() {
	return score;
}

public void setScore(Integer score) {
	this.score = score;
}


}
