package com.goodee.corpdesk.employee;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // 등록 폼 페이지
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee/add"; // /WEB-INF/views/employee/add.jsp
    }

    // 등록 처리
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/employee/list"; // 등록 후 목록으로 이동
    }

    // 목록 조회
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employee/list"; // /WEB-INF/views/employee/list.jsp
    }
}
