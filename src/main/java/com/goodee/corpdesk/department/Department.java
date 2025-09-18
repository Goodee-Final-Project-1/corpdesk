package com.goodee.corpdesk.department;


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
@Table(name = "department")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer departmentId;
	private Integer departmentHigh;
	private String departmentName = "인피니티오토";
	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private String modifiedBy;
	private Boolean useYn;
	
	
	// departmentName만 받는 생성자 추가
    public Department(String departmentName) {
        this.departmentName = departmentName;
        this.useYn = true;  // 기본값 예시
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
	
}
