package com.goodee.corpdesk.board;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
// 실제 데이터베이스 테이블 이름
@Table(name = "board")

public class Board {

  // 게시판 번호
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long boardId;

  // 작성자
  @Column(nullable = false)
  private String username;
  
  // 게시판 구분 ID (0=공지, 1부터=부서ID)
  private Integer departmentId;
  
  // 제목
  @Column(nullable = false)
  private String title;
  
  // 내용
  private String content;
  
  // 조회수
  private Long viewCount;

  // 생성일
  @Column(nullable = false)
  private LocalDateTime createdAt;

  // 수정일
  @Column(nullable = false)
  private LocalDateTime updatedAt;
  
  // 수정자 ID
  private Integer modifiedBy;
  
  // 사용 여부
  private Boolean useYn;

}