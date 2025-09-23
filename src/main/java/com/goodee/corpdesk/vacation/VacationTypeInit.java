package com.goodee.corpdesk.vacation;

import com.goodee.corpdesk.employee.Role;
import com.goodee.corpdesk.employee.RoleRepository;
import com.goodee.corpdesk.vacation.entity.VacationType;
import com.goodee.corpdesk.vacation.repository.VacationTypeRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VacationTypeInit implements InitializingBean {

	@Autowired
	private VacationTypeRepository vacationTypeRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	private void init() {
        VacationType type1 = new VacationType();
        type1.setVacationTypeName("연차휴가");
        vacationTypeRepository.save(type1);

        VacationType type2 = new VacationType();
        type2.setVacationTypeName("병가");
        vacationTypeRepository.save(type2);

        VacationType type3 = new VacationType();
        type3.setVacationTypeName("경조사휴가");
        vacationTypeRepository.save(type3);

        VacationType type4 = new VacationType();
        type4.setVacationTypeName("출산휴가");
        vacationTypeRepository.save(type4);

        VacationType type5 = new VacationType();
        type5.setVacationTypeName("육아휴직");
        vacationTypeRepository.save(type5);

        VacationType type6 = new VacationType();
        type6.setVacationTypeName("공가");
        vacationTypeRepository.save(type6);
	}
}
