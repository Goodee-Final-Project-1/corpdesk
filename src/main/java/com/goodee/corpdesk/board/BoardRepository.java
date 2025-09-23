package com.goodee.corpdesk.board;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository extends JpaRepository<Board, Long> {

  // 부서 ID로 게시판 조회
  // Optional<Board> findByDepartment_DeptId(Integer departmentId);

  // 게시판을 비활성화(소프트삭제)
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Transactional
  @Query("update Board b set b.useYn = false, b.updatedAt = current_timestamp where b.boardId = :boardId and b.useYn = true")
  int softDelete(@Param("boardId") Long boardId);

}
