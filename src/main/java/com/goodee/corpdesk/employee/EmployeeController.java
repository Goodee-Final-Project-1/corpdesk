package com.goodee.corpdesk.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.goodee.corpdesk.attendance.AttendanceRepository;
import com.goodee.corpdesk.department.DepartmentRepository;
import com.goodee.corpdesk.employee.Employee.CreateGroup;
import com.goodee.corpdesk.employee.Employee.UpdateGroup;
import com.goodee.corpdesk.employee.validation.UpdateEmail;
import com.goodee.corpdesk.employee.validation.UpdatePassword;
import com.goodee.corpdesk.position.PositionRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/employee/**")
@Slf4j
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private PositionRepository positionRepository;
 
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

		if (employeeRepository.existsByUsername(employee.getUsername())) {
			bindingResult.rejectValue("username", "error.employee", "이미 사용 중인 아이디입니다.");
			return "employee/add";
		}

		// 휴대전화 중복 체크
		if (employeeRepository.existsByMobilePhone(employee.getMobilePhone())) {
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

	@PostMapping("update/email")
	public String updateEmail(Authentication authentication, @Validated(UpdateEmail.class) Employee employee,
			BindingResult bindingResult) {
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

	@GetMapping("update")
	public void update(Authentication authentication, Model model) {
		Employee employee = employeeService.detail(authentication.getName());
		model.addAttribute("employee", employee);
	}

	@PostMapping("update/password")
	public String updatePassword(Authentication authentication, @Validated(UpdatePassword.class) Employee employee,
			BindingResult bindingResult) {
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
