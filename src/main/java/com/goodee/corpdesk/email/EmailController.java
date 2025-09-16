package com.goodee.corpdesk.email;

import jakarta.mail.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/email/*")
@Slf4j
public class EmailController {

    @Autowired
    EmailService emailService;

	@GetMapping("received")
	public void received(Model model, Pageable pageable) {
		List<EmailDTO> messageList = emailService.mailBox(pageable);
		log.info("================== {}", messageList);
		model.addAttribute("messageList", messageList);
	}
}
