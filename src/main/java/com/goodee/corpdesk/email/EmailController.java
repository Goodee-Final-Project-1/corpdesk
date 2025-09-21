package com.goodee.corpdesk.email;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
	public String received() {
		return "email/list";
	}

	@GetMapping("received/detail/{no}")
	public String received(@PathVariable Integer no) {
		return "email/detail";
	}

	@GetMapping("sending")
	public void sending() {
	}

	@GetMapping("sent")
	public String sent() {
		return "email/list";
	}

	@GetMapping("sent/detail/{no}")
	public String sent(@PathVariable Integer no) {
		return "email/detail";
	}
}
