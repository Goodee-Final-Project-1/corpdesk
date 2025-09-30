package com.goodee.corpdesk.department;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;

@Configuration
public class DataInitializer {


	
	 @Bean
	    CommandLineRunner initData(DepartmentRepository departmentRepository,
                                   PositionRepository positionRepository) {
	        return args -> {
	            // Department 초기 데이터
	            if (departmentRepository.count() == 0) {
//	            	Department root = departmentRepository.save(new Department(null, "인피니티오토"));
//	                departmentRepository.save(new Department(root.getDepartmentId(), "개발팀"));
//	                departmentRepository.save(new Department(root.getDepartmentId(), "인사팀"));
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