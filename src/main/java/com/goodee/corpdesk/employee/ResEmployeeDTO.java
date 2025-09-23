package com.goodee.corpdesk.employee;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResEmployeeDTO {

    // Employee
    private String username;
    private Integer positionId;
    private Integer departmentId;
    private String name;

    // Department
    private String departmentName;
    private Integer parentDepartmentId;

    // Position
    private String positionName;
    private Integer parentPositionId;

}
