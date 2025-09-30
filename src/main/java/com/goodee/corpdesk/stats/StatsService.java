package com.goodee.corpdesk.stats;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

	private final EmployeeRepository employeeRepository;

	public Map<String, Employee> list() {

		return null;
	}
}
