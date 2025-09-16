package com.goodee.corpdesk.email;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private EmployeeRepository employeeRepository;

	// 메일 가져오기
	public List<ReceivedDTO> mailBox(String username, Pageable pageable) {
		Optional<Employee> optional = employeeRepository.findById(username);
		Employee employee = optional.get();
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
		Folder inbox = null;

		List<ReceivedDTO> messageList = new ArrayList<>();
		int start = pageable.getPageNumber() * pageable.getPageSize();

		try {
			store = session.getStore("imap");
			store.connect(host, myEmail.split("@")[0], password);

			inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			// 받아온 메일 목록
			Message[] messages = inbox.getMessages();

			for (int i = messages.length - 1; i >= messages.length - 5; i--) {
				Message message = messages[i];

				ReceivedDTO email = new ReceivedDTO();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				email.setFrom(Arrays.toString(message.getFrom()));
				email.setSentDate(format.format(message.getSentDate()));
				email.setReceivedDate(format.format(message.getReceivedDate()));
				email.setMessageNumber(message.getMessageNumber());
				email.setSubject(message.getSubject());
				email.setReplyTo(Arrays.toString(message.getReplyTo()));

				messageList.add(email);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inbox != null && inbox.isOpen()) {
					inbox.close(false);
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

	// 메일 보내기
	public void sendSimpleMail(SendDTO sendDTO) {
		Optional<Employee> optional = employeeRepository.findById(sendDTO.getReplyTo());
		Employee employee = optional.get();

		JavaMailSender mailSender = this.javaMailSender(employee);

		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom(sendDTO.getFrom());
		message.setFrom(employee.getExternalEmail());
		message.setTo(sendDTO.getTo());
		message.setSubject(sendDTO.getSubject());
		message.setText(sendDTO.getText());

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
