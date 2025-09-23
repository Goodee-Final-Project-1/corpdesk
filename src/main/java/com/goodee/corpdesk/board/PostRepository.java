package com.goodee.corpdesk.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

  // 특정 게시판의 활성글만 페이지로 조회
  Page<Post> findByBoard_BoardIdAndUseYnTrue(Long boardId, Pageable pageable);

  // 단건 상세 조회시에도 비활성 글은 보이지 않도록 useYn 조건으로 조회
  Optional<Post> findByPostIdAndUseYnTrue(Long postId);

  // 조회수 증가를 동시성 안전하게 DB 한 방으로 처리
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Transactional
  @Query("update Post p set p.viewCount = p.viewCount + 1 where p.postId = :postId and p.useYn = true")
  int increaseViewCount(@Param("postId") Long postId);

  // 소프트 삭제: 물리 삭제 대신 useYn=false로 전환하고 updatedAt 갱신
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Transactional
  @Query("update Post p set p.useYn = false, p.updatedAt = current_timestamp where p.postId = :postId and p.useYn = true")
  int softDelete(@Param("postId") Long postId);
  
  // 복구: 비활성 글을 다시 활성화하고 updatedAt 갱신
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Transactional
  @Query("update Post p set p.useYn = true, p.updatedAt = current_timestamp where p.postId = :postId and p.useYn = false")
  int restore(@Param("postId") Long postId);

}
