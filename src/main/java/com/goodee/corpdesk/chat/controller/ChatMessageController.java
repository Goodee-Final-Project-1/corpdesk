package com.goodee.corpdesk.chat.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.goodee.corpdesk.chat.service.ChatRoomService;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import com.goodee.corpdesk.position.entity.Position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.goodee.corpdesk.attendance.controller.AttendanceController;
import com.goodee.corpdesk.chat.dto.ChatSessionTracker;
import com.goodee.corpdesk.chat.dto.FocusMessage;
import com.goodee.corpdesk.chat.dto.RoomData;
import com.goodee.corpdesk.chat.entity.ChatMessage;
import com.goodee.corpdesk.chat.entity.ChatParticipant;
import com.goodee.corpdesk.chat.entity.ChatRoom;
import com.goodee.corpdesk.chat.service.ChatMessageService;
import com.goodee.corpdesk.chat.service.ChatParticipantService;

@Controller
@RequestMapping("/chat/message/**")
public class ChatMessageController {



	@Autowired
	// Spring에서 서버가 클라이언트(STOMP 구독자)에게 메시지를 푸시하기 위해 제공하는 템플릿 객체
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatMessageService chatMessageService;
	@Autowired
	private ChatSessionTracker chatSessionTracker;

	@Autowired
	private ChatRoomService chatRoomService;
	
	@Autowired
	private EmployeeService employeeService;
	

