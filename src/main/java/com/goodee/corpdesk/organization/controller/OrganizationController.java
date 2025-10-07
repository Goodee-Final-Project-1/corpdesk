package com.goodee.corpdesk.organization.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goodee.corpdesk.department.dto.DepartmentDetailDTO;
import com.goodee.corpdesk.department.dto.MoveEmployeesDTO;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.department.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/organization") // 조직 관련 URL
@RequiredArgsConstructor
public class OrganizationController {

    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;

    // 조직도 페이지
    @GetMapping("/list")
    public String listPage() {
        // /WEB-INF/views/organization/list.jsp
        return "organization/list";
    }

    
    @GetMapping("/tree")
    @ResponseBody
    public List<Map<String, Object>> getOrganizationTree() {
        List<Department> departments = departmentRepository.findAll();

        List<Map<String, Object>> nodes = new ArrayList<>();
        for (Department dept : departments) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", dept.getDepartmentId());                 // 노드 고유 id
            node.put("parent", dept.getParentDepartmentId() == null ? "#" : dept.getParentDepartmentId()); // root면 "#"
            node.put("text", dept.getDepartmentName());             // 노드 이름
            nodes.add(node);
        }
        return nodes;
    }
    
    @PostMapping("/addByName")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addDepartmentByName(
            @RequestParam(value = "parentName", required = false) String parentName,
            @RequestParam("name") String name) {

        // 중복 체크
        if (departmentRepository.findByDepartmentName(name).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "이미 존재하는 부서명입니다."));
        }

        Department parent = null;
        if (parentName != null && !parentName.isBlank()) {
            parent = departmentRepository.findByDepartmentName(parentName).orElse(null);
        }

        Department dept = new Department();
        dept.setDepartmentName(name);
        dept.setParentDepartmentId(parent != null ? parent.getDepartmentId() : null);

        departmentRepository.save(dept);

        Map<String, Object> res = new HashMap<>();
        res.put("id", dept.getDepartmentId());
        res.put("parentId", dept.getParentDepartmentId());
        res.put("name", dept.getDepartmentName());

        return ResponseEntity.ok(res);
    }



    
    
    @PostMapping("/deleteCascade")
    @ResponseBody
    public ResponseEntity<Void> deleteCascade(@RequestParam("id") Integer id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }


    private void deleteWithChildren(Integer id) {
        List<Department> children = departmentRepository.findByParentDepartmentId(id);
        for(Department child : children) {
            deleteWithChildren(child.getDepartmentId());
        }
        departmentRepository.deleteById(id);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDetailDTO> getDepartmentDetail(@PathVariable("id") Integer id) {
        DepartmentDetailDTO dto = departmentService.getDepartmentDetail(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/moveEmployees")
    @ResponseBody
    public ResponseEntity<?> moveEmployees(@RequestBody MoveEmployeesDTO dto) {
        departmentService.moveEmployees(dto.getEmployeeUsernames(), dto.getNewDeptId());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/excludeEmployees")
    @ResponseBody
    public ResponseEntity<?> excludeEmployees(@RequestBody MoveEmployeesDTO dto) {
        departmentService.excludeEmployees(dto.getEmployeeUsernames());
        return ResponseEntity.ok().build();
    }

    
}
