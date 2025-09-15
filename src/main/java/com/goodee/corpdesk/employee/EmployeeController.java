package com.goodee.corpdesk.employee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/employee/**")
@Slf4j
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	@GetMapping
	public String link() {
        return "employee/link";
	}

    @GetMapping("sample_page")
    public void sample() {}
	
	@GetMapping("login")
	public void login() {
	}
	
	@GetMapping("join")
	public void join() {
	}
	
	@PostMapping("join")
	public String join(Employee employee) {
		employeeService.join(employee);
		return "employee/link";
	}
	
	@GetMapping("detail")
	@ResponseBody
	public Employee detail(Authentication authentication) {
        return employeeService.detail(authentication.getName());
	}

    @GetMapping("update")
    public void update() {
    }

    @PostMapping("update/password")
    public String updatePassword(Authentication authentication, Employee param) {
        param.setUsername(authentication.getName());
        employeeService.updatePassword(param);
        return "redirect:/employee/logout";
    }

    @PostMapping("update/email")
    public String updateEmail(Authentication authentication, Employee param) {
        param.setUsername(authentication.getName());
        employeeService.updateEmail(param);
        return "employee/link";
    }
}
