package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "attendance")
public class Attendance {
	
	// PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;
    
    // 직원 ID
    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;
    
    // 출근 일시
    @Column(name = "check_in_datetime")
    private LocalDateTime checkInDateTime;
    
    // 퇴근 일시
    @Column(name = "check_out_datetime")
    private LocalDateTime checkOutDateTime;
    
    // 휴일 여부
    @Column(name = "is_holiday")
    private boolean isHoliday;
    
    // 근무 상태
    @Column(name = "work_status", nullable = false, length = 255)
    private String workStatus;
        
    // 생성 일시
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

    // 수정 일시
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 수정한 사람
    @Column(name = "modified_by", nullable = false, length = 255)
    private String modifiedBy;
    
    // 사용 여부
    @Column(name = "use_yn", nullable = false)
    private Boolean useYn;
    
}
