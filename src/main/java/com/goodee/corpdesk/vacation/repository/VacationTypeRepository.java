package com.goodee.corpdesk.vacation.repository;

import com.goodee.corpdesk.vacation.entity.VacationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationTypeRepository extends JpaRepository<VacationType, Integer> {
}
