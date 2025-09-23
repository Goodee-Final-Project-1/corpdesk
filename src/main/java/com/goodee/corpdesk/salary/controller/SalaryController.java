package com.goodee.corpdesk.salary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/salary")
public class SalaryController {


	@RequestMapping("/list")
	public String list() {
		return "salary/list";
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable Integer page) {
		return "salary/list";
	}

	@RequestMapping("/detail/{paymentId}")
	public String detail(@PathVariable Long paymentId) {
		return "salary/detail";
	}
}
