package com.goodee.corpdesk.board;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseDTO {

  private Long boardId;
  private String username;
  private String title;
  private String content;
  private Long viewCount;
  private LocalDateTime createdAt;

  public static BoardResponseDTO from(Board board) {
    BoardResponseDTO dto = new BoardResponseDTO();
    dto.boardId = board.getBoardId();
    dto.username = board.getUsername();
    dto.title = board.getTitle();
    dto.content = board.getContent();
    dto.viewCount = board.getViewCount();
    dto.createdAt = board.getCreatedAt();

    return dto;
  }

}
