package com.bezkoder.spring.jpa.h2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.jpa.h2.model.GameTimer;

@Transactional
public interface GameTimerRepository extends JpaRepository<GameTimer, Long> {

	  @Query("from GameTimer where homeTeam=:homeTeam and awayTeam=:awayTeam")
	  List<GameTimer> getTimerByTeams(String homeTeam, String awayTeam);	  
}
