package com.goodee.corpdesk.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emailApi/**")
@Slf4j
public class EmailApiController {

	@Autowired
	EmailService emailService;

	@PostMapping("received")
	public List<ReceivedDTO> received(Authentication authentication, Pageable pageable) {
		log.info("received");
		String username = authentication.getName();
		List<ReceivedDTO> messageList = emailService.mailBox(username, pageable);
		return messageList;
	}

//	@PostMapping("send")
//	public void send(Authentication authentication, SendDTO sendDTO) {
//		sendDTO.setReplyTo(authentication.getName());
//		emailService.sendSimpleMail(sendDTO);
//	}
}
