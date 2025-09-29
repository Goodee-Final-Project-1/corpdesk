package com.goodee.corpdesk.vacation.repository;

import com.goodee.corpdesk.vacation.dto.VacationDetailTypeDTO;
import com.goodee.corpdesk.vacation.dto.VacationDetailUsernameDTO;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
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

    @NativeQuery("""
        SELECT sum(used_days)
        FROM vacation_detail
        WHERE username = :username
    """)
    public Integer countUsedVacationDays(@Param("username") String username);



	// 캘린더
	@Query("""
	SELECT new com.goodee.corpdesk.vacation.dto.VacationDetailTypeDTO(
		vd.vacationDetailId,
		vd.vacationId,
		vd.vacationTypeId,
		vd.startDate,
		vd.endDate,
		vt.vacationTypeName
	)
	FROM VacationDetail vd
	JOIN Vacation v ON v.vacationId = vd.vacationId
	JOIN VacationType vt ON vd.vacationTypeId = vt.vacationTypeId
	WHERE v.username = :username
	AND (
		vd.startDate <= :end
		AND
		vd.endDate >= :start
	)
""")
	List<VacationDetailTypeDTO> findAllByVacationIdAndDate(String username, LocalDate start, LocalDate end);

	@Query("""
	SELECT new com.goodee.corpdesk.vacation.dto.VacationDetailUsernameDTO(
		vd.vacationDetailId,
		vd.vacationId,
		vd.vacationTypeId,
		vd.startDate,
		vd.endDate,
		v.username
	)
	FROM VacationDetail vd
	JOIN Vacation v ON vd.vacationId = v.vacationId
	WHERE (
		vd.startDate <= :end
		AND
		vd.endDate >= :start
	)
""")
	List<VacationDetailUsernameDTO> findEveryByDate(LocalDate start, LocalDate end);
}
