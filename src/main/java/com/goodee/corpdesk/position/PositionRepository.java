package com.goodee.corpdesk.position;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Integer>{
    Optional<Position> findByPositionName(String positionName);
    boolean existsByPositionName(String positionName);
}
