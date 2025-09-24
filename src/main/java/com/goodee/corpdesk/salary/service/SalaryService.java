package com.goodee.corpdesk.salary.service;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.entity.Allowance;
import com.goodee.corpdesk.salary.entity.Deduction;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import com.goodee.corpdesk.salary.repository.AllowanceRepository;
import com.goodee.corpdesk.salary.repository.DeductionRepository;
import com.goodee.corpdesk.salary.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	private final EmployeeRepository employeeRepository;

	@Value("${deduction.pension.national}")
	private Double nationalPension;
	@Value("${deduction.insurance.health}")
	private Double healthInsurance;
	@Value("${deduction.insurance.nursing}")
	private Double nursingInsurance;
	@Value("${deduction.insurance.employment}")
	private Double employmentInsurance;



	public Page<EmployeeSalaryDTO> getList(Pageable pageable) {
		Page<EmployeeSalaryDTO> page = salaryRepository.findAllEmployeeSalary(pageable);

		log.info("======================= {}", page.getContent());

		return page;
	}

	public Map<String, Object> getDetail(Long paymentId) {
		Map<String, Object> map = new HashMap<>();
		SalaryPayment salaryPayment = salaryRepository.findById(paymentId).get();
		List<Allowance> allowanceList = allowanceRepository.findAllByPaymentId(paymentId);
		List<Deduction> deductionList = deductionRepository.findAllByPaymentId(paymentId);

		map.put("salaryPayment", salaryPayment);
		map.put("allowanceList", allowanceList);
		map.put("deductionList", deductionList);

		return map;
	}

	// 스케줄러에서 호출
	public void saveSalaries() {
		List<Employee> employeeList = employeeRepository.findAllByUseYnTrue();

		for (Employee e : employeeList) {
			SalaryPayment salaryPayment = new SalaryPayment();
			salaryPayment.setUsername(e.getUsername());
			salaryPayment.setPaymentDate(LocalDateTime.now());
			salaryPayment.setBaseSalary(e.getCurrentBaseSalary());

			SalaryPayment result = salaryRepository.save(salaryPayment);

			// 통상 임금
			Long avgSalary = result.getBaseSalary() / 209;
			// 총 임금 : 기본급 + 수당
			Long totalPayment = 0L;
			totalPayment += result.getBaseSalary();

			// 수당
			// TODO: attendance 정보 가져오기
			/* 해당 기간의 attendance 정보를 순회하면서 수당 정보 저장하기
			 * 연장 근로 수당
			 * 휴일 근로 수당
			 * 야간 근로 수당
			 */

			// FIXME: 참조 변수로 넘겨주면 바뀐 값이 유지 되는지 확인이 필요함
			List<Allowance> allowanceList = this.getAllowanceList(totalPayment);
			allowanceRepository.saveAll(allowanceList);


			// TODO: vacation 정보 가져오기
			/* 1년 전의 기록을 순회하면서 연차 수당 정보 저장하기
			 * 연차 수당
			 */

			// 공제
			/* 총 임금 기준으로 계산
			 * 국민연금 4.5%
			 * 건강보험 3.545%
			 * 장기요양보험 12.95%
			 * 고용보험 0.9%
			 */
			List<Deduction> deductionList = this.getDeductionList(result.getPaymentId(), totalPayment);
			deductionRepository.saveAll(deductionList);
		}

	}

	// 수당 목록
	private List<Allowance> getAllowanceList(Long totalPayment) {
		List<Allowance> allowanceList = new ArrayList<>();
		// TODO:

		return allowanceList;
	}

	// 공제 목록
	private List<Deduction> getDeductionList(Long paymentId, Long totalPayment) {
		List<Deduction> deductionList = new ArrayList<>();
		String[] categories = {"국민연금", "건강보험", "장기요양보험", "고용보험"};

		for (String c : categories) {
			Deduction deduction = new Deduction();
			deduction.setPaymentId(paymentId);
			deduction.setDeductionName(c);

			Double percent = switch (c) {
				case "국민연금":
					yield nationalPension;
				case "건강보험":
					yield healthInsurance;
				case "장기요양보험":
					yield nursingInsurance;
				case "고용보험":
					yield employmentInsurance;
				default:
					throw new IllegalStateException("Unexpected value: " + c);
			};
			Long amount = (long) Math.ceil((double) totalPayment * percent / 100);
			deduction.setDeductionAmount(amount);

			deductionList.add(deduction);
		}

		return deductionList;
	}
}
