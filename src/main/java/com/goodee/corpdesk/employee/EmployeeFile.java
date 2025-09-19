package com.goodee.corpdesk.employee;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employee_file")
public class EmployeeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_Id")
    private Long fileId;
    
    // ⭐ 직접 username 필드를 가집니다.
    private String username;

    private String oriName;
    private String saveName;
    private String extension;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String modifiedBy;
    private Boolean useYn;
    // ... 다른 필드들
}
