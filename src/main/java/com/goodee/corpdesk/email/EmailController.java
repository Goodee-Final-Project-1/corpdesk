package com.goodee.corpdesk.email;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/email/**")
public class EmailController {

//	@Autowired
//	EmailService emailService;

	@GetMapping("received")
	public void received() {
	}

	@GetMapping("received/{no}")
	public String received(@PathVariable Integer no) {
		return "email/detail";
	}

	@GetMapping("sending")
	public void sending() {
	}

	@GetMapping("sent")
	public void sent() {
	}

	@GetMapping("sent/{no}")
	public String sent(@PathVariable Integer no) {
		return "email/detail";
	}
}
