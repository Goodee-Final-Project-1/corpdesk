package com.goodee.corpdesk.position;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "`position`")
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer positionId;
	private Integer positionUpperId;
	private String positionName;
	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private String modifiedBy;
	private Boolean useYn;
}
