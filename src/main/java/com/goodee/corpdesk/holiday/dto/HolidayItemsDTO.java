package com.goodee.corpdesk.holiday.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class HolidayItemsDTO {

    private List<HolidayItemDTO> item;

}
