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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


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
    model.addAttribute("title", "부서 게시판");
    model.addAttribute("post", page.getContent());
    
    return "board/boardList";
  }

  // 공지 게시글 (departmentId = 0)
  @GetMapping("/notice")
  public String noticeList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                           Model model) {
    Page<Board> page = boardService.getNoticeBoards(pageable);
    
    model.addAttribute("page", page);
    model.addAttribute("departmentId", 0);
    model.addAttribute("title", "공지 게시판");
    model.addAttribute("post", page.getContent());
    
    return "board/boardList";
  }

  // 공지 게시글 상세 페이지 (useYn = true)
  @GetMapping("/detail/{boardId}")
  public String noticeDetail(@PathVariable("boardId") Long boardId, Model model) {
    Board post = boardService.getBoardsDetail(boardId);

    model.addAttribute("post", post);
    model.addAttribute("title", "공지 상세");

    return "board/noticeDetail";
  }
  

}
