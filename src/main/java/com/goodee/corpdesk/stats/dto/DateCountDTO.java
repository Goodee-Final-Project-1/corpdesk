package com.goodee.corpdesk.stats.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class DateCountDTO {

	private LocalDate date;
	private Long count;

	DateCountDTO(Date date, Long count) {
		this.date = date.toLocalDate();
		this.count = count;
	}
}
