package com.goodee.corpdesk.vacation.service;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.vacation.VacationManager;
import com.goodee.corpdesk.vacation.entity.Vacation;
import com.goodee.corpdesk.vacation.repository.VacationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class VacationService {

    @Autowired
    private VacationRepository vacationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private VacationManager vacationManager;

    // 특정 월, 일에 해당하는 입사자들 데이터를 가져와서 vacation의 총 연차, 잔여 연차 update
    public void updateVacationsByHireDate(LocalDate currDate) throws Exception {

        // 월, 일로 입사자들 데이터를 가져옴
        int year = currDate.getYear();
        int month = currDate.getMonthValue();

        List<Employee> employeeList = employeeRepository.findAllByHireDateYearMonth(year, month);

        // 모든 입사자들에 대해 현재 총발생연차를 계산하고, DB상 총발생연차를 계산된 총발생연차로 대체
        // (계산된 총발생연차 - DB상 총발생연차)만큼 잔여연차를 증가시킴
        for (Employee employee : employeeList) {
            int newTotalVacation = vacationManager.calTotalVacation(employee.getHireDate());

            Vacation vacation = vacationRepository.findByUsername(employee.getUsername());
            int oldTotalVacation = vacation.getTotalVacation();

            int increaseAmount = newTotalVacation - oldTotalVacation;

            if(increaseAmount > 0) {
                vacation.setTotalVacation(newTotalVacation);
                vacation.setRemainingVacation(vacation.getRemainingVacation() + increaseAmount);

                vacationRepository.save(vacation);
            }

        }

    }

}
