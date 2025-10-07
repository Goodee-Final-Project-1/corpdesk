package com.goodee.corpdesk.chat.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodee.corpdesk.chat.dto.ChatMessageDto;
import com.goodee.corpdesk.chat.dto.ChatSessionTracker;
import com.goodee.corpdesk.chat.dto.RoomData;
import com.goodee.corpdesk.chat.entity.ChatMessage;
import com.goodee.corpdesk.chat.entity.ChatParticipant;
import com.goodee.corpdesk.chat.repository.ChatMessageRepository;
import com.goodee.corpdesk.chat.repository.ChatParticipantRepository;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import com.goodee.corpdesk.position.entity.Position;

@Service
public class ChatMessageService {

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private ChatParticipantRepository chatParticipantRepository;
	@Autowired 
	private ChatRoomService chatRoomService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private ChatSessionTracker chatSessionTracker;

	// 유저이름으로 유저 이름 부서 직위 가져옴
	public String getUserNameDepPos(String username) {
		Map<String, Object> map = employeeService.detail(username);
		Employee emp = (Employee) map.get("employee");
		Department department = (Department) map.get("department");
		Position position = (Position) map.get("position");
		String userNameDepPos = department.getDepartmentName() + " " + position.getPositionName() + " " + emp.getName();
		return userNameDepPos;

	}

	public String getUserImgPath(String username) {
		Optional<EmployeeFile> empFileOp = employeeService.getEmployeeFileByUsername(username);
		if (empFileOp.isPresent()) {
			EmployeeFile empFile = empFileOp.get();
			if (empFile != null && empFile.getUseYn()) {
				return "/files/profile/" + empFile.getSaveName() + "." + empFile.getExtension();
			} else {
				return "/images/default_profile.jpg";
			}
		} else {
			return "/images/default_profile.jpg";
		}
	}

	public ChatMessage messageSave(ChatMessage msg) {
		msg.setUseYn(true);
		return chatMessageRepository.save(msg);

	}

	public List<ChatParticipant> participantListByRoom(Long RoomId) {
		return chatParticipantRepository.findAllByChatRoomIdAndUseYnTrue(RoomId);
	}

	public List<ChatParticipant> participantOnetoOneByRoom(Long RoomId) {
		List<ChatParticipant> list = chatParticipantRepository.findAllByChatRoomId(RoomId);

		list.forEach(l -> {
			if (!l.getUseYn()) {
				chatParticipantRepository.updateRoomUseYnTrue(l.getEmployeeUsername(), RoomId);
			}
		});
		return list;
	}

	// 방번호로 해당 방의 메세지를 조회해옴
	public List<ChatMessage> chatMessageList(Long chatRoomId, Long lastMessageNo, int size, Principal principal) {
		Pageable pageable = PageRequest.of(0, size);

		// 방에 초대 되었을 시점을 기준까지 가져옴 (방을 나갔다 다시 들어온 경우 이전 내용을 볼 수 없음)
		LocalDateTime enterTime = chatParticipantRepository
				.findByChatRoomIdAndEmployeeUsername(chatRoomId, principal.getName()).getUpdatedAt();
		List<ChatMessage> list = null;
		if (lastMessageNo == 0 || lastMessageNo == null) {
			list = chatMessageRepository.findByChatRoomIdAndSentAtGreaterThanEqualOrderByMessageIdDesc(chatRoomId,
					enterTime, pageable);
			list.forEach(l -> {
				if(l.getMessageType()==null) {
					l.setViewName(getUserNameDepPos(l.getEmployeeUsername()));
					l.setImgPath(getUserImgPath(l.getEmployeeUsername()));
				}
			});

		} else {
			list = chatMessageRepository
					.findByChatRoomIdAndMessageIdLessThanAndSentAtGreaterThanEqualOrderByMessageIdDesc(chatRoomId,
							lastMessageNo, enterTime, pageable);
			list.forEach(l -> {
				if(l.getMessageType()==null) {
					l.setViewName(getUserNameDepPos(l.getEmployeeUsername()));
					l.setImgPath(getUserImgPath(l.getEmployeeUsername()));
				}

			});
		}
		return list;
	}

	public Long checkFirstMessage(Long chatRoomId) {
		return chatMessageRepository.countByChatRoomId(chatRoomId);

	}

	public void RoomUserAllUseYnTrue(Long chatRoomId) {
		chatParticipantRepository.updateAllRoomUseYnTrue(chatRoomId);

	}

	
	//메세지 처리 구독
	public void processMessage(ChatMessage msg, Principal principal) {
		  //  방 타입 조회
	    String chatRoomType = chatRoomService.getChatRoomType(msg.getChatRoomId());

	    //  방 참가자 처리
	    List<ChatParticipant> participants;
	    RoomData chatRoom = new RoomData();
	  
        //참가자 활성화
	    if (chatRoomType.equals("direct")) {
	        // 1대1이면 나간 사람도 다시 참여 처리
	        participants = participantOnetoOneByRoom(msg.getChatRoomId());

	        // 상대방 이름으로 방제목 세팅
	        chatRoom.setChatRoomTitle(getUserNameDepPos(principal.getName()));
	        chatRoom.setImgPath(getUserImgPath(principal.getName()));
	        

	    } else {
	        // 그룹 채팅은 나간 사람한테 안보냄
	    	if(checkFirstMessage(msg.getChatRoomId())==0) {
	    		RoomUserAllUseYnTrue(msg.getChatRoomId());
	    		
	    	}
	        participants = participantListByRoom(msg.getChatRoomId());
	        chatRoom.setChatRoomTitle(chatRoomService.getRoomTitle(msg.getChatRoomId()));
	        chatRoom.setNotificationType("room");
	        chatRoom.setImgPath("/images/group_profile.png");
	    }
	    
	    //  메시지 저장
	  		ChatMessageDto saveMsg = messageSave(msg).toChatMessageDto();
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
	        	List<ChatParticipant> list = participantOnetoOneByRoom(chatRoomId);
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
	        		        chatRoomMe.setImgPath( getUserImgPath(l.getEmployeeUsername()));
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

}