	//유저이름으로 유저 이름 부서 직위 가져옴
	public String getUserNameDepPos(String username) {
		Map<String, Object> map = employeeService.detail(username);
        Employee emp = (Employee) map.get("employee");
        Department department = (Department) map.get("department");
        Position position = (Position) map.get("position");
        String userNameDepPos = department.getDepartmentName() + " " + position.getPositionName() + " " + emp.getName();
        return userNameDepPos;
		
	}
	public String getUserImgPath(String username) {
		 Optional <EmployeeFile> empFileOp= employeeService.getEmployeeFileByUsername(username);
	        if(empFileOp.isPresent()) {
	        	EmployeeFile empFile = empFileOp.get();
	        	 if(empFile!=null && empFile.getUseYn()) {
	 	        	return "/files/profile/"+empFile.getSaveName()+"."+empFile.getExtension();
	 	        }else {
	 	        	return "/images/default_profile.jpg";
	 	        }
	        }else {
	        	return "/images/default_profile.jpg";
	        }
	}
  
  
	// websocket 요청에 대한 매핑 위의 requestMapping과 관련없고 websocket config에서 지정해준 prefix 사용
	@MessageMapping("/chat/message")
	public void chatsendMessage(ChatMessage msg , Principal principal) {
	    //  방 타입 조회
	    String chatRoomType = chatRoomService.getChatRoomType(msg.getChatRoomId());

	    //  방 참가자 처리
	    List<ChatParticipant> participants;
	    RoomData chatRoom = new RoomData();
	  
        //참가자 활성화
	    if (chatRoomType.equals("direct")) {
	        // 1대1이면 나간 사람도 다시 참여 처리
	        participants = chatMessageService.participantOnetoOneByRoom(msg.getChatRoomId());

	        // 상대방 이름으로 방제목 세팅
	        chatRoom.setChatRoomTitle(getUserNameDepPos(principal.getName()));
	        chatRoom.setImgPath(getUserImgPath(principal.getName()));
	        

	    } else {
	        // 그룹 채팅은 나간 사람한테 안보냄
	    	if(chatMessageService.checkFirstMessage(msg.getChatRoomId())==0) {
	    		chatMessageService.RoomUserAllUseYnTrue(msg.getChatRoomId());
	    		
	    	}
	        participants = chatMessageService.participantListByRoom(msg.getChatRoomId());
	        chatRoom.setChatRoomTitle(chatRoomService.getRoomTitle(msg.getChatRoomId()));
	        chatRoom.setNotificationType("room");
	        chatRoom.setImgPath("/images/group_profile.png");
	    }
	    
	    //  메시지 저장
	  		ChatMessage saveMsg = chatMessageService.messageSave(msg);
	  	    saveMsg.setNotificationType("message");

	  	    
	  	    chatRoom.setChatRoomId(msg.getChatRoomId());
	        chatRoom.setChatRoomLastMessage(saveMsg.getMessageContent());
	        chatRoom.setLastMessageTime(saveMsg.getSentAt());
	        chatRoom.setNotificationType("room");
	        chatRoom.setUnreadCount(0L);
	    //  방 전체 브로드캐스트
	      
	        //메세지 전송자 이름, 이미지 
	        String viewName = getUserNameDepPos(principal.getName());
	        saveMsg.setImgPath(getUserImgPath(principal.getName()));
	        saveMsg.setViewName(viewName);
	        messagingTemplate.convertAndSend("/sub/chat/room/" + msg.getChatRoomId(), saveMsg);

	    //  개인 알림 전송
	    // 그룹 , 개인 전부 여기서 보내줌
	    for (ChatParticipant p : participants) {
	        String username = p.getEmployeeUsername();
	        Long chatRoomId = p.getChatRoomId();

	        if (!chatSessionTracker.isUserFocused(chatRoomId, username)) {
	        	saveMsg.setFocused(false);
	        } else {
	        	saveMsg.setFocused(true);
	        }
	        
	        //참가자들중에 내가 보낸메세지에 대한처리 ;나한테 보내는 알림으로는 상대방에대한 이름이나 사진이 전송되야함
	        if(chatRoomType.equals("direct") && username.equals(saveMsg.getEmployeeUsername())) {
	        	//기본은 내가보낸 메세지를 그대로 보내는데(방에 나만 있으면 이렇게 보내짐)
	        	List<ChatParticipant> list = chatMessageService.participantOnetoOneByRoom(chatRoomId);
	        	 RoomData chatRoomMe = new RoomData();
	        	 chatRoomMe.setChatRoomId(msg.getChatRoomId());
	        	 chatRoomMe.setChatRoomLastMessage(saveMsg.getMessageContent());
	        	 chatRoomMe.setLastMessageTime(saveMsg.getSentAt());
	        	 chatRoomMe.setNotificationType("room");
	        	 chatRoomMe.setUnreadCount(0L);
	        	 chatRoomMe.setChatRoomTitle(chatRoom.getChatRoomTitle());
	        	 chatRoomMe.setImgPath(chatRoom.getImgPath());
	        	 
	        	 //상대방이 있다면 나한테는 상대방에 대한 이름이랑 이미지가 채팅방 목록에 표시되야함
	        	 list.forEach(l->{
	        		if(!l.getEmployeeUsername().equals(username)) {
	        		        chatRoomMe.setChatRoomTitle(getUserNameDepPos(l.getEmployeeUsername()));
	        		        chatRoomMe.setImgPath((l.getEmployeeUsername()));
	        		}
	        		
	        	});
	        	
	        	
	        	messagingTemplate.convertAndSendToUser(username, "/queue/notifications", chatRoomMe);
	 	        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", saveMsg);
	        	
	        		continue;
	        }
	    
	        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", chatRoom);
	        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", saveMsg);
	    }
	    
	    
	    
	    
	}

	
	@MessageMapping("/chat/focus")
	 public void updateFocus(FocusMessage message, Principal principal, StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        chatSessionTracker.setFocus(sessionId, message.isFocused());
        
        //포커스되면 읽음을 목록 jsp에도 보내줘야함
       if(message.isFocused()) {
    	  message.setNotificationType("read");
       	messagingTemplate.convertAndSendToUser(principal.getName(),"/queue/notifications",message);
       }
	}
	
	@GetMapping("list/{chatRoomId}/{messageNo}/{size}")
	@ResponseBody
	public Map<String , Object> chatMessageList(@PathVariable(value = "chatRoomId")Long chatRoomId , 
			@PathVariable(value = "messageNo")Long lastMessageNo, @PathVariable(value = "size")int size , Principal principal){
		
		List<ChatMessage> list = chatMessageService.chatMessageList(chatRoomId,lastMessageNo,size,principal);
		Map<String, Object> map = new HashMap<>();
		map.put("size", list.size());
		map.put("messages",list);
		
		return map;
		
	}
	
	
	
	
}
