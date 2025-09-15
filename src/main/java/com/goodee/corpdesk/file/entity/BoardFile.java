package com.goodee.corpdesk.file.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
@Entity @Table(name = "board_file")
public class BoardFile extends FileBase {
	@Column(nullable = false)
	private Long boardId;
}
