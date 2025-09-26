package com.goodee.corpdesk.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCreateRequstDTO {

  private String username;
  private String title;
  private String content;
  private Integer departmentId;

}
