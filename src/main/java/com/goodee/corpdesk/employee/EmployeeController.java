package com.goodee.corpdesk.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.goodee.corpdesk.department.Department;
import com.goodee.corpdesk.department.DepartmentRepository;
import com.goodee.corpdesk.position.Position;
import com.goodee.corpdesk.position.PositionRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;


    // 등록 폼 페이지
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());
        return "employee/add"; // /WEB-INF/views/employee/add.jsp
    }

    // 등록 처리
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee) {
        // 부서와 직위 설정
        if (employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null) {
            Department department = departmentRepository.findById(employee.getDepartment().getDepartmentId()).orElse(null);
            employee.setDepartment(department);
        }
        if (employee.getPosition() != null && employee.getPosition().getPositionId() != null) {
            Position position = positionRepository.findById(employee.getPosition().getPositionId()).orElse(null);
            employee.setPosition(position);
        }
        
        employeeRepository.save(employee);
        return "redirect:/employee/list"; // 등록 후 목록으로 이동
    }

    // 목록 조회
    @GetMapping("/list")
    public String list(Model model) {
    	List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employee/list";
    }
    
    // 수정 폼 페이지
    @GetMapping("/edit")
    public String redirectEditRoot() {
        return "redirect:/employee/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            model.addAttribute("departments", departmentRepository.findAll());
            model.addAttribute("positions", positionRepository.findAll());
            return "employee/edit";
        }
        return "redirect:/employee/list";
    }
    
    // 수정 처리
    @PostMapping("/edit")
    public String editEmployee(@ModelAttribute Employee employee) {
        // 기존 엔티티 로드 후 편집 가능한 필드만 갱신 (username 등 누락 필드 보호)
        Employee persisted = employeeRepository.findById(employee.getEmployeeId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + employee.getEmployeeId()));

        // 기본 정보
        persisted.setName(employee.getName());
        persisted.setPassword(employee.getPassword());
        persisted.setExternalEmail(employee.getExternalEmail());
        persisted.setHireDate(employee.getHireDate());
        persisted.setGender(employee.getGender());
        persisted.setMobilePhone(employee.getMobilePhone());
        persisted.setEnabled(employee.getEnabled());
        persisted.setStatus(employee.getStatus());
        persisted.setLastWorkingDay(employee.getLastWorkingDay());

        // 부서와 직위 설정
        if (employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null) {
            Department department = departmentRepository.findById(employee.getDepartment().getDepartmentId()).orElse(null);
            persisted.setDepartment(department);
        }
        if (employee.getPosition() != null && employee.getPosition().getPositionId() != null) {
            Position position = positionRepository.findById(employee.getPosition().getPositionId()).orElse(null);
            persisted.setPosition(position);
        }

        // username 등 폼에 없는 값은 그대로 유지됨
        employeeRepository.save(persisted);
        return "redirect:/employee/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Integer id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));

    if (employee.getLastWorkingDay() == null) {
        // 안전장치: 서버단에서도 체크
        throw new IllegalStateException("퇴사일자가 없는 사원은 삭제할 수 없습니다.");
    }

    employee.setUseYn(false); // 물리적 삭제 대신 비활성화 처리
    employeeRepository.save(employee);
    return "redirect:/employee/list";
}
}
