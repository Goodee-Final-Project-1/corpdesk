package com.goodee.corpdesk.department;

import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {


	
	 @Bean
	    CommandLineRunner initData(DepartmentRepository departmentRepository,
                                   PositionRepository positionRepository) {
	        return args -> {
	            // Department 초기 데이터
	            if (departmentRepository.count() == 0) {
	                departmentRepository.save(new Department(null, "인피니티오토"));
	                departmentRepository.save(new Department(1, "개발팀"));
	                departmentRepository.save(new Department(1, "인사팀"));
	            }

	            // Position 초기 데이터
	            if (positionRepository.count() == 0) {
	                positionRepository.save(new Position(null, "부장"));
	                positionRepository.save(new Position(1, "차장"));
	                positionRepository.save(new Position(2, "과장"));
	                positionRepository.save(new Position(3, "대리"));
	                positionRepository.save(new Position(4, "사원"));
	            }
	        };
	    }
}