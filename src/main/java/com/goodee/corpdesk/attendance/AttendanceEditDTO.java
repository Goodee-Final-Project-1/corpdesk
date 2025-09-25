package com.goodee.corpdesk.attendance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceEditDTO {
    private Long attendanceId;
    private String workStatus;
    private String dateTime;
}