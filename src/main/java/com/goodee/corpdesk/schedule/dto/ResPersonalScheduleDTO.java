package com.goodee.corpdesk.schedule.dto;

import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class ResPersonalScheduleDTO {

    private Long personalScheduleId;
    private String scheduleName;
    private LocalDateTime scheduleDateTime;
    private String content;
    private String address;

    public ResPersonalScheduleDTO(Long personalScheduleId, String scheduleName
        , LocalDateTime scheduleDateTime, String content, String address) {
        this.personalScheduleId = personalScheduleId;
        this.scheduleName = scheduleName;
        this.scheduleDateTime = scheduleDateTime;
        this.content = content;
        this.address = address;
    }
}
