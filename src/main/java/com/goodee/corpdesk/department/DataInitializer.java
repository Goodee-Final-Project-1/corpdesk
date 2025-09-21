package com.goodee.corpdesk.department;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.goodee.corpdesk.position.Position;
import com.goodee.corpdesk.position.PositionRepository;

@Configuration
public class DataInitializer {


	
	 @Bean
	    CommandLineRunner initData(DepartmentRepository departmentRepository,
	                               PositionRepository positionRepository) {
	        return args -> {
	            // Department 초기 데이터
	            if (departmentRepository.count() == 0) {
	                departmentRepository.save(new Department("인피니티오토"));
	                departmentRepository.save(new Department("개발팀"));
	                departmentRepository.save(new Department("인사팀"));
	            }

	            // Position 초기 데이터
	            if (positionRepository.count() == 0) {
	                positionRepository.save(new Position("사원"));
	                positionRepository.save(new Position("대리"));
	                positionRepository.save(new Position("과장"));
	                positionRepository.save(new Position("차장"));
	                positionRepository.save(new Position("부장"));
	            }
	        };
	    }
}