package com.goodee.corpdesk.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email/**")
@Slf4j
public class EmailApiController {

	@Autowired
	EmailService emailService;

	/**
	 * @param authentication
	 * @param page 현재 페이지 번호
	 * @return 수신 메일 목록
	 */
	@PostMapping("received/{page}")
	public List<EmailDTO> received(Authentication authentication, @PathVariable Integer page) {
		String username = authentication.getName();
		Pageable pageable = PageRequest.of(page - 1, 10);

		return emailService.receivedBox(username, pageable);
	}

	/**
	 * @param authentication
	 * @param map JSON 객체, 메일 번호를 받아옴
	 * @return 해당 메일의 정보를 가지고 있는 객체
	 */
	@PostMapping("received/detail")
	public EmailDTO receivedDetail(Authentication authentication, @RequestBody Map<String, Integer> map) {
		String username = authentication.getName();
		return emailService.receivedDetail(username, map.get("emailNo") - 1);
	}

	/**
	 * @param authentication
	 * @param sendDTO 보낼 메일의 정보를 가지고 있는 객체
	 * @return 성공시 success 문자열을 반환
	 * @throws Exception 
	 * UnsupportedEncodingException 보내는 사람 이름 인코딩 실패
	 * MailException 메일 전송 실패
	 */
	@PostMapping("sending")
	public String send(Authentication authentication, SendDTO sendDTO) throws Exception {
		sendDTO.setUser(authentication.getName());
		emailService.sendSimpleMail(sendDTO);
		return "success";
	}

	@PostMapping("sent/{page}")
	public List<EmailDTO> sentList(Authentication authentication, @PathVariable Integer page) {
		String username = authentication.getName();
		Pageable pageable = PageRequest.of(page - 1, 10);

		return emailService.sentBox(username, pageable);
	}

	@PostMapping("sent/detail")
	public EmailDTO sentDetail(Authentication authentication, @RequestBody Map<String, Integer> map) {
		String username = authentication.getName();
		return emailService.sentDetail(username, map.get("emailNo") - 1);
	}
}
