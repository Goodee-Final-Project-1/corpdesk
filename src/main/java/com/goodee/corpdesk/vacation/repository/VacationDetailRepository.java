package com.goodee.corpdesk.vacation.repository;

import com.goodee.corpdesk.vacation.entity.Vacation;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface VacationDetailRepository extends JpaRepository<VacationDetail, Long> {

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

}
