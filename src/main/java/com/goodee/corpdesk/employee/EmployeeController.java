package com.goodee.corpdesk.employee;

import com.goodee.corpdesk.employee.validation.UpdateEmail;
import com.goodee.corpdesk.employee.validation.UpdatePassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
    public void sample() {
    }

    @GetMapping("sign_in")
    public String signIn() {
        return "sample/sign_in";
    }

    @GetMapping("reset")
    public String reset() {
        return "sample/reset_password";
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
    public void detail(Authentication authentication, Model model) {
        Employee employee = employeeService.detail(authentication.getName());
        model.addAttribute("employee", employee);
    }

	@GetMapping("update/email")
	public String updateEmail(Authentication authentication, Model model) {
		Employee employee = employeeService.detail(authentication.getName());
		model.addAttribute("employee", employee);
		return "employee/update_email";
	}

    @PostMapping("update/email")
    public String updateEmail(Authentication authentication,
            @Validated(UpdateEmail.class) Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "employee/detail";
        }

        employee.setUsername(authentication.getName());
        Employee result = employeeService.updateEmail(employee);

        if (result == null) {
            return "employee/detail";
        }

        return "redirect:/employee/link";
    }

    @GetMapping("update/password")
    public String updatePassword(Authentication authentication, Model model) {
        Employee employee = employeeService.detail(authentication.getName());
        model.addAttribute("employee", employee);
		return "employee/update_password";
    }

    @PostMapping("update/password")
    public String updatePassword(Authentication authentication,
            @Validated(UpdatePassword.class) Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "employee/update";
        }

        employee.setUsername(authentication.getName());
        Employee result = employeeService.updatePassword(employee);

        if (result == null) {
            return "employee/update";
        }

        return "redirect:/logout";
    }
}
