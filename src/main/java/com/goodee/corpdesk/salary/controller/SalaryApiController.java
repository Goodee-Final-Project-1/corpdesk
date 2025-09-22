package com.goodee.corpdesk.salary.controller;

import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/salary")
@RequiredArgsConstructor
public class SalaryApiController {

	private final SalaryService salaryService;

	@PostMapping("/list/{page}")
	public Page<EmployeeSalaryDTO> list(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 10);

		return salaryService.getList(pageable);
	}
}
