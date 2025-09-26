package com.goodee.corpdesk.attendance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AttendanceRepositoryTest {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Test
	void findAllByUsernameAndMonth() {
		List<Attendance> list = attendanceRepository.findAllByUsernameAndMonth("user01");
		System.out.println(list);

		Assertions.assertNotEquals(list.size(), 0);
	}
}