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

    private Character isHoliday; // 휴일 여부

    @Column(nullable = false)
    private String workStatus; /**
     * Get the check-in timestamp formatted as yyyy-MM-dd HH:mm:ss.
     *
     * @return {@code ""} if {@code checkInDateTime} is null, otherwise the check-in timestamp formatted as {@code "yyyy-MM-dd HH:mm:ss"}.
     */
    
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
