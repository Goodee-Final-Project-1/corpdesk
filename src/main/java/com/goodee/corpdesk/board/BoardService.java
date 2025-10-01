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

  // 현재 직원의 부서번호를 가져오기
  private Integer getCurrentUserDepartmentIdOrThrow() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
      throw new IllegalStateException("로그인이 필요합니다.");
    }
    Object principal = authentication.getPrincipal();

    // principal에 Employee가 직접 들어있는 경우
    if (principal instanceof com.goodee.corpdesk.employee.Employee emp) {
      Integer deptId = emp.getDepartmentId();
      if (deptId != null && deptId >= 0) return deptId;
      throw new IllegalStateException("유효한 부서가 없습니다.");
    }

    // 커스텀 UserDetails를 사용하는 경우에 맞게 추가 분기 가능
    throw new IllegalStateException("인증 주체에서 부서 정보를 찾을 수 없습니다.");
  }

  // 관리자인지 확인
  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) return false;
    for (GrantedAuthority auth : authentication.getAuthorities()) {
      String role = auth.getAuthority();
      if ("ROLE_ADMIN".equals(role) || "ROLE_MANAGER".equals(role)) return true;
    }
    return false;
  }

  // 게시글 작성
  public Board createPost(Board board) {
    if (board.getDepartmentId() == null) {
      board.setDepartmentId(getCurrentUserDepartmentIdOrThrow());
    }
    
    // 공지 작성 제한 (필요 시)
    if (Integer.valueOf(0).equals(board.getDepartmentId()) && !isAdmin()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "공지 작성 권한이 없습니다.");
    }

    board.setUseYn(true);
    board.setCreatedAt(LocalDateTime.now());
    board.setUpdatedAt(LocalDateTime.now());
    
    return boardRepository.save(board);
  }

  // 부서게시판 불러오기 (Authentication에서 부서ID 사용)
  public Page<Board> getMyDepartmentBoards(Pageable pageable) {
    Integer departmentId = getCurrentUserDepartmentIdOrThrow();
    return boardRepository.findByDepartmentIdAndUseYnTrue(departmentId, pageable);
  }

  // 공지게시판 불러오기
  public Page<Board> getNoticeBoards(Pageable pageable) {
    // departmentId = 0
    return boardRepository.findNoticePosts(pageable);
  }

  // 게시글 상세 조회
  public Board getBoardsDetail(Long boardId) {
    return boardRepository.findByBoardIdAndUseYnTrue(boardId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다"));
  }

}
