package com.goodee.corpdesk.attendance.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.goodee.corpdesk.common.BaseEntity;
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
public class Attendance extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @Column(nullable = false)
    private String username;
    
    private LocalDateTime checkInDateTime;  // 출근 일시

    private LocalDateTime checkOutDateTime; // 퇴근 일시

    private boolean isHoliday; // 휴일 여부

    @Column(nullable = false)
    private String workStatus; // 근무 상태

//	private LocalDateTime createdAt; // 생성 일시
//
//    private LocalDateTime updatedAt; // 수정 일시
//
//    private String modifiedBy; // 수정한 사람
//
//    private Boolean useYn = true; // 사용 여부
    
    
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
