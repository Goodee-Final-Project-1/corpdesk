package com.goodee.corpdesk.position.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goodee.corpdesk.position.dto.IdsDTO;
import com.goodee.corpdesk.position.dto.PositionDTO;
import com.goodee.corpdesk.position.service.PositionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/position")
@RequiredArgsConstructor
public class PositionController {

	
	
	private final PositionService positionService;

    @GetMapping("/list")
    public String list(@RequestParam(name = "q", required = false) String q, Model model){
        model.addAttribute("positionList", positionService.getAllWithEmployeeCount(q));
        model.addAttribute("allPositions", positionService.getAllActive());
        return "position/list";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> add(@Valid @RequestBody PositionDTO req){
    	try {
        positionService.create(req.getPositionName(),req.getParentPositionId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (IllegalArgumentException | IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
   }
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> delete(@RequestBody IdsDTO ids){
    	        if (ids == null || ids.getIds() == null || ids.getIds().isEmpty()) {
    	            return ResponseEntity.badRequest().body("삭제할 ID가 비어있습니다.");
    	        }
    	        try {
    	            if (ids.getIds().size() == 1) {
    	                positionService.deleteOneAndReparent(ids.getIds().get(0));
    	            } else {
    	                positionService.deleteAllAndReparent(ids.getIds());
    	            }
    	            return ResponseEntity.ok().build();
    	        } catch (IllegalArgumentException | IllegalStateException e) {
    	            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    	        }
    	    }
}
