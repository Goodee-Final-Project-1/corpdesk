package com.goodee.corpdesk.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/email/**")
@Slf4j
public class EmailController {

	@Autowired
	EmailService emailService;

	@GetMapping("received")
	public void received(/* Authentication authentication, Model model, Pageable pageable */) {
//		String username = authentication.getName();
//		List<ReceivedDTO> messageList = emailService.mailBox(username, pageable);
//		model.addAttribute("messageList", messageList);
	}

//	@PostMapping("received")
//	@ResponseBody
//	public List<ReceivedDTO> received(Authentication authentication, Pageable pageable) {
//		log.info("received");
//		String username = authentication.getName();
//		List<ReceivedDTO> messageList = emailService.mailBox(username, pageable);
//		return messageList;
//	}

	@GetMapping("send")
	public void send() {
	}

	@PostMapping("send")
	public void send(Authentication authentication, SendDTO sendDTO) {
		sendDTO.setReplyTo(authentication.getName());
		emailService.sendSimpleMail(sendDTO);
	}
}
