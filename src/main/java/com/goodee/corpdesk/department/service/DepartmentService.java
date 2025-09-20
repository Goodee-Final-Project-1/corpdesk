package com.goodee.corpdesk.department.service;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.entity.ApprovalForm;
import com.goodee.corpdesk.approval.repository.ApprovalFormRepository;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class DepartmentService {

    @Autowired
	private DepartmentRepository departmentRepository;

    public List<ResApprovalDTO> getApprovalFormList() throws Exception {
        List<Department> result = departmentRepository.findAll();

        return result.stream().map(Department::toResApprovalDTO).toList();
    }

}
