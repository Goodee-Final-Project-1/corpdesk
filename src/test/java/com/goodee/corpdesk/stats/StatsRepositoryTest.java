package com.goodee.corpdesk.stats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StatsRepositoryTest {

	@Autowired
	private StatsRepository statsRepository;

//	@Test
//	void countAllByHireDateYearAndHireDateMonth() {
////		List<Integer> datas = statsRepository.countAllByHireDateYearAndHireDateMonth(LocalDate.parse("2020-10-01"), LocalDate.parse("2023-10-01"));
//		List<Date> months = statsRepository.findAllDateMonth(LocalDate.parse("2020-10-01"), LocalDate.parse("2023-10-01"));
//
//		System.out.println("============================");
//		System.out.println(months);
//		Assertions.assertNotEquals(0, months.size());
//	}

	@Test
	void countAllServicePeriod() {
		List<Long> list = statsRepository.countAllServicePeriod();

		System.out.println("============================");
		System.out.println(list);
		Assertions.assertNotEquals(0, list.size());
	}
}