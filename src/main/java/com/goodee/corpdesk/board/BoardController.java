package com.goodee.corpdesk.board;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

  @Autowired
  private BoardService boardService;

  // 부서게시판 목록 페이지 요청
  @GetMapping("/department/{departmentId}")
  public String departmentList(@PathVariable("departmentId") Integer departmentId,
                               @PageableDefault(size = 10, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable,
                               Model model) {
    // Service에서 부서게시판 게시글 목록 가져오기
    Page<Board> departmentPage = boardService.getDepartmentList(departmentId, pageable);
    model.addAttribute("departmentPage", departmentPage);
    model.addAttribute("departmentId", departmentId);
    return "board/departmentList";
  }
  
  // 부서게시판 상세 조회
  @GetMapping("/department/{departmentId}/{id}")
  public String departmentDetail(@PathVariable("departmentId") Integer departmentId,
                                 @PathVariable("id") Long boardId,
                                 Model model) {
    // Service에서 해당 글 가져오기
    Board board = boardService.getBoard(boardId);
    model.addAttribute("board", board);
    model.addAttribute("departmentId", departmentId);
    return "board/departmentDetail";
  }

  // 부서게시판 등록 폼 요청
  @GetMapping("/department/{departmentId}/new")
  public String departmentForm(@PathVariable("departmentId") Integer departmentId, Model model) {
    model.addAttribute("board", new Board());
    model.addAttribute("departmentId", departmentId);
    return "board/departmentForm";
  }

  // 부서게시판 등록 처리
  @PostMapping("/department/{departmentId}")
  public String createdepartment(@PathVariable("departmentId") Integer departmentId,
                                 @ModelAttribute Board board) {
    board.setDepartmentId(departmentId);
    boardService.createBoard(board);
    return "redirect:/board/department" + departmentId;
  }
  
  
  

}
