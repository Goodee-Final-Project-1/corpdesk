package com.goodee.corpdesk.common;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@Column(nullable = false) @CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	private Integer modifiedBy;
	
	@Column(nullable = false, columnDefinition = "true")
	private Boolean useYn;
}
