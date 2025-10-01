package com.goodee.corpdesk.schedule.repository;

import com.goodee.corpdesk.approval.entity.ApprovalForm;
import com.goodee.corpdesk.schedule.dto.ReqPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonalScheduleRepository extends JpaRepository<PersonalSchedule, Long> {

    @Query("""
        SELECT new com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO(
        ps.personalScheduleId, ps.scheduleName, ps.scheduleDateTime, ps.content, ps.address
        )
        FROM PersonalSchedule ps
        WHERE
            ps.useYn = :useYn
            AND ps.username = :username
            AND (:year IS NULL OR YEAR(ps.scheduleDateTime) = :year)
            AND (:month IS NULL OR MONTH(ps.scheduleDateTime) = :month)
        ORDER BY ps.scheduleDateTime DESC
    """)
    List<ResPersonalScheduleDTO> findPersonalScheduleByUsernameAndYearMonth(@Param("useYn") Boolean useYn
                                                                            , @Param("username") String username
                                                                            , @Param("year") Integer year
                                                                            , @Param("month") Integer month);

    @Query("""
        SELECT YEAR(MIN(ps.scheduleDateTime))
        FROM PersonalSchedule ps
        WHERE
            ps.useYn = :useYn
            AND ps.username = :username
    """)
    Integer findOldestScheduleYearByUsername(@Param("useYn") Boolean useYn, @Param("username") String username);
}
