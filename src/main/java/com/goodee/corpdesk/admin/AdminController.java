package com.goodee.corpdesk.admin;

import com.goodee.corpdesk.employee.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@GetMapping
	public String list(Model model) {
		List<Map<String, Object>> employeeList = adminService.employeeList();
		List<Role> roleList = adminService.roleList();

		model.addAttribute("employeeList", employeeList);
		model.addAttribute("roleList", roleList);

		return "admin/list";
	}

	@PostMapping
	@ResponseBody
	public String updateRole(@RequestBody Map<String, Object> payload) {
		adminService.updateRole((String) payload.get("username"), (Integer) payload.get("roleId"));
		return "success";
	}
}
