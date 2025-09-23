package com.goodee.corpdesk.salary.service;

import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.entity.Allowance;
import com.goodee.corpdesk.salary.entity.Deduction;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import com.goodee.corpdesk.salary.repository.AllowanceRepository;
import com.goodee.corpdesk.salary.repository.DeductionRepository;
import com.goodee.corpdesk.salary.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SalaryService {

	private final SalaryRepository salaryRepository;
	private final AllowanceRepository allowanceRepository;
	private final DeductionRepository deductionRepository;

	public Page<EmployeeSalaryDTO> getList(Pageable pageable) {
		return salaryRepository.findAllEmployeeSalary(pageable);
	}

	public Map<String, Object> getDetail(Long paymentId) {
		Map<String, Object> map = new HashMap<>();
		SalaryPayment salaryPayment = salaryRepository.findById(paymentId).get();
		List<Allowance> allowanceList = allowanceRepository.findAllByPaymentId(paymentId);
		List<Deduction> deductionList = deductionRepository.findAllByPaymentId(paymentId);

		map.put("salaryPayment", salaryPayment);
		map.put("allowanceList", allowanceList);
		map.put("deductionList", deductionList);

		log.info("============================ {}", map);

		return map;
	}
}
