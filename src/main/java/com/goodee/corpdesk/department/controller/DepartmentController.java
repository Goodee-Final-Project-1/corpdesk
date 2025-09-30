package com.goodee.corpdesk.department.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goodee.corpdesk.department.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;



}
