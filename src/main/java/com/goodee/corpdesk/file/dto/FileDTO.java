package com.goodee.corpdesk.file.dto;

import java.io.File;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDTO {

	private Long fileId;
	private String oriName;
	private String saveName;
	private String extension;
	
	private File file;
}
