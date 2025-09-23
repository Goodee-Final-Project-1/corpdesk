package com.goodee.corpdesk.board;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/post")
public class PostController {

  @Autowired
  private PostService postService;

  // 게시글 생성 API
  @PostMapping
  public ResponseEntity<Void> createPost(@Valid @RequestBody PostCreateRequest request) {
    Long postId = postService.createPost(request);
    // 생성된 게시글의 URI를 Location 헤더에 담아 201 Created 상태 코드와 함께 반환
    return ResponseEntity.created(URI.create("/post/" + postId)).build();
  }

  // 게시글 상세 조회 API
  @GetMapping("/{postId}")
  public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable("postId") Long postId) {
    PostDetailResponse response = postService.getPostDetail(postId);
    return ResponseEntity.ok(response);
  }

  // 게시글 목록 조회
  @GetMapping("/list/{boardId}")
  public String postList(@PathVariable("boardId") Long boardId,
                         @PageableDefault(size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable,
                         Model model) {
    Page<Post> postPage = postService.getPostsByBoardId(boardId, pageable);
    model.addAttribute("postPage", postPage);
    model.addAttribute("boardId", boardId);
    return "post/list";
  }
  

}
