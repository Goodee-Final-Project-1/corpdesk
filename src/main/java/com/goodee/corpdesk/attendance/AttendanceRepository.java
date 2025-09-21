package com.goodee.corpdesk.attendance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
//    // 기간 내 모든 근태
//    List<Attendance> findAllByCheckInDatetimeBetween(LocalDateTime start, LocalDateTime end);
//
//    // 직원별 + 기간 내 근태 (필요시 사용)
//    List<Attendance> findAllByEmployeeIdAndCheckInDatetimeBetween(Long employeeId, LocalDateTime start, LocalDateTime end);
    
	List<Attendance> findByUsername(String username);
	
}
