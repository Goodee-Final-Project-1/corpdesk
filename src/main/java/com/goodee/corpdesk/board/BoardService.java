package com.goodee.corpdesk.board;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BoardService {

  @Autowired
  private BoardRepository boardRepository;

  // 현재 직원의 부서번호 가져오기
  private Integer getCurrentUserDepartmentIdOrThrow() {
    org.springframework.security.core.Authentication auth =
        org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
    Object principal = (auth != null ? auth.getPrincipal() : null);
    if (principal instanceof com.goodee.corpdesk.employee.Employee emp) {
      return emp.getDepartmentId();
    }
    throw new IllegalStateException("부서 정보를 찾을 수 없습니다.");
  }

  // 현재 직원의 이름 가져오기
  private String getCurrentUsername() {
    org.springframework.security.core.Authentication auth =
        org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()
        || auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
      throw new IllegalStateException("로그인이 필요합니다.");
    }
    return auth.getName();
  }

  // 관리자인지 확인
  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()
        || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
      return false;
    }
    Object principal = authentication.getPrincipal();
    if (principal instanceof com.goodee.corpdesk.employee.Employee) {
      com.goodee.corpdesk.employee.Employee emp = (com.goodee.corpdesk.employee.Employee) principal;
      Integer roleId = emp.getRoleId(); // 1=ADMIN, 2=MANAGER 가정
      return roleId != null && (roleId == 1 || roleId == 2);
    }
    return false;
  }

  // 게시글 작성
  public Board createPost(Board board) {
    if (board.getUsername() == null || board.getUsername().isBlank()) {
      board.setUsername(getCurrentUsername());
    }
    if (board.getDepartmentId() == null) {
      board.setDepartmentId(getCurrentUserDepartmentIdOrThrow());
    }

    // 공지(0) 작성 권한 체크
    if (Integer.valueOf(0).equals(board.getDepartmentId()) && !isAdmin()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "공지 작성 권한이 없습니다.");
    }

    board.setUseYn(true);
    board.setCreatedAt(java.time.LocalDateTime.now());
    board.setUpdatedAt(java.time.LocalDateTime.now());
    return boardRepository.save(board);
  }

  // 부서게시판 불러오기 (Authentication에서 부서ID 사용)
  public Page<Board> getMyDepartmentBoards(Pageable pageable) {
    Integer departmentId = getCurrentUserDepartmentIdOrThrow();
    return boardRepository.findByDepartmentIdAndUseYnTrue(departmentId, pageable);
  }

  // 공지게시판 불러오기
  public Page<Board> getNoticeBoards(Pageable pageable) {
    return boardRepository.findByDepartmentIdAndUseYnTrue(0, pageable);
  }

  // 게시글 상세 조회
  public Board getBoardsDetail(Long boardId) {
    return boardRepository.findByBoardIdAndUseYnTrue(boardId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다"));
  }

  // 게시글 수정 (본인글만 가능)
  public Board updatePost(Long boardId, Board request) {
    Board existing = boardRepository.findByBoardIdAndUseYnTrue(boardId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다"));

    String current = getCurrentUsername();
    if (existing.getUsername() == null || !existing.getUsername().equals(current)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
    }

    // 수정 가능 필드만 반영
    if (request.getTitle() != null) existing.setTitle(request.getTitle());
    if (request.getContent() != null) existing.setContent(request.getContent());
    existing.setUpdatedAt(LocalDateTime.now());

    // 공지/부서 전환 금지: departmentId는 변경하지 않음
    return boardRepository.save(existing);
  }

}
