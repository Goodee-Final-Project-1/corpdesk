package com.goodee.corpdesk.employee;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.goodee.corpdesk.attendance.Attendance;
import com.goodee.corpdesk.attendance.AttendanceRepository;
import com.goodee.corpdesk.attendance.AttendanceService;
import com.goodee.corpdesk.department.DepartmentRepository;
import com.goodee.corpdesk.employee.Employee.CreateGroup;
import com.goodee.corpdesk.employee.Employee.UpdateGroup;
import com.goodee.corpdesk.employee.validation.UpdateEmail;
import com.goodee.corpdesk.employee.validation.UpdatePassword;
import com.goodee.corpdesk.position.PositionRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Controller
@RequestMapping("/employee/**")
@Slf4j
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private  AttendanceService attendanceService;
	
	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private PositionRepository positionRepository;
 
	// ⭐⭐ File 관련 의존성 주입은 이제 Service로 이동합니다.
	// @Autowired
    // private EmployeeFileRepository employeeFileRepository;
    // @Autowired
    // private FileManager fileManager;
    // @Value("${app.upload}")
    // private String uploadPath;
	
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
	
	@GetMapping("/edit/{username}")
	public String editEmployeePage(@PathVariable("username") String username, Model model) {
	    // 직원 정보 가져오기
	    Employee employee = employeeService.getEmployeeOrThrow(username);
	    model.addAttribute("employee", employee);

	    // 프로필 파일 조회 (옵션)
	    Optional<EmployeeFile> employeeFileOpt = employeeService.getEmployeeFileByUsername(username);
	    employeeFileOpt.ifPresent(file -> model.addAttribute("employeeFile", file));

	    // 출퇴근 내역 가져오기
	    List<Attendance> attendanceList = employeeService.getAttendanceByUsername(username);
	    model.addAttribute("attendanceList", attendanceList);

	    // 부서, 직급 목록
	    model.addAttribute("departments", departmentRepository.findAll());
	    model.addAttribute("positions", positionRepository.findAll());

	    return "employee/edit"; // JSP
	}
	
	
	

	
	
	@PostMapping("/edit")
	public String editEmployee(@Validated(UpdateGroup.class) @ModelAttribute Employee employeeFromForm,
	        BindingResult bindingResult, Model model,
	        @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile) {

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("departments", departmentRepository.findAll());
	        model.addAttribute("positions", positionRepository.findAll());
	        return "employee/edit";
	    }
	    
	    // ⭐⭐ 파일과 직원 정보를 Service로 넘겨서 한 번에 처리합니다.
	    employeeService.updateEmployee(employeeFromForm, profileImageFile);

	    return "redirect:/employee/list";
	}
	
	@PostMapping("/deleteProfileImage")
	@ResponseBody
	public String deleteProfileImage(@RequestParam("username") String username) {
	    try {
            // ⭐⭐ 삭제 로직을 Service로 위임합니다.
	        employeeService.deleteProfileImage(username);
	        return "success";
	    } catch (Exception e) {
	        log.error("직원 {}의 프로필 이미지 삭제 실패: {}", username, e);
	        return "fail";
	    }
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

	@GetMapping("update/email")
	public String updateEmail(Authentication authentication, Model model) {
		Employee employee = employeeService.detail(authentication.getName());
		model.addAttribute("employee", employee);
		return "employee/update_email";
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

    @GetMapping("update/password")
    public String updatePassword(Authentication authentication, Model model) {
        Employee employee = employeeService.detail(authentication.getName());
        model.addAttribute("employee", employee);
		return "employee/update_password";
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