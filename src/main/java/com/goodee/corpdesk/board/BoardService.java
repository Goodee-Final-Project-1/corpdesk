package com.goodee.corpdesk.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

  @Autowired
  private BoardRepository boardRepository;

  // 모든 게시판 목록을 조회하는 메소드
  @Transactional(readOnly = true)
  public List<Board> findAllBoards() {
    return boardRepository.findAll();
  }

  // 게시판 생성 메소드
  @Transactional
  public void createBoard(String boardName) {
    Board newBoard = new Board();
    newBoard.setBoardName(boardName);
    // @PrePersist가 나머지 필드를 자동으로 채워줌
    boardRepository.save(newBoard);
  }

}
