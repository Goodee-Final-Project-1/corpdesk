package com.goodee.corpdesk.position.entity;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
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

    private Integer parentPositionId;

    @Column(nullable = false)
    private String positionName;

    public Position(Integer parentPositionId, String positionName) {
        this.parentPositionId = parentPositionId;
        this.positionName = positionName;
    }

}
