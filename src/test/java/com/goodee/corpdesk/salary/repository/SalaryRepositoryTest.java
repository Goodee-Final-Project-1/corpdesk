package com.goodee.corpdesk.salary.repository;

import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class SalaryRepositoryTest {

	@Autowired
	private SalaryRepository salaryRepository;

	@Test
	void findAllEmployeeSalary() {
		Pageable pageable = PageRequest.of(1, 10);
		Page<EmployeeSalaryDTO> page = salaryRepository.findAllEmployeeSalary(pageable);

		System.out.println("===================== " + page.getContent());
	}

	@Test
	void findAll() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<SalaryPayment> list = salaryRepository.findAll(pageable);
		System.out.println("===================== " + list.getContent());
	}
}