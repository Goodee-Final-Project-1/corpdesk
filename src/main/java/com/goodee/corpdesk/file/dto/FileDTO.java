package com.goodee.corpdesk.file.dto;

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
	
	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private Integer modifiedBy;
	private Boolean useYn;
}
