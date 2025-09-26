package com.goodee.corpdesk.vacation.repository;

import com.goodee.corpdesk.vacation.entity.VacationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacationDetailRepository extends JpaRepository<VacationDetail, Long> {

    /**
     * Finds the vacation detail that applies to the given user's vacation on the specified date.
     *
     * @param username the user's username whose vacation is being queried
     * @param date the date to check for an active vacation detail
     * @return the VacationDetail covering the specified date for the user's vacation, or `null` if none exists
     */
    @NativeQuery("""
        WITH vd AS (
        	SELECT *
        	FROM vacation_detail
        	WHERE (vacation_id = (SELECT vacation_id FROM vacation WHERE username = :username))
        		AND start_date <= :date
        )
        SELECT *
        FROM vd
        WHERE :date <= end_date
    """)
    public VacationDetail findVacationDetailOnDate(@Param("username") String username, @Param("date")LocalDate date);




	// 캘린더
	@NativeQuery("""
	SELECT *
	FROM vacation_detail
	WHERE vacation_id = :vacationId
	AND (
	    start_date BETWEEN :start AND :end
	    OR
	    end_date BETWEEN :start AND :end
	)
""")
	List<VacationDetail> findAllByVacationIdAndDateTime(Integer vacationId, LocalDate start, LocalDate end);
}
