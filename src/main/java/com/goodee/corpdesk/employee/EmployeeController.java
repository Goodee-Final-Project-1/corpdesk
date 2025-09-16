package com.goodee.corpdesk.employee;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.goodee.corpdesk.department.DepartmentRepository;
import com.goodee.corpdesk.employee.Employee.CreateGroup;
import com.goodee.corpdesk.employee.Employee.UpdateGroup;
import com.goodee.corpdesk.position.PositionRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());
        return "employee/add";
    }

    @PostMapping("/add")
    public String addEmployee(@Validated(CreateGroup.class) @ModelAttribute Employee employee,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findAll());
            model.addAttribute("positions", positionRepository.findAll());
            return "employee/add";
        }

        if(employeeRepository.existsByUsername(employee.getUsername())) {
            bindingResult.rejectValue("username", "error.employee", "이미 사용 중인 아이디입니다.");
            return "employee/add";
        }
        
        // 휴대전화 중복 체크
        if(employeeRepository.existsByMobilePhone(employee.getMobilePhone())) {
            bindingResult.rejectValue("mobilePhone", "error.employee", "이미 등록된 휴대전화입니다.");
            return "employee/add";
        }

        employeeService.addEmployee(employee);
        return "redirect:/employee/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("employees", employeeService.getActiveEmployees());
        return "employee/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Employee employee = employeeService.getEmployee(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());
        return "employee/edit";
    }

    @PostMapping("/edit")
    public String editEmployee(@Validated(UpdateGroup.class) @ModelAttribute Employee employee,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findAll());
            model.addAttribute("positions", positionRepository.findAll());
            return "employee/edit";
        }

        employeeService.updateEmployee(employee);
        return "redirect:/employee/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") String id) {
        employeeService.deactivateEmployee(id);
        return "redirect:/employee/list";
    }
}
