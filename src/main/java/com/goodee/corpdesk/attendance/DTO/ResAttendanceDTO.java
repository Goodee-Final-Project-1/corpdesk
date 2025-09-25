package com.goodee.corpdesk.attendance.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResAttendanceDTO {

    private Long attendanceId;
    private String status;
    private LocalDateTime checkInDateTime;
    private LocalDateTime checkOutDateTime;

    private LocalDateTime oldestCheckInDateTime;

    public ResAttendanceDTO(Timestamp oldestCheckInDateTime) {
        this.oldestCheckInDateTime = LocalDateTime.ofInstant(oldestCheckInDateTime.toInstant(), ZoneId.of("Asia/Seoul"));
    }
}
