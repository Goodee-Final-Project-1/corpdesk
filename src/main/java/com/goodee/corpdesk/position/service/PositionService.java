package com.goodee.corpdesk.position.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodee.corpdesk.position.dto.PositionDTO;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PositionService {

    
    private final PositionRepository positionRepository;
    
    public void deleteOneAndReparent(Integer positionId) {
        Position target = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("직위를 찾을 수 없습니다. id=" + positionId));

        Integer parentId = target.getParentPositionId(); // null이면 최상위

        // 1) 삭제 대상의 직계 자식 수 검사 (부모당 자식 1명 정책)
        int childCount = positionRepository.countByParentPositionIdAndUseYnTrue(positionId);
        if (childCount > 1) {
            throw new IllegalStateException("해당 직위의 하위 직위가 2개 이상이라 삭제할 수 없습니다. 먼저 하위 직위를 정리하세요.");
        }

        // 2) 부모가 이미 '삭제 대상 이외의' 다른 자식을 갖고 있는지 검사
        if (parentId != null) {
            int siblingsAtParent =
                    positionRepository.countActiveChildrenExcluding(parentId, java.util.List.of(positionId));
            if (siblingsAtParent > 0 && childCount > 0) {
                // 부모에 이미 자식이 있는 상태에서 또 다른 자식을 올리려 함 → 제약 위반
                throw new IllegalStateException("상위 직위에 이미 다른 자식 직위가 있어 재연결할 수 없습니다.");
            }
        }

        // 3) 먼저 삭제 대상 소프트 삭제 (부모의 자식 슬롯 확보)
        positionRepository.softDelete(positionId);

     // 자식이 1명이면 재연결
        if (childCount == 1) {
            // 루트로 승격되는 경우(삭제 대상의 parent==null) → 루트 단일성 체크
            if (parentId == null &&
                positionRepository.existsByParentPositionIdIsNullAndUseYnTrue()) {
                // 방금 삭제한 루트가 use_yn=false가 되었으므로, 이 체크는 "다른 활성 루트가 남아있냐"를 본다.
                throw new IllegalStateException("이미 다른 최상위 직위가 존재하여 루트 승격이 불가합니다.");
            }
            positionRepository.reparentChildren(positionId, parentId); // parentId==null이면 루트 승격
        }
    }
    
    
    public void deleteAllAndReparent(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;

        // 삭제 대상의 parent 맵 조회
        Map<Integer, Integer> parentMap = positionRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Position::getPositionId, Position::getParentPositionId));

        Set<Integer> toDelete = new HashSet<>(ids);

        for (Integer id : ids) {
            // 1) 새 parent 찾기: 삭제 집합에 포함되지 않은 가장 가까운 조상
        	Integer newParent = parentMap.get(id);
        	while (newParent != null && toDelete.contains(newParent)) {
        	    // parentMap에 없으면 DB에서 parent를 읽어옴
        	    Integer next = parentMap.containsKey(newParent)
        	            ? parentMap.get(newParent)
        	            : positionRepository.findById(newParent)
        	                  .map(Position::getParentPositionId)
        	                  .orElse(null);
        	    newParent = next;
        	}
            
            //  2) 새 부모의 남는 활성 자식 검사 (삭제 대상은 제외)
            int remaining = positionRepository.countActiveChildrenExcluding(newParent, ids);
            if (remaining > 0) {
                throw new IllegalStateException("상위 직위에 이미 자식 직위가 있어 재연결할 수 없습니다. id=" + newParent);
            }
            
            // 2) 자식들 재연결
            positionRepository.reparentChildren(id, newParent);
        }

        // 3) 마지막에 한 번만 벌크 소프트 삭제
            positionRepository.softDeleteIn(ids);
        
    }
    
    

    public Integer getPositionIdByName(String positionName) {
        return positionRepository.findByPositionName(positionName)
                .map(Position::getPositionId)
                .orElse(null);
    }
    
 // 직위 목록 + 사용 사원 수 + 검색
    public List<PositionDTO> getAllWithEmployeeCount(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return positionRepository.findAllWithEmployeeCount();
        } else {
            return positionRepository.findAllWithEmployeeCountByKeyword(keyword.trim());
        }
    }

    //  직위 생성: save 사용
    public void create(String positionName, Integer parentPositionId) {
        // 1) 입력 검증
        String trimmed = (positionName == null) ? null : positionName.strip();
        if (trimmed == null || trimmed.isEmpty()) {
            throw new IllegalArgumentException("직위명은 필수입니다.");
        }
        if (trimmed.length() > 50) {
            throw new IllegalArgumentException("직위명은 50자 이내여야 합니다.");
        }

        // 2) 중복(활성) 확인 — 필요시 정규화 키 사용
        if (positionRepository.existsByPositionNameAndUseYnTrue(trimmed)) {
            throw new IllegalArgumentException("이미 존재하는 직위명입니다: " + trimmed);
        }

        // 3) 루트 단 하나 정책
        if (parentPositionId == null &&
            positionRepository.existsByParentPositionIdIsNullAndUseYnTrue()) {
            throw new IllegalStateException("최상위 직위는 하나만 생성할 수 있습니다.");
        }

        // 4) 상위 직위 제약
        if (parentPositionId != null &&
            positionRepository.existsByParentPositionIdAndUseYnTrue(parentPositionId)) {
            throw new IllegalArgumentException("해당 상위 직위에는 이미 자식 직위가 존재합니다.");
        }

        // 5) 상위 직위 존재/활성 확인
        if (parentPositionId != null &&
            positionRepository.findByPositionIdAndUseYnTrue(parentPositionId).isEmpty()) {
            throw new IllegalArgumentException("상위 직위가 존재하지 않거나 비활성 상태입니다: " + parentPositionId);
        }

        // 6) 저장
        Position p = new Position();
        p.setPositionName(trimmed);
        p.setParentPositionId(parentPositionId);
        p.setUseYn(true);
        positionRepository.save(p);
    }


    
    // ✅ 일괄 삭제: JPA 기본 제공 메서드 사용
    public void deleteAll(List<Integer> ids) {
        List<Position> positions = positionRepository.findAllById(ids);
        for (Position p : positions) {
            p.setUseYn(false);
        }
        positionRepository.saveAll(positions);
    }
    
    public List<Position> getAllActive() {
        return positionRepository.findByUseYnTrueOrderByPositionIdAsc();
    }
    
    public void changeParent(Integer positionId, Integer newParentId) {
        Position p = positionRepository.findById(positionId)
            .orElseThrow(() -> new IllegalArgumentException("직위 없음: " + positionId));

        // (선택) 비활성 노드 변경 금지 정책
        // if (!Boolean.TRUE.equals(p.getUseYn())) {
        //     throw new IllegalStateException("비활성 직위는 상위 변경이 불가합니다.");
        // }

        Integer currentParentId = p.getParentPositionId();

        // 0) 무의미한 변경은 패스
        if ((currentParentId == null && newParentId == null) ||
            (currentParentId != null && currentParentId.equals(newParentId))) {
            return; // no-op
        }

        // 1) 자기 참조 금지
        if (newParentId != null && positionId.equals(newParentId)) {
            throw new IllegalArgumentException("자기 자신을 상위로 지정할 수 없습니다.");
        }

        // 2) 루트 단일성: p가 현재 루트가 아닐 때만 단일성 검사
        if (newParentId == null && currentParentId != null &&
            positionRepository.existsByParentPositionIdIsNullAndUseYnTrue()) {
            throw new IllegalStateException("최상위 직위는 하나만 가능합니다.");
        }

        // 3) 새 부모 존재/활성 검증
        if (newParentId != null &&
            positionRepository.findByPositionIdAndUseYnTrue(newParentId).isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 상위 직위입니다: " + newParentId);
        }

        // 4) 부모당 자식 1개 정책: 새 부모에 이미 다른 자식이 있으면 금지
        if (newParentId != null &&
            positionRepository.existsByParentPositionIdAndUseYnTrue(newParentId)) {
            throw new IllegalStateException("해당 상위 직위에는 이미 자식 직위가 존재합니다.");
        }

        // 5) 사이클 방지: newParentId 사슬 상에 positionId가 있으면 금지
        Integer cursor = newParentId;
        while (cursor != null) {
            if (cursor.equals(positionId)) {
                throw new IllegalStateException("상위 변경 시 계층 순환이 발생합니다.");
            }
            cursor = positionRepository.findById(cursor)
                .map(Position::getParentPositionId)
                .orElse(null);
        }

        // 6) 변경 적용
        p.setParentPositionId(newParentId);
    }

    
}
