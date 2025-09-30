package com.goodee.corpdesk.stats;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stats")
public class StatsController {

	@GetMapping
	public String list() {
		return "stats/list";
	}
}
