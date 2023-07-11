package com.bezkoder.spring.jpa.h2.common;

import java.io.IOException;
import java.util.List;

import javax.persistence.Column;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bezkoder.spring.jpa.h2.model.GameEvent;

public class ExcelUtil {

	private String homeTeam;
	private String awayTeam;

	private List<GameEvent> allEventList;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public ExcelUtil(List<GameEvent> allEventList, String homeTeam, String awayTeam) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.allEventList = allEventList;
		workbook = new XSSFWorkbook();
	}

	private void writeHeader() {
		sheet = workbook.createSheet("All Events");
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);
		createCell(row, 0, "Team", style);
		createCell(row, 1, "Event", style);
		createCell(row, 2, "Minutes", style);
		createCell(row, 3, "Seconds", style);
		createCell(row, 4, "Score", style);
	}

	private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if(valueOfCell != null) {
			if (valueOfCell instanceof Integer) {
				cell.setCellValue((Integer) valueOfCell);
			} else if (valueOfCell instanceof Long) {
				cell.setCellValue((Long) valueOfCell);
			} else if (valueOfCell instanceof String) {
				cell.setCellValue((String) valueOfCell);
			} else {
				cell.setCellValue((Boolean) valueOfCell);
			}
		}
		cell.setCellStyle(style);
	}

	private void write() {
		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		for (GameEvent gameEvent : allEventList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, gameEvent.getTeam(), style);
			createCell(row, columnCount++, gameEvent.getEvent(), style);
			createCell(row, columnCount++, gameEvent.getMinutes(), style);
			createCell(row, columnCount++, gameEvent.getSeconds(), style);
			createCell(row, columnCount++, gameEvent.getScore(), style);
		}
	}

	public void generateExcelFile(HttpServletResponse response) throws IOException {
		writeHeader();
		write();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
}
