package com.goodee.corpdesk.vacation;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity @Table(name = "vacation_type")
public class VacationType extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vacationTypeId;

    private String vacationTypeName;

}
