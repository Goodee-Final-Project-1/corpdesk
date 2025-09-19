package com.goodee.corpdesk.email;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import jakarta.mail.*;
import jakarta.mail.internet.MimeUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private AesBytesEncryptor aesBytesEncryptor;

	// 메일 상세
	public EmailDTO receivedDetail(String username, Integer emailNo) {
		Employee employee = getEmployee(username);
		return mailDetail(employee, emailNo, "INBOX");
	}

	public EmailDTO sentDetail(String username, Integer emailNo) {
		Employee employee = getEmployee(username);

		EmailDTO emailDTO = null;

		if (employee.getExternalEmail().contains("gmail")) {
			emailDTO = this.mailDetail(employee, emailNo, "[Gmail]/Sent Mail");
		} else {
			emailDTO = this.mailDetail(employee, emailNo, "Sent Messages");
		}

		return emailDTO;
	}

	public EmailDTO mailDetail(Employee employee, Integer emailNo, String folderName) {
		String myEmail = employee.getExternalEmail();
		String host = "imap." + myEmail.split("@")[1];
		String password = employee.getExternalEmailPassword();

		Properties props = new Properties();
		props.put("mail.store.protocol", "imap");
		props.put("mail.imap.host", host);
		props.put("mail.imap.port", "993");
		props.put("mail.imap.ssl.enable", "true");

		Session session = Session.getInstance(props);
		Store store = null;
		Folder folder = null;

		EmailDTO emailDTO = new EmailDTO();
		try {
			store = session.getStore("imap");
			store.connect(host, myEmail.split("@")[0], password);

			folder = store.getFolder(folderName);
			folder.open(Folder.READ_ONLY);

			// 받아온 메일 목록
			Message[] messages = folder.getMessages();

			Message message = messages[emailNo];

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String from = Arrays.toString(message.getFrom());
			if (from.toUpperCase().contains("=?UTF-8?B?")) {
				from = '[' + MimeUtility.decodeText(from.substring(from.indexOf("=?"), from.lastIndexOf("?=") + 2))
						+ from.substring(from.lastIndexOf("?=") + 2);
			}

			emailDTO.setFrom(from);
			emailDTO.setSubject(message.getSubject());
			emailDTO.setEmailNo(message.getMessageNumber());
			emailDTO.setReceivedDate(format.format(message.getReceivedDate()));
			emailDTO.setSentDate(format.format(message.getSentDate()));
			emailDTO.setRecipients(Arrays.toString(message.getAllRecipients()));

			Object content = message.getContent();

			if (content instanceof String) {
				emailDTO.setText(content.toString());
			} else if (content instanceof Multipart) {
				emailDTO.setText(getTextFromMultipart((Multipart) content));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (folder != null && folder.isOpen()) {
					folder.close(false);
				}
				if (store != null && store.isConnected()) {
					store.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return emailDTO;
	}

	// 메일 가져오기
	private List<EmailDTO> mailBox(Employee employee, Pageable pageable, String folderName) {
		String myEmail = employee.getExternalEmail();
		String host = "imap." + myEmail.split("@")[1];
		String password = employee.getExternalEmailPassword();

		Properties props = new Properties();
		props.put("mail.store.protocol", "imap");
		props.put("mail.imap.host", host);
		props.put("mail.imap.port", "993");
		props.put("mail.imap.ssl.enable", "true");

		Session session = Session.getInstance(props);
		Store store = null;
		Folder folder = null;

		List<EmailDTO> messageList = new ArrayList<>();
		// FIXME:
//		int start = pageable.getPageNumber() * pageable.getPageSize();

		try {
			store = session.getStore("imap");
			store.connect(host, myEmail.split("@")[0], password);

			folder = store.getFolder(folderName);
			folder.open(Folder.READ_ONLY);

			int total = folder.getMessageCount();
			int start = total;

			// 받아온 메일 목록
			Message[] messages = folder.getMessages(start - 5, start);

			for (Message message : messages) {
//			for (int i = messages.length - 1; i >= 0; i--) {
//				Message message = messages[i];

//				log.info("From: {}", message.getFrom());
//				log.info("Sub: {}", message.getSubject());
//				log.info("No: {}", message.getMessageNumber());
//				log.info("Received: {}", message.getReceivedDate());
//				log.info("Recipients: {}", message.getRecipients(Message.RecipientType.TO));
//				log.info("Sent: {}", message.getSentDate());
//				log.info("Content: {}", message.getContent());

				EmailDTO emailDTO = new EmailDTO();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				String from = Arrays.toString(message.getFrom());
				if (from.toUpperCase().contains("=?UTF-8?B?")) {
					from = '[' + MimeUtility.decodeText(from.substring(from.indexOf("=?"), from.lastIndexOf("?=") + 2))
							+ from.substring(from.lastIndexOf("?=") + 2);
				}
				emailDTO.setFrom(from);
				emailDTO.setSubject(message.getSubject());
				emailDTO.setEmailNo(message.getMessageNumber());
				emailDTO.setReceivedDate(format.format(message.getReceivedDate()));
				emailDTO.setSentDate(format.format(message.getSentDate()));
				emailDTO.setRecipients(Arrays.toString(message.getAllRecipients()));
//				emailDTO.setRecipients(Arrays.toString(message.getRecipients(Message.RecipientType.TO)));

				Object content = message.getContent();

				if (content instanceof String) {
					emailDTO.setText(content.toString());
				} else if (content instanceof Multipart) {
					emailDTO.setText(this.getTextFromMultipart((Multipart) content));
				}

				messageList.add(emailDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (folder != null && folder.isOpen()) {
					folder.close(false);
				}
				if (store != null && store.isConnected()) {
					store.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return messageList;
	}

	// FIXME: content에서 text 정보만 가져오기
	private String getTextFromMultipart(Multipart multipart) throws Exception {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < multipart.getCount(); i++) {
			BodyPart bodyPart = multipart.getBodyPart(i);

			if (bodyPart.isMimeType("text/plain")) {
				sb.append(bodyPart.getContent());
			}
//			else if (bodyPart.isMimeType("text/html")) {
//				sb.append(bodyPart.getContent());
//			}
		}

		return sb.toString();
	}

	// 유저 정보를 가져오면서, 이메일 비밀번호를 디코딩
	private Employee getEmployee(String username) {
		Optional<Employee> optional = employeeRepository.findById(username);
		Employee employee = optional.get();

		if (employee.getEncodedEmailPassword() == null) {
			throw new RuntimeException("비밀번호가 입력되지 않았습니다.");
		}

		byte[] decoded = aesBytesEncryptor.decrypt(employee.getEncodedEmailPassword());
		employee.setExternalEmailPassword(new String(decoded));

		return employee;
	}

	// 받은 메일함
	public List<EmailDTO> receivedBox(String username, Pageable pageable) {
		Employee employee = getEmployee(username);

		return this.mailBox(employee, pageable, "INBOX");
	}

	// 보낸 메일함
	public List<EmailDTO> sentBox(String username, Pageable pageable) {
		Employee employee = getEmployee(username);

		List<EmailDTO> messageList = null;

		if (employee.getExternalEmail().contains("gmail")) {
			messageList = this.mailBox(employee, pageable, "[Gmail]/Sent Mail");
		} else {
			messageList = this.mailBox(employee, pageable, "Sent Messages");
		}

		return messageList;
	}

	// 메일 보내기
	public void sendSimpleMail(SendDTO sendDTO) {
		Optional<Employee> optional = employeeRepository.findById(sendDTO.getReplyTo());
		Employee employee = optional.get();

		if (employee.getEncodedEmailPassword() != null) {
			byte[] decoded = aesBytesEncryptor.decrypt(employee.getEncodedEmailPassword());
			employee.setExternalEmailPassword(new String(decoded));
		}

		JavaMailSender mailSender = this.javaMailSender(employee);

		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom(sendDTO.getFrom());
		message.setFrom(employee.getExternalEmail());
		message.setTo(sendDTO.getTo());
		message.setSubject(sendDTO.getSubject());
		String text = sendDTO.getText().replaceAll("<br\\s*/?>", " ")
				.replaceAll("</p>", "\n").replaceAll("<[^>]*>", "");
		message.setText(text);

		mailSender.send(message);
	}

	// 메일 센더 구현
	public JavaMailSender javaMailSender(Employee employee) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		String email = employee.getExternalEmail();
		String host = "smtp." + email.split("@")[1];
		String password = employee.getExternalEmailPassword();

		mailSender.setHost(host);
//		mailSender.setUsername(email.split("@")[0]);
		mailSender.setUsername(email);
		mailSender.setPassword(password);

		mailSender.setPort(465);

		mailSender.setJavaMailProperties(getMailProperties());

		return mailSender;
	}

	private Properties getMailProperties() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
//		props.setProperty("mail.smtp.host", host);
//		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.ssl.enable", "true");

		return props;
	}
}
