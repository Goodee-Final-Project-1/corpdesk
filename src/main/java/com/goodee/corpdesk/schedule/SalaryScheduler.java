package com.goodee.corpdesk.schedule;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import com.goodee.corpdesk.salary.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SalaryScheduler {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private SalaryRepository salaryRepository;

	@Scheduled(cron = "${salary.cron.payday}")
	@Transactional
	public void pay() {
		List<Employee> employeeList = employeeRepository.findAll();

		for (Employee e : employeeList) {
			SalaryPayment salaryPayment = new SalaryPayment();
			salaryPayment.setUsername(e.getUsername());
			salaryPayment.setPaymentDate(LocalDateTime.now());
			salaryPayment.setBaseSalary(e.getCurrentBaseSalary());

			salaryRepository.save(salaryPayment);

			// FIXME: 수당, 공제
		}
	}
}
