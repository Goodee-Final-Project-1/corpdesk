package com.goodee.corpdesk.holiday.entity;

import com.goodee.corpdesk.common.BaseEntity;
import com.goodee.corpdesk.holiday.dto.HolidayDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Entity @Table(name = "holiday")
@DynamicInsert
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Holiday extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holidayId;
    private String dateName;
    private LocalDate locdate;
    private Character isHoliday;

}
