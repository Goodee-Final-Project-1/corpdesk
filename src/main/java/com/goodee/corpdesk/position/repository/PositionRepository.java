package com.goodee.corpdesk.position.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goodee.corpdesk.position.dto.PositionDTO;
import com.goodee.corpdesk.position.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Optional<Position> findByPositionName(String positionName);
    Optional<Position> findById(Integer id);
    
    Optional<Position> findByPositionIdAndUseYnTrue(Integer positionId);
    
    // 부모에 활성 자식 수
    int countByParentPositionIdAndUseYnTrue(Integer parentPositionId);
    // 부모에 활성 자식 수(특정 자식은 제외) → 삭제 대상 자신을 제외하고 검사
    int countByParentPositionIdAndUseYnTrueAndPositionIdNot(Integer parentPositionId, Integer excludeChildId);
    
    
    // 활성 루트(부모=null) 존재 여부
    boolean existsByParentPositionIdIsNullAndUseYnTrue();
    boolean existsByPositionNameAndUseYnTrue(String positionName);
    boolean existsByParentPositionIdAndUseYnTrue(Integer parentId);
    
    // 직계 자식들 조회
    List<Position> findByParentPositionIdAndUseYnTrue(Integer parentPositionId);

    // 자식들의 parent를 일괄 변경
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Position p
           SET p.parentPositionId = :newParentId
         WHERE p.parentPositionId = :oldParentId
           AND p.useYn = true
    """)
    int reparentChildren(@Param("oldParentId") Integer oldParentId,
                         @Param("newParentId") Integer newParentId);

    // 소프트 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Position p
           SET p.useYn = false
         WHERE p.positionId = :id
    """)
    int softDelete(@Param("id") Integer id);
    
    
    
    boolean existsByPositionName(String positionName);
    
    List<Position> findByUseYnTrueOrderByPositionIdAsc();
    
    @Query("""
    		SELECT new com.goodee.corpdesk.position.dto.PositionDTO(
    		  p.positionId,
    		  p.positionName,
    		  (SELECT COUNT(e.username)
    		     FROM Employee e
    		     WHERE e.useYn = true AND e.positionId = p.positionId),
    		  p.parentPositionId
    		)
    		FROM Position p
    		WHERE p.useYn = true
    		ORDER BY
    		  COALESCE(p.parentPositionId, 0) ASC,
    		  p.positionId ASC                                         
    		""")
    		List<PositionDTO> findAllWithEmployeeCount();


    		@Query("""
    		SELECT new com.goodee.corpdesk.position.dto.PositionDTO(
    		  p.positionId,
    		  p.positionName,
    		  (SELECT COUNT(e.username)
    		     FROM Employee e
    		     WHERE e.useYn = true AND e.positionId = p.positionId),
    		  p.parentPositionId
    		)
    		FROM Position p
    		WHERE p.useYn = true
    		  AND p.positionName LIKE CONCAT('%', :keyword, '%')
    		ORDER BY p.positionId
    		""")
    		List<PositionDTO> findAllWithEmployeeCountByKeyword(@Param("keyword") String keyword);
    		
}
