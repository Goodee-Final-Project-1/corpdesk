package com.goodee.corpdesk.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


  // 특정 부서(departmentId) + 사용중만을 최신순으로 페이징 조회
  Page<Board> findByDepartmentIdAndUseYnOrderByCreatedAtDesc(Integer departmentId, Boolean useYn, Pageable pageable);

}
