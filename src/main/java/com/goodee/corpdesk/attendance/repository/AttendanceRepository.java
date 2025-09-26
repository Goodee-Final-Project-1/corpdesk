package com.goodee.corpdesk.attendance.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


    @NativeQuery("""
        SELECT *
        FROM attendance
        WHERE
        	check_in_date_time = (SELECT MAX(check_in_date_time) FROM attendance WHERE username = :username)
        	AND username  = :username
    """)
    public Attendance findLatestAttendanceByUsername(@Param("username") String username);

    @NativeQuery("""
        SELECT check_in_date_time AS oldestCheckInDateTime
        FROM attendance
        WHERE
        	check_in_date_time = (SELECT MIN(check_in_date_time) FROM attendance WHERE username = :username)
        	AND username  = :username
    """)
    public Timestamp findOldestAttendanceByUsername(@Param("username") String username);


    // 캘린더
    List<Attendance> findAllByUsernameAndCheckInDateTimeBetween(String username, LocalDateTime start, LocalDateTime end);
}
