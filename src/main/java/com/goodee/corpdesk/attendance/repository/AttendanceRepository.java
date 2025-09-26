package com.goodee.corpdesk.attendance.repository;

import com.goodee.corpdesk.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
//    // 기간 내 모든 근태
//    List<Attendance> findAllByCheckInDatetimeBetween(LocalDateTime start, LocalDateTime end);
//
//    // 직원별 + 기간 내 근태 (필요시 사용)
//    List<Attendance> findAllByEmployeeIdAndCheckInDatetimeBetween(Long employeeId, LocalDateTime start, LocalDateTime end);
    
	/**
 * Retrieve attendance records for a specific user filtered by the `useYn` flag.
 *
 * @param username the user's username
 * @param useYn    `true` to fetch active records, `false` to fetch inactive records
 * @return         a list of Attendance entities matching the given username and `useYn`; an empty list if none found
 */
	List<Attendance> findByUsernameAndUseYn(String username, Boolean useYn);
	
	/**
     * Marks the specified attendance records as inactive by setting their `useYn` flag to false.
     *
     * @param ids the attendance record IDs to mark inactive
     */
    @Modifying
    @Query("UPDATE Attendance a SET a.useYn = false WHERE a.attendanceId IN :ids")
    void softDeleteByIds(@Param("ids") List<Long> ids);


    /**
     * Retrieve the most recent attendance record for a user.
     *
     * @param username the username to look up
     * @return the Attendance with the latest check-in time for the given username, or `null` if none exists
     */
    @NativeQuery("""
        SELECT *
        FROM attendance
        WHERE
        	check_in_date_time = (SELECT MAX(check_in_date_time) FROM attendance WHERE username = :username)
        	AND username  = :username
    """)
    public Attendance findLatestAttendanceByUsername(@Param("username") String username);

    /**
     * Retrieves the oldest check-in timestamp for the specified user.
     *
     * @param username the username whose earliest attendance check-in time to retrieve
     * @return the earliest check_in_date_time for the given username, or null if no records exist
     */
    @NativeQuery("""
        SELECT check_in_date_time AS oldestCheckInDateTime
        FROM attendance
        WHERE
        	check_in_date_time = (SELECT MIN(check_in_date_time) FROM attendance WHERE username = :username)
        	AND username  = :username
    """)
    public Timestamp findOldestAttendanceByUsername(@Param("username") String username);


    /**
 * Finds attendance records for a user whose check-in timestamps fall within the specified range.
 *
 * @param username the user's username to filter records by
 * @param start the inclusive lower bound of the check-in date-time range
 * @param end the inclusive upper bound of the check-in date-time range
 * @return a list of Attendance entities matching the username with check-in date-times between `start` and `end`
 */
    List<Attendance> findAllByUsernameAndCheckInDateTimeBetween(String username, LocalDateTime start, LocalDateTime end);





	// 캘린더
	@NativeQuery("""
	SELECT *
	FROM attendance
	WHERE username = :username
	AND (
	    check_in_date_time BETWEEN :start AND :end
	    OR
	    check_out_date_time BETWEEN :start AND :end
	)
""")
	List<Attendance> findAllByUsernameAndDateTime(String username, LocalDateTime start, LocalDateTime end);
}
