package com.goodee.corpdesk.salary.service;

import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryService {

	private final SalaryRepository salaryRepository;

	public Page<EmployeeSalaryDTO> getList(Pageable pageable) {
		return salaryRepository.findAllEmployeeSalary(pageable);
	}
}
