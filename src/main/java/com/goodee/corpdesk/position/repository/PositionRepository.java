package com.goodee.corpdesk.position.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.position.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Optional<Position> findByPositionName(String positionName);
    boolean existsByPositionName(String positionName);
}
