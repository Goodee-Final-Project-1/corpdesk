package com.goodee.corpdesk.stats;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsApiController {

	@PostMapping
	public void list(){

	}
}
