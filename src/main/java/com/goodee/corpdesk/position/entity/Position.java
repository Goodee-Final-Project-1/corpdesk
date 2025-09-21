package com.goodee.corpdesk.position.entity;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Setter
@Getter
@ToString
@Entity @Table(name = "`position`")
@DynamicInsert
@DynamicUpdate
public class Position extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer positionId;

    @Column(nullable = false)
    private Integer parentPositionId;

    @Column(nullable = false)
    private String positionName;

}
