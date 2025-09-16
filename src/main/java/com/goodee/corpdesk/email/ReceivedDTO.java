package com.goodee.corpdesk.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReceivedDTO {

	private Integer messageNumber;
	private String subject;
	private String content;
	private String from;
	private String sentDate;
	private String receivedDate;
	private String replyTo;

}
