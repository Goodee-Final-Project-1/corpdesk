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




	// 캘린더
//	@NativeQuery("""
//	SELECT
//		vd.vacation_detail_id vacationDetailId,
//		vd.vacation_id vacationId,
//		vd.vacation_type_id vacationTypeId,
//		vd.start_date startDate,
//		vd.end_date endDate,
//		vt.vacation_type_name vacationTypeName
//	FROM vacation_detail vd
//	JOIN vacation_type vt ON vd.vacation_type_id = vt.vacation_type_id
//	WHERE vd.vacation_id = :vacationId
//	AND (
//	    vd.start_date BETWEEN :start AND :end
//	    OR
//	    vd.end_date BETWEEN :start AND :end
//	)
//""")
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
	JOIN VacationType vt ON vd.vacationTypeId = vt.vacationTypeId
	WHERE vd.vacationId = :vacationId
	AND (
		vd.startDate BETWEEN :start AND :end
		OR
		vd.endDate BETWEEN :start AND :end
	)
""")
	List<VacationDetailTypeDTO> findAllByVacationIdAndDate(Integer vacationId, LocalDate start, LocalDate end);

//	@NativeQuery("""
//	SELECT
//		vd.vacation_detail_id vacationDetailId,
//		vd.vacation_id vacationId,
//		vd.vacation_type_id vacationTypeId,
//		vd.start_date startDate,
//		vd.end_date endDate,
//		v.username
//	FROM vacation_detail vd
//	JOIN vacation v ON vd.vacation_id = v.vacation_id
//	WHERE (
//	    vd.start_date BETWEEN :start AND :end
//	    OR
//	    vd.end_date BETWEEN :start AND :end
//	)
//""")
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
		vd.startDate BETWEEN :start AND :end
		OR
		vd.endDate BETWEEN :start AND :end
	)
""")
	List<VacationDetailUsernameDTO> findEveryByDate(LocalDate start, LocalDate end);
}
