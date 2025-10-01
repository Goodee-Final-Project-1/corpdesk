package com.goodee.corpdesk.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

  @Autowired
  private BoardService boardService;

  // 내 부서 게시글
  @GetMapping("/me")
  public String myDepartmentList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                 Model model) {
    Page<Board> page = boardService.getMyDepartmentBoards(pageable);
    
    model.addAttribute("page", page);
    model.addAttribute("title", "내 부서 게시판");
    model.addAttribute("post", page.getContent());
    
    return "board/departmentList";
  }

  // 공지 게시글 (departmentId = 0)
  @GetMapping("/notice")
  public String noticeList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                           Model model) {
    Page<Board> page = boardService.getNoticeBoards(pageable);
    
    model.addAttribute("page", page);
    model.addAttribute("departmentId", 0);
    model.addAttribute("title", "공지사항");
    model.addAttribute("post", page.getContent());
    
    return "board/departmentList";
  }



}
