package com.goodee.corpdesk.board;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

  // 특정 부서 게시판
  List<Board> findByDepartmentId(Long departmentId);

  // 특정 부서 게시판 존재 여부
  boolean existsByDepartmentId(Integer departmentId);
  
  // 부서 게시판만
  List<Board> findByDepartmentIdIsNotNullAndUseYnTrueOrderByDepartmentIdAsc();

  // 공지 게시판만
  List<Board> findByDepartmentIdIsNullAndUseYnTrueOrderByCreatedAtDesc();



}
