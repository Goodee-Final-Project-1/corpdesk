package com.goodee.corpdesk.home;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {

	@GetMapping("/")
	public String home() {
		return "index";
	}
}
