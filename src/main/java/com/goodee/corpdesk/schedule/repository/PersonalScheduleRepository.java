package com.goodee.corpdesk.schedule.repository;

import com.goodee.corpdesk.approval.entity.ApprovalForm;
import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalScheduleRepository extends JpaRepository<PersonalSchedule, Long> {

}
