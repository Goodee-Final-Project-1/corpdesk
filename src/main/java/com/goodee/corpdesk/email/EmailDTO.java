package com.goodee.corpdesk.email;

import jakarta.mail.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailDTO {

	private Integer messageNumber;
	private String subject;
	private String content;
	private String from;
	private Date sentDate;
	private Date receivedDate;
	private String replyTo;

}
