package com.goodee.corpdesk.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email/**")
@Slf4j
public class EmailApiController {

	@Autowired
	EmailService emailService;

	@PostMapping("received")
	public List<EmailDTO> received(Authentication authentication, Pageable pageable) {
		String username = authentication.getName();
		return emailService.receivedBox(username, pageable);
	}

	@PostMapping("received/detail")
	public EmailDTO receivedDetail(Authentication authentication, @RequestBody Map<String, Integer> map) {
		String username = authentication.getName();
		return emailService.receivedDetail(username, map.get("emailNo") - 1);
	}

	@PostMapping("sending")
	public void send(Authentication authentication, SendDTO sendDTO) {
		// FIXME: from으로 바꾸기
		sendDTO.setReplyTo(authentication.getName());
		emailService.sendSimpleMail(sendDTO);
		// FIXME: 성공/실패시 리다이렉트 할 url을 보내줘야 됨
	}

	@PostMapping("sent")
	public List<EmailDTO> sentList(Authentication authentication, Pageable pageable) {
		String username = authentication.getName();
		return emailService.sentBox(username, pageable);
	}

	@PostMapping("sent/detail")
	public EmailDTO sentDetail(Authentication authentication, @RequestBody Map<String, Integer> map) {
		String username = authentication.getName();
		return emailService.sentDetail(username, map.get("emailNo") - 1);
	}
}
