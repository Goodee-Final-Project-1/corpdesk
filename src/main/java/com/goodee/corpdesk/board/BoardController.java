package com.goodee.corpdesk.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/board")
public class BoardController {

  @Autowired
  private BoardService boardService;

  // 전체 부서 게시판 목록
  @GetMapping("/department/list")
  public String departmentBoardList(Model model) {
    List<Board> department = boardService.getAllDepartmentBoards();
    model.addAttribute("department", department);
    return "board/departmentList";
  }

  // 특정 부서 게시판 상세
  @GetMapping("/department/{departmentId}")
  public String departmentBoard(@PathVariable("departmentId") Long departmentId, Model model) {
    List<Board> department = boardService.getDepartmentBoard(departmentId);
    model.addAttribute("department", department);
    return "board/departmentDetail";
  }

  // 공용 게시판 목록
  @GetMapping("/common/list")
  public String commonBoardList(Model model) {
    List<Board> commonBoard = boardService.getCommonBoard();
    model.addAttribute("commonBoard", commonBoard);
    return "board/commonList";
  }


}
