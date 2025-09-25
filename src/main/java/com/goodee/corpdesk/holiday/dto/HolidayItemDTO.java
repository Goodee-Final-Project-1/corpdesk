package com.goodee.corpdesk.holiday.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class HolidayItemDTO {

    private String dateKind;
    private String dateName;
    private String isHoliday; // 'Y', 'N' 문자열이므로 String 타입
    private Integer locdate; // 20250505와 같은 숫자이므로 Integer 타입
    private Integer seq;

}
