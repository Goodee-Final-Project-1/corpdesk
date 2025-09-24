package com.goodee.corpdesk.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private BoardRepository boardRepository;

  // 게시글 생성
  @Transactional
  public Long createPost(PostCreateRequest request) {
    // 1. 요청 받은 boardId로 게시판이 실제로 존재하는지 확인
    Board board = boardRepository.findById(request.getBoardId())
        .orElseThrow(() -> new IllegalArgumentException("해당 게시판을 찾을 수 없습니다. id=" + request.getBoardId()));

    // 2. DTO를 Post 엔티티로 변환
    Post newPost = new Post();
    newPost.setBoard(board);
    newPost.setTitle(request.getTitle());
    newPost.setContent(request.getContent());
    
    // 3. 리포지토리를 통해 엔티티를 DB에 저장
    Post savedPost = postRepository.save(newPost);

    return savedPost.getPostId();
  }

  // 게시글 상세 조회
  @Transactional(readOnly = true)
  public PostDetailResponse getPostDetail(Long postId) {
    // 1. postId로 게시글을 조회 ( 없으면 예외 발생)
    Post post = postRepository.findByPostIdAndUseYnTrue(postId)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

    // 2. 조회한 엔티티를 DTO로 변환하여 반환
    return new PostDetailResponse(post);
  }

  // 게시글 목록 조회
  @Transactional(readOnly = true)
  public Page<Post> getPostsByBoardId(Long boardId, Pageable pageable) {
    return postRepository.findByBoard_BoardIdAndUseYnTrue(boardId, pageable);
  }

}
