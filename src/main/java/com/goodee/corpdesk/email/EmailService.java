package com.goodee.corpdesk.email;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
@Slf4j
public class EmailService {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    public List<EmailDTO> mailBox(Pageable pageable) {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", "993");
        props.put("mail.imap.ssl.enable", "true");

        Session session = Session.getInstance(props);
        Store store = null;
        Folder inbox = null;

		List<EmailDTO> messageList = new ArrayList<>();
		int start = pageable.getPageNumber() * pageable.getPageSize();

        try {
            store = session.getStore("imap");
            store.connect("imap.gmail.com", username, password);

            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // 받아온 메일 목록
            Message[] messages = inbox.getMessages();

			for (int i = messages.length - 1; i >= messages.length - 5; i--) {
				Message message = messages[i];

				EmailDTO email = new EmailDTO();
				email.setFrom(Arrays.toString(message.getFrom()));
				email.setSentDate(message.getSentDate());
				email.setReceivedDate(message.getReceivedDate());
				email.setMessageNumber(message.getMessageNumber());
				email.setSubject(message.getSubject());
				email.setReplyTo(Arrays.toString(message.getReplyTo()));

				log.info("=================== {}", email);

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

}
