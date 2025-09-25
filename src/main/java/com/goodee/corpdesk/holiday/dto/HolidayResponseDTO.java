package com.goodee.corpdesk.holiday.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true) // 💡알 수 없는 필드를 무시하도록 설정
public class HolidayResponseDTO {

    private HolidayHeaderDTO header;
    private HolidayBodyDTO body;

}
