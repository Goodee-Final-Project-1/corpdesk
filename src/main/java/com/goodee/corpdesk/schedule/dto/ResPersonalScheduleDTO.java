package com.goodee.corpdesk.schedule.dto;

import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ResPersonalScheduleDTO {

    private Long personalScheduleId;

}
