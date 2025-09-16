package com.goodee.corpdesk.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/email/*")
@Slf4j
public class EmailController {

    @Autowired
    EmailService emailService;

	@GetMapping("received")
	public void received(Authentication authentication, Model model, Pageable pageable) {
		String username = authentication.getName();
		List<ReceivedDTO> messageList = emailService.mailBox(username, pageable);
		model.addAttribute("messageList", messageList);
	}

	@GetMapping("send")
	public void send() {
	}

	@PostMapping("send")
	public void send(Authentication authentication, SendDTO sendDTO) {
		sendDTO.setReplyTo(authentication.getName());
		emailService.sendSimpleMail(sendDTO);
	}
}
