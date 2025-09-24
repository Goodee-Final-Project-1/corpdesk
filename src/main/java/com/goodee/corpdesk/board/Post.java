package com.goodee.corpdesk.board;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString (exclude = "board")
public class Post {

  // PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  // FK : Board 엔티티와 다대일(N:1) 관계 설정
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id", nullable = false)
  private Board board;

  // 제목
  @Column(nullable = false, length = 255)
  private String title;

  // 내용
  @Column(nullable = false, columnDefinition = "LONGTEXT")
  private String content;

  // 작성자
  private int modifiedBy;

  // 조회수
  @Column(nullable = false)
  private Long viewCount = 0L;

  // 사용 여부
  @Column(nullable = false)
  private Boolean useYn;

  // 생성 시간
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // 수정 시간
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    if(this.useYn == null) this.useYn = true;
    if(this.viewCount == null) this.viewCount = 0L;
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

}
