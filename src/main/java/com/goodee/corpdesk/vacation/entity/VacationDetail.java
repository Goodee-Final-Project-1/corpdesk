package com.goodee.corpdesk.vacation.entity;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@Entity @Table(name = "vacation_detail")
@DynamicInsert @DynamicUpdate
public class VacationDetail extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacation_detail_id;

    @Column(nullable = false)
    private Integer vacation_id;

    @Column(nullable = false)
    private Integer vacation_type_id;

    @Column(nullable = false)
    private LocalDate start_date;

    @Column(nullable = false)
    private LocalDate end_date;

}
