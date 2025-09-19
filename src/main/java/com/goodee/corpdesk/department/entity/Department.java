package com.goodee.corpdesk.department.entity;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Setter
@Getter
@ToString
@SuperBuilder
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "department")
@DynamicInsert
@DynamicUpdate
public class Department extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(nullable = false)
    private Long departmentHigh;

    @Column(nullable = false)
    private String departmentName;

}
