package com.goodee.corpdesk.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceEditDTO {
    private Long attendanceId;
    private String workStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private String dateTime;
}