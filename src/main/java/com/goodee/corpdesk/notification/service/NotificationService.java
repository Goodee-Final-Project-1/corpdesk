package com.goodee.corpdesk.notification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.notification.dto.NotificationDto;
import com.goodee.corpdesk.notification.entity.Notification;
import com.goodee.corpdesk.notification.repository.NotificationRepository;

@Service
public class NotificationService {
	@Autowired
	NotificationRepository notificationRepository;
	
	//읽지 않은 '메세지' 알림 조회
	public List<NotificationDto> getMessageNotification(String username) {
		List<Notification> notification =  notificationRepository.findByNotificationTypeAndUsernameAndIsReadFalseOrderByRelatedIdDesc("message",username);
		List<NotificationDto> notificationDto=new ArrayList<>();
		if(!notification.isEmpty()) {
			 notification.forEach(l->{
				 NotificationDto n= l.ChangeToDto();
				 notificationDto.add(n);
			 });
		}
		
		
		return notificationDto;
	}
	// 읽지 않은 '결재' 알림 조회
	public List<NotificationDto> getApprovalNotificationList(String username) {
		List<Notification> notification =  notificationRepository.findByNotificationTypeAndUsernameAndIsReadFalseOrderByRelatedIdDesc("approval",username);
		List<NotificationDto> notificationDto=new ArrayList<>();
		if(!notification.isEmpty()) {
			 notification.forEach(l->{
				 NotificationDto n= l.ChangeToDto();
				 notificationDto.add(n);
			 });
		}
		return notificationDto;
	}
	
	// 특정 알림 읽음 처리 및 정보
	public NotificationDto notificationSelect(Long relatedId ,String notificationType , String username) {
		Optional<Notification> notification =  notificationRepository.findByRelatedIdAndNotificationTypeAndUsername(relatedId,notificationType,username);
		if(notification.isPresent()) {
			notificationRepository.updateReadTrue(relatedId,notificationType,username);
			return  notification.get().ChangeToDto();
		}
		return null;
		}
	//특정 알림 유형의 모든 알림 읽음 처리
	public int setNotificationReadAll(String notificationType , String username) {
		int result= notificationRepository.updateAllReadTrue(notificationType,username);
		return result;
	
	}
	//채팅방 하나의 메세지 전부 읽음 처리
	public int setNotificationOneRoomReadAll(Long chatRoomId , String username) {
		int result= notificationRepository.updateOneRoomAllReadTrue(chatRoomId,username);
		return result;
	
	}
	//메세지 알림 저장
	public Notification saveNotification(Long relatedId ,String notificationType , String username) {
		Notification notification = Notification.builder()
				.notificationType(notificationType)
				.relatedId(relatedId)
				.username(username)
				.build();
		return notificationRepository.save(notification);
	}
	//결재 알림 저장
	public Notification saveApprovalNotification(Long relatedId ,String notificationType ,String content, String username) {
		Notification notification = Notification.builder()
				.notificationType(notificationType)
				.relatedId(relatedId)
				.username(username)
				.content(content)
				.build();
		return notificationRepository.save(notification);
	}

	
}
