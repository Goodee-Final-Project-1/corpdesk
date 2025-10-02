package com.goodee.corpdesk.position.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    public Integer getPositionIdByName(String positionName) {
        return positionRepository.findByPositionName(positionName)
                .map(Position::getPositionId)
                .orElse(null);
    }
}
