package com.goodee.corpdesk.chat.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodee.corpdesk.chat.dto.RoomData;
import com.goodee.corpdesk.chat.entity.ChatRoom;
import com.goodee.corpdesk.chat.service.ChatRoomService;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.position.entity.Position;

@Controller
@RequestMapping("/chat/room/**")
public class ChatRoomController {

	@Autowired
	ChatRoomService chatRoomService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	@GetMapping("list")
	public String chatRoomList(Principal principal ,Model model) {
			String username= principal.getName();
			List<RoomData> roomList= chatRoomService.getChatRoomList(username);
			List<Employee> employeeList = employeeService.getActiveEmployees();
			model.addAttribute("roomList", roomList);
			model.addAttribute("employeeList",employeeList);
		
		return "Chat/chat_list";
	}
	
	
	@GetMapping("detail/{roomId}")
	public String chatRoomDetail(@PathVariable(value = "roomId") Long roomId , Model model , Principal principal) throws JsonProcessingException {
		RoomData roomData = new RoomData();
		roomData = chatRoomService.chatRoomDetail(roomId,principal);
		
		//json으로 변환
		ObjectMapper mapper = new ObjectMapper();
		String JsonRoomData = mapper.writeValueAsString(roomData);
		List<Employee> employeeList = employeeService.getActiveEmployees();
		model.addAttribute("roomData",JsonRoomData);
		model.addAttribute("employeeList",employeeList);
		model.addAttribute("roomDataEl",roomData);
		return "Chat/chat_page";
	}
	
	@ResponseBody
	@PostMapping("createRoom")
	public RoomData createRoom(@RequestBody RoomData roomdata , Principal principal) {
		//채팅방 생성 
		Long roomId =chatRoomService.createRoom(roomdata, principal);
		RoomData chatRoom = new RoomData();
		chatRoom.setChatRoomId(roomId);

		return chatRoom;
	}
	@GetMapping("out/{roomId}")
	@ResponseBody
	public boolean chatRoomOut(@PathVariable(value = "roomId") Long roomId,Principal principal) {
		boolean result = chatRoomService.outRoom(roomId,principal);
		return result;
	}
	
	@PostMapping("inviteRoom")
	@ResponseBody
	public boolean inviteRoom(@RequestBody RoomData roomData) {
		boolean result = chatRoomService.inviteRoom(roomData);
		return result;
	}
}
