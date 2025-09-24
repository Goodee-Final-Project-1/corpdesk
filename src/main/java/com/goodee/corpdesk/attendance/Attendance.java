package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "attendance")
public class Attendance {
	
	// PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;
    
    // 직원 ID
    @Column(nullable = false)
    private String username;
    
    // 출근 일시
    private LocalDateTime checkInDateTime;
    
    // 퇴근 일시
    private LocalDateTime checkOutDateTime;
    
    // 휴일 여부
    private boolean isHoliday;
    
    // 근무 상태
    @Column(nullable = false, length = 255)
    private String workStatus;
        
    // 생성 일시
	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

    // 수정 일시
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // 수정한 사람
    @Column(nullable = false, length = 255)
    private String modifiedBy;
    
    // 사용 여부
    @Column(nullable = false)
    private Boolean useYn = true;
    
    
    public String getFormattedCheckInDateTime() {
        if (checkInDateTime == null) return "";
        return checkInDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getFormattedCheckOutDateTime() {
        if (checkOutDateTime == null) return "";
        return checkOutDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    public String getCheckInDateTimeForInput() {
        return checkInDateTime != null
            ? checkInDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            : "";
    }

    public String getCheckOutDateTimeForInput() {
        return checkOutDateTime != null
            ? checkOutDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            : "";
    }
}
