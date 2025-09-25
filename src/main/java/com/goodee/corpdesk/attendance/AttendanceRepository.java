package com.goodee.corpdesk.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
//    // 기간 내 모든 근태
//    List<Attendance> findAllByCheckInDatetimeBetween(LocalDateTime start, LocalDateTime end);
//
//    // 직원별 + 기간 내 근태 (필요시 사용)
//    List<Attendance> findAllByEmployeeIdAndCheckInDatetimeBetween(Long employeeId, LocalDateTime start, LocalDateTime end);
    
	// 살아있는 데이터만 조회
	List<Attendance> findByUsernameAndUseYn(String username, Boolean useYn);
	
	
	@Modifying
    @Query("UPDATE Attendance a SET a.useYn = false WHERE a.attendanceId IN :ids")
    void softDeleteByIds(@Param("ids") List<Long> ids);



































	// 캘린더
	List<Attendance> findAllByCheckInDateTimeBetween(LocalDateTime start, LocalDateTime end);




}
