package com.goodee.corpdesk.department.entity;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
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

    // DataInitializer에서 데이터 넣어서 테스트하기 위해 임시로 닫아둠
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer departmentId;

    private Integer parentDepartmentId;

    @Column(nullable = false)
    private String departmentName;

    public ResApprovalDTO toResApprovalDTO() {
        return ResApprovalDTO.builder()
                .departmentId(departmentId)
                .departmentName(departmentName)
                .build();
    }

    // departmentName만 받는 생성자 추가
    public Department(Integer parentDepartmentId, String departmentName) {
        this.parentDepartmentId = parentDepartmentId;
        this.departmentName = departmentName;
    }

}
