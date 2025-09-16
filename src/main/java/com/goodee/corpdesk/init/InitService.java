package com.goodee.corpdesk.init;


import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.goodee.corpdesk.department.Department;
import com.goodee.corpdesk.department.DepartmentRepository;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.position.Position;
import com.goodee.corpdesk.position.PositionRepository;

@Service
@RequiredArgsConstructor
public class InitService implements CommandLineRunner {

	private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== InitService 시작 ===");
        System.out.println("현재 시간: " + java.time.LocalDateTime.now());
        try {
            initData();
            System.out.println("=== InitService 완료 ===");
        } catch (Exception e) {
            System.out.println("=== InitService 오류 발생 ===");
            e.printStackTrace();
        }
    }
    
    @Transactional
    public void initData() {
        System.out.println("initData() 메서드 시작");
        
        // 기본 직위 보장
        ensureDefaultPositions();

        // 기존 데이터 확인
        long employeeCount = employeeRepository.count();
        System.out.println("현재 사원 수: " + employeeCount);
        
        if (employeeCount > 0) {
            System.out.println("이미 데이터가 존재하므로 초기화를 건너뜁니다.");
            return;
        }
        
        System.out.println("새 데이터 생성 시작...");
        
        Department dev = new Department();
        dev.setDepartmentName("인피니티오토");
        dev.setUseYn(true);
        Department savedDept = departmentRepository.save(dev);
        System.out.println("부서 저장 완료: " + savedDept.getDepartmentId());

        Position pos = new Position();
        pos.setPositionName("사원");
        
        Position savedPos = positionRepository.save(pos);
        System.out.println("직위 저장 완료: " + savedPos.getPositionId());
        
        Employee emp = new Employee();
        emp.setName("고두현");
        emp.setUsername("rhengus");
        emp.setMobilePhone("010-6666-6666");
        emp.setPassword("rhengus1234");
        emp.setHireDate(LocalDate.parse("2025-09-08"));
        emp.setStatus("휴가");
        emp.setDepartmentId(savedDept.getDepartmentId());
        emp.setPositionId(savedPos.getPositionId());
        emp.setUseYn(true);    // 활성화
        emp.setEnabled(true);  // 계정활성화
        Employee savedEmp = employeeRepository.save(emp);
        System.out.println("사원 저장 완료: " + savedEmp.getEmployeeId());
        
        System.out.println("초기 데이터 생성 완료:");
        System.out.println("- 부서: " + savedDept.getDepartmentName() + " (ID: " + savedDept.getDepartmentId() + ")");
        System.out.println("- 직위: " + savedPos.getPositionName() + " (ID: " + savedPos.getPositionId() + ")");
        System.out.println("- 사원: " + savedEmp.getName() + " (ID: " + savedEmp.getEmployeeId() + ")");
    }

    private void ensureDefaultPositions() {
        String[] names = {"사원", "대리", "과장", "이사", "대표이사"};
        for (String name : names) {
            if (!positionRepository.existsByPositionName(name)) {
                Position p = new Position();
                p.setPositionName(name);
                p.setUseYn(true);
                positionRepository.save(p);
                System.out.println("기본 직위 생성: " + name);
            }
        }
    }
	
	
	
}
