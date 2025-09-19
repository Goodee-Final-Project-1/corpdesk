package com.goodee.corpdesk.common;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseEntity {
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@Column(nullable = false) @CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	private String modifiedBy;
	
	@Column(nullable = false)
	@ColumnDefault("true")
	private Boolean useYn;
}
