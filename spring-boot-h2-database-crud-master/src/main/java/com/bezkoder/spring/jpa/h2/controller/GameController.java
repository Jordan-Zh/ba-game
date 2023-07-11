package com.bezkoder.spring.jpa.h2.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.jpa.h2.common.ExcelUtil;
import com.bezkoder.spring.jpa.h2.model.AverageData;
import com.bezkoder.spring.jpa.h2.model.ChartPoint;
import com.bezkoder.spring.jpa.h2.model.EventAverageEnum;
import com.bezkoder.spring.jpa.h2.model.GameEvent;
import com.bezkoder.spring.jpa.h2.model.GameTimer;
import com.bezkoder.spring.jpa.h2.repository.GameEventRepository;
import com.bezkoder.spring.jpa.h2.repository.GameTimerRepository;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api")
public class GameController {

	@Autowired
	GameEventRepository gameEventRepository;
	@Autowired
	GameTimerRepository gameTimerRepository;

	@RequestMapping("/createEvent")
	public ResponseEntity<GameEvent> createEvent(@RequestBody GameEvent gameEvent) {
		String event = gameEvent.getEvent();
		if ("Score1".equalsIgnoreCase(event)) {
			gameEvent.setEvent("Score");
			gameEvent.setScore(1);
		} else if ("Score3".equalsIgnoreCase(event)) {
			gameEvent.setEvent("Score");
			gameEvent.setScore(3);
		}

		gameEvent.setUpdateTime(System.currentTimeMillis());
		GameEvent result = gameEventRepository.save(gameEvent);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping("/findAllEvents")
	public ResponseEntity<List<GameEvent>> findAllEvents(@RequestParam(name = "homeTeam") String homeTeam,
			@RequestParam(name = "awayTeam") String awayTeam) {

		GameEvent homeQuery = new GameEvent();
		homeQuery.setTeam(homeTeam);
		List<GameEvent> homeEvents = gameEventRepository.findByTeam(homeTeam);

		GameEvent awyQuery = new GameEvent();
		homeQuery.setTeam(awayTeam);
		List<GameEvent> awayEvents = gameEventRepository.findByTeam(awayTeam);

		List<GameEvent> allEvents = new ArrayList<GameEvent>();
		allEvents.addAll(homeEvents);
		allEvents.addAll(awayEvents);

		allEvents.sort((GameEvent e1, GameEvent e2) -> e1.getUpdateTime().compareTo(e2.getUpdateTime()));

		return new ResponseEntity<>(allEvents, HttpStatus.OK);
	}

	@RequestMapping("/deleteAllData")
	public ResponseEntity<String> deleteAllData(@RequestParam(name = "homeTeam") String homeTeam,
			@RequestParam(name = "awayTeam") String awayTeam) {

		gameEventRepository.deleteByTeam(homeTeam);
		gameEventRepository.deleteByTeam(awayTeam);

		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@RequestMapping("/loadData")
	public ResponseEntity<Map<String, Object>> loadData(@RequestParam(name = "homeTeam") String homeTeam,
			@RequestParam(name = "awayTeam") String awayTeam) {

		GameEvent homeQuery = new GameEvent();
		homeQuery.setTeam(homeTeam);
		List<GameEvent> homeEvents = gameEventRepository.findByTeam(homeTeam);

		GameEvent awyQuery = new GameEvent();
		homeQuery.setTeam(awayTeam);
		List<GameEvent> awayEvents = gameEventRepository.findByTeam(awayTeam);

		List<GameEvent> allEvents = new ArrayList<GameEvent>();
		allEvents.addAll(homeEvents);
		allEvents.addAll(awayEvents);

		allEvents.sort((GameEvent e1, GameEvent e2) -> e1.getUpdateTime().compareTo(e2.getUpdateTime()));

		Map<String, Object> allData = processEvents(allEvents, homeTeam, awayTeam);

		return new ResponseEntity<>(allData, HttpStatus.OK);
	}

	@RequestMapping("/export")
	public ResponseEntity<String> export(HttpServletResponse response, @RequestParam(name = "homeTeam") String homeTeam,
			@RequestParam(name = "awayTeam") String awayTeam) {

		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=All-Events" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		GameEvent homeQuery = new GameEvent();
		homeQuery.setTeam(homeTeam);
		List<GameEvent> homeEvents = gameEventRepository.findByTeam(homeTeam);

		GameEvent awyQuery = new GameEvent();
		homeQuery.setTeam(awayTeam);
		List<GameEvent> awayEvents = gameEventRepository.findByTeam(awayTeam);

		List<GameEvent> allEvents = new ArrayList<GameEvent>();
		allEvents.addAll(homeEvents);
		allEvents.addAll(awayEvents);

		allEvents.sort((GameEvent e1, GameEvent e2) -> e1.getUpdateTime().compareTo(e2.getUpdateTime()));

		ExcelUtil excelUtil = new ExcelUtil(allEvents, homeTeam, awayTeam);
		
		try {
			excelUtil.generateExcelFile(response);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@RequestMapping("/gameStart")
	public ResponseEntity<GameTimer> gameStart(@RequestParam(name = "homeTeam") String homeTeam,
			@RequestParam(name = "awayTeam") String awayTeam) {
		GameTimer gameTimer = new GameTimer();
		gameTimer.setHomeTeam(homeTeam);
		gameTimer.setAwayTeam(awayTeam);
		gameTimer.setStartTime(System.currentTimeMillis());
		gameTimerRepository.save(gameTimer);

		return new ResponseEntity<>(gameTimer, HttpStatus.OK);
	}
	
	@RequestMapping("/loadGameTimer")
	public ResponseEntity<GameTimer> loadGameTimer(@RequestParam(name = "homeTeam") String homeTeam,
			@RequestParam(name = "awayTeam") String awayTeam) {
		GameTimer result = new GameTimer();
		List<GameTimer> gameTimerLilst = gameTimerRepository.getTimerByTeams(homeTeam, awayTeam);
		
		
		if(!CollectionUtils.isEmpty(gameTimerLilst)) {
			result = gameTimerLilst.get(gameTimerLilst.size() - 1);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	public static void main(String[] args) {
		long secs = 3800;
//		 str=String.format("Hi,%s %s %s", "小超","是个","大帅哥");
		String hour = String.format("0%d", secs/3600);
		System.out.println(hour);
	}
	
	private Map<String, Object> processEvents(List<GameEvent> allEvents, String homeTeam, String awayTeam) {
		Map<String, Object> resultlMap = new HashMap<>();

		// All event data
		resultlMap.put("allEvents", allEvents);

		// Average data
		List<AverageData> averageDataList = new ArrayList<>();
		for (EventAverageEnum eventAverageEnum : EventAverageEnum.values()) {
			AverageData averageData = new AverageData();
			averageData.setEvent(eventAverageEnum.getName());
			averageData.setAverageAmount(eventAverageEnum.getAverageAmount());
			averageData.setHomeTeamAmount(countTeamAmount(allEvents, homeTeam, eventAverageEnum.getName()));
			averageData.setAwayTeamAmount(countTeamAmount(allEvents, awayTeam, eventAverageEnum.getName()));
			averageDataList.add(averageData);
		}
		resultlMap.put("averageDataList", averageDataList);

		// Chart data
		resultlMap.put("chartPoints", generateChartPoints(allEvents, homeTeam, awayTeam));

		return resultlMap;
	}

	private int countTeamAmount(List<GameEvent> allEvents, String team, String event) {
		int result = 0;
		for (GameEvent gameEvent : allEvents) {
			if (gameEvent.getTeam().equals(team) && gameEvent.getEvent().equals(event)) {
				if (event.equals("Score")) {
					result = result + gameEvent.getScore();
				} else {
					result = result + 1;
				}
			}
		}
		return result;
	}

	private List<ChartPoint> generateChartPoints(List<GameEvent> allEvents, String homeTeam, String awayTeam) {
		List<ChartPoint> resultList = new ArrayList<>();

		int maxMinutes = 0;
		for (GameEvent gameEvent : allEvents) {
			if (gameEvent.getMinutes() > maxMinutes) {
				maxMinutes = gameEvent.getMinutes();
			}
		}
		maxMinutes = maxMinutes + 1;

		for (int i = 0; i <= maxMinutes; i++) {
			ChartPoint point = new ChartPoint();
			point.setTeam(homeTeam);
			point.setMinutes(i);
			point.setScore(countTeamScoreByMinutes(allEvents, homeTeam, i));
			resultList.add(point);

			ChartPoint awayPoint = new ChartPoint();
			awayPoint.setTeam(awayTeam);
			awayPoint.setMinutes(i);
			awayPoint.setScore(countTeamScoreByMinutes(allEvents, awayTeam, i));
			resultList.add(awayPoint);
		}

		return resultList;
	}

	private int countTeamScoreByMinutes(List<GameEvent> allEvents, String team, int minutes) {
		int count = 0;
		for (GameEvent gameEvent : allEvents) {
			if ("Score".equals(gameEvent.getEvent()) && gameEvent.getTeam().equals(team)) {
				if (gameEvent.getMinutes() <= minutes) {
					count = count + gameEvent.getScore();
				} else {
					return count;
				}
			}
		}
		return count;
	}

}
