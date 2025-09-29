package com.goodee.corpdesk.chat.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.chat.entity.ChatMessage;
import com.goodee.corpdesk.chat.entity.ChatParticipant;
import com.goodee.corpdesk.chat.repository.ChatMessageRepository;
import com.goodee.corpdesk.chat.repository.ChatParticipantRepository;

@Service
public class ChatMessageService {

	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
	@Autowired
	private ChatParticipantRepository chatParticipantRepository;
	
	public ChatMessage messageSave(ChatMessage msg) {
		msg.setUseYn(true);
		return chatMessageRepository.save(msg);
		
	}
	
	public List<ChatParticipant> participantListByRoom(Long RoomId){
		return chatParticipantRepository.findAllByChatRoomIdAndUseYnTrue(RoomId);
	}
	public List<ChatParticipant> participantOnetoOneByRoom(Long RoomId){
		List<ChatParticipant> list = chatParticipantRepository.findAllByChatRoomId(RoomId);
		
		list.forEach(l->{
			if(!l.getUseYn()) {
				chatParticipantRepository.updateRoomUseYnTrue(l.getEmployeeUsername(), RoomId);
			}
		});
		return list;
	}

	//방번호로 해당 방의 메세지를 조회해옴
	public List<ChatMessage> chatMessageList(Long chatRoomId, Long lastMessageNo, int size , Principal principal) {
		Pageable pageable = PageRequest.of(0, size);
		
		//방에 초대 되었을 시점을 기준까지 가져옴 (방을 나갔다 다시 들어온 경우 이전 내용을 볼 수 없음)
		LocalDateTime enterTime = chatParticipantRepository.findByChatRoomIdAndEmployeeUsername(chatRoomId, principal.getName()).getUpdatedAt();
		
		if(lastMessageNo==0|| lastMessageNo==null) {
			return chatMessageRepository.findByChatRoomIdAndSentAtGreaterThanEqualOrderByMessageIdDesc(chatRoomId,enterTime , pageable);
		}else {
			return chatMessageRepository.findByChatRoomIdAndMessageIdLessThanAndSentAtGreaterThanEqualOrderByMessageIdDesc(chatRoomId,lastMessageNo, enterTime, pageable);
		}
	
	}

	public Long checkFirstMessage(Long chatRoomId) {
			return chatMessageRepository.countByChatRoomId(chatRoomId);
		
	}

	public void RoomUserAllUseYnTrue(Long chatRoomId) {
		chatParticipantRepository.updateAllRoomUseYnTrue(chatRoomId);
		
	}

}
