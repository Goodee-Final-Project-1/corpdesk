package com.goodee.corpdesk.notification.entity;

import com.goodee.corpdesk.common.BaseEntity;
import com.goodee.corpdesk.notification.dto.NotificationDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class Notification extends BaseEntity{
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long notificationId;
	 
	  @Column(nullable = false)
	    private String username; 
	    @Column(nullable = false)
	    private String notificationType; // MESSAGE, APPROVAL 등
	    @Column(nullable = false)
	    private Long relatedId; // ChatMessageId or ApprovalId
	    @Builder.Default()
	    @Column(nullable = false)
	    private Boolean isRead = false;
	    
	 public NotificationDto changeToDto() {
		 return NotificationDto.builder()
		 			 .notificationId(notificationId)
		 			 .notificationType(notificationType)
		 			 .isRead(isRead)
		 			 .username(username)
		 			 .relatedId(relatedId)
		 			 .build();
		 
	 }
	    
}
