package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
	// 기간 조회
    List<Attendance> findByCheckInDateTimeBetween(LocalDateTime start, LocalDateTime end);

    // 직원별 + 기간 조회
    List<Attendance> findByEmployeeIdAndCheckInDateTimeBetween(Integer employeeId, LocalDateTime start, LocalDateTime end);
    
}
