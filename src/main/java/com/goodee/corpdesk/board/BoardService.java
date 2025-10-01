package com.goodee.corpdesk.board;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

  @Autowired
  private BoardRepository boardRepository;

  // 전체 부서 게시판 목록
  public List<Board> getAllDepartmentBoards() {
    return boardRepository.findByDepartmentIdIsNotNullAndUseYnTrueOrderByDepartmentIdAsc();
  }

  // 특정 부서 게시판 조회
  public List<Board> getDepartmentBoard(Long departmentId) {
    return boardRepository.findByDepartmentId(departmentId);
  }

  // 공지 게시판
  public List<Board> getCommonBoard() {
    return boardRepository.findByDepartmentIdIsNullAndUseYnTrueOrderByCreatedAtDesc();
  }

  // 게시판 생성 (중복 방지)
  public Board createBoard(Board board) throws IllegalAccessException {
    // 부서ID가 있는 게시판은 중복 생성 방지
    if (board.getDepartmentId() != null && boardRepository.existsByDepartmentId(board.getDepartmentId())) {
      throw new IllegalAccessException("해당 부서의 게시판이 이미 존재합니다");
    }
    
    board.setCreatedAt(LocalDateTime.now());
    board.setUpdatedAt(LocalDateTime.now());
    board.setUseYn(true);

    return boardRepository.save(board);
  }

}
