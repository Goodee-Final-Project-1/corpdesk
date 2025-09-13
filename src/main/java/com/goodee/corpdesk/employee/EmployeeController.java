package com.goodee.corpdesk.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/employee/**")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	@GetMapping
	public String link() {
		return "employee/link";
	}
	
	@GetMapping("login")
	public void login() {
	}
	
	@GetMapping("join")
	public void join() {
	}
	
	@PostMapping("join")
	public String join(Employee employee) {
		employeeService.join(employee);
		return "index";
	}
	
	@GetMapping("detail")
	@ResponseBody
	public Employee detail(Authentication authentication) {
		return employeeService.detail(authentication.getName());
	}
}
