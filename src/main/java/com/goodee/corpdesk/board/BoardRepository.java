package com.goodee.corpdesk.board;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

  // 활성 글만 페이징 조회
  Page<Board> findByDepartmentIdAndUseYnTrue(Integer departmentId, Pageable pageable);

  // 공지 전용 (departmentId = 0, 최신순)
  @Query("select b from Board b where b.useYn = true and b.departmentId = 0 order by b.createdAt desc")
  Page<Board> findNoticePosts(Pageable pageable);

  // 부서 전용 (departmentId > 0, 최신순)
  Page<Board> findByDepartmentIdGreaterThanAndUseYnTrueOrderByCreatedAtDesc(Integer minDepartmentId, Pageable pageable);

  // 글 상세 (useYn = true)
  Optional<Board> findByBoardIdAndUseYnTrue(Long boardId);

}
