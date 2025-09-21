package com.goodee.corpdesk.position.repository;

import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.position.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Integer> {
}
