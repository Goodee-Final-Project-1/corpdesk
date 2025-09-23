package com.goodee.corpdesk.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

  @Autowired
  private BoardService boardService;

  // 게시판 목록 페이지
  @GetMapping("/list")
  public String boardList(Model model) {
    List<Board> board = boardService.findAllBoards();
    model.addAttribute("boards", board);
    return "board/list";
  }

  // 게시판 생성 페이지
  @GetMapping("/new")
  public String newBoardForm() {
      return "board/new";
  }

  // 게시판 생성 메소드
  @PostMapping
  public String createBoard(@RequestParam("boardName") String boardName) {      
    boardService.createBoard(boardName);
    return "redirect:/board/list";
  }  

}
