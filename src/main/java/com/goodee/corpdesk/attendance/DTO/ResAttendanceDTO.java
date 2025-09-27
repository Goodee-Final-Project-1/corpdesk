package com.goodee.corpdesk.attendance.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ResAttendanceDTO {

    private Long attendanceId;
    private String workStatus;
    private LocalDateTime checkInDateTime;
    private LocalDateTime checkOutDateTime;

    private LocalDateTime oldestCheckInDateTime;

    private Long lateArrivalsCnt;
    private Long absentDaysCnt;
    private Long earlyLeavingsCnt;

    private BigDecimal totalWorkHours;
    private BigDecimal totalWorkDays;

    public ResAttendanceDTO(BigDecimal totalWorkHours, BigDecimal totalWorkDays) {
        this.totalWorkHours = totalWorkHours;
        this.totalWorkDays = totalWorkDays;
    }
}
