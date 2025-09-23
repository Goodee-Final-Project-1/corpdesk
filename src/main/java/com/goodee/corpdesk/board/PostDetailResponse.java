package com.goodee.corpdesk.board;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class PostDetailResponse {

  private final Long postId;
  private final String title;
  private final String content;
  private final int modifiedBy;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final Long viewCount;

  // Post 엔티티를 DTO로 변환하는 생성자
  public PostDetailResponse(Post post) {
    this.postId = post.getPostId();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.modifiedBy = post.getModifiedBy();
    this.createdAt = post.getCreatedAt();
    this.updatedAt = post.getUpdatedAt();
    this.viewCount = post.getViewCount();
  }


}
