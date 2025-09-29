package com.goodee.corpdesk.organization.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goodee.corpdesk.department.dto.DepartmentTreeDTO;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/organization") // 조직 관련 URL
@RequiredArgsConstructor
public class OrganizationController {

    private final DepartmentService departmentService;

    // 조직도 페이지
    @GetMapping("/list")
    public String listPage() {
        // /WEB-INF/views/organization/list.jsp
        return "organization/list";
    }

    // 조직도 트리용 데이터(JSON 반환)
    @ResponseBody
    @GetMapping("/tree")
    public List<DepartmentTreeDTO> getDepartmentTree() {
        return departmentService.getDepartmentTree();
    }

    // 부서 상세정보(JSON 반환)
    @ResponseBody
    @GetMapping("/{id}")
    public Department getDepartmentDetail(@PathVariable Integer id) {
        return departmentService.getDepartmentDetail(id);
    }
}
