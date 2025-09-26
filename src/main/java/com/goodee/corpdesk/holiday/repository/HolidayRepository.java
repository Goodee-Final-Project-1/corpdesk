package com.goodee.corpdesk.holiday.repository;

import com.goodee.corpdesk.holiday.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}
