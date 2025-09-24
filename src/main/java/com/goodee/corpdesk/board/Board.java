package com.goodee.corpdesk.board;

import java.time.LocalDateTime;

import com.goodee.corpdesk.department.entity.Department;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString (exclude = "department")

public class Board {

  // PK 자동증가 컬럼
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long boardId;

  // 게시판 표시 이름(예: 인사팀 게시판)
  @Column(nullable = false, length = 255)
  private String boardName;

  // 게시판 제목
  @Column(nullable = false, length = 255)
  private String title;

  // 게시판 유형
  @Column(nullable = false, length = 255)
  private String boardType;

  // 사용 여부
  @Column(nullable = false)
  private Boolean useYn;

  // 마지막 수정자
  @Column(length = 255)
  private String modifiedBy;

  // 생성 시간
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // 수정 시간
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  // Department 와의 1:1 관계. Board 가 department_id 외래 키를 가짐
  // @OneToOne(fetch = FetchType.LAZY)
  // @JoinColumn(name = "department_id", nullable = false, unique = true)
  // private Department department;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    if (this.useYn == null) this.useYn = true;
    if (this.boardType == null) this.boardType = "DEPARTMENT";
    if (this.title == null) this.title = this.boardName;
    }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

}


