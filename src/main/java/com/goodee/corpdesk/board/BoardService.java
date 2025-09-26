package com.goodee.corpdesk.board;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

  @Autowired
  private BoardRepository boardRepository;

  // 특정 부서의 게시글 페이징 조회
  public Page<Board> getDepartmentList(Integer departmentId, Pageable pageable) {
    // 부서게시판은 departmentId가 1 이상
    if (departmentId == null || departmentId < 1) throw new IllegalArgumentException("유효하지 않는 부서입니다");
    // 부서ID와 사용여부=true 조건으로 최신순 페이징 조회
    return boardRepository.findByDepartmentIdAndUseYnOrderByCreatedAtDesc(departmentId, true, pageable);
  }

  // 게시글 상세 조회
  public Board getBoard(Long boardId) {
    return boardRepository.findById(boardId)
          .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. id=" + boardId));
  }

  // 게시글 등록
  public Board createBoard(Board board) {
    return boardRepository.save(board);
  }

}
