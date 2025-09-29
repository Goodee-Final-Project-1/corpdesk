package com.goodee.corpdesk.vacation.entity;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Setter
@Getter
@ToString
@Entity @Table(name = "vacation")
@DynamicInsert @DynamicUpdate
public class Vacation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vacationId;

    @Column(nullable = false)
    private String username;

    @ColumnDefault("0")
    private Integer totalVacation;

    @ColumnDefault("0")
    private Integer remainingVacation;

    @ColumnDefault("0")
    private Integer usedVacation;

}
