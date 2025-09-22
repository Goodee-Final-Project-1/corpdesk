package com.goodee.corpdesk.vacation.entity;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@Entity @Table(name = "vacation_type")
@Builder
@NoArgsConstructor @AllArgsConstructor
public class VacationType extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vacationTypeId;

    private String vacationTypeName;


    public ResApprovalDTO toResApprovalDTO() {
        return ResApprovalDTO.builder()
                .vacationTypeId(vacationTypeId)
                .vacationTypeName(vacationTypeName)
                .build();
    }

}
