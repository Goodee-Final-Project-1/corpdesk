package com.goodee.corpdesk.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequest {

  @NotNull(message = "게시판 ID는 필수입니다.")
  private Long boardId;

  @NotBlank(message = "게시글 제목은 필수입니다.")
  private String title;

  @NotBlank(message = "게시글 내용은 필수입니다.")
  private String content;

}
