package com.goodee.corpdesk.file.dto;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {

	private Long fileId;
	private String oriName;
	private String saveName;
	private String extension;
	
	private File file;
	
	public FileDTO(String saveName, String extension) {
		this.saveName = saveName;
		this.extension = extension;
	}
}
