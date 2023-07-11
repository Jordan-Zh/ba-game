package com.bezkoder.spring.jpa.h2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.jpa.h2.model.GameEvent;

@Transactional
public interface GameEventRepository extends JpaRepository<GameEvent, Long> {

	  List<GameEvent> findByTeam(String team);
	  
	  @Modifying
	  @Query("delete from GameEvent where team=:team")
	  int deleteByTeam(String team);
	  
}
