package com.goodee.corpdesk.file.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDTO {

	private Long fileId;
	private Long messageId;
	private Long approvalId;
	private Long boardId;
	private String oriName;
	private String saveName;
	private String extension;

}
