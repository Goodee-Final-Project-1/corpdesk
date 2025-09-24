package com.goodee.corpdesk.Chat;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

/**
 * Testing library/framework: JUnit 5 (Jupiter) with Mockito for mocking.
 * These tests focus on ChatController behavior, covering happy paths and edge cases.
 */
class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Utility to build a Principal with a fixed name
    private static Principal principalWithName(String name) {
        return new Principal() {
            @Override
            public String getName() {
                return name;
            }
        };
    }

    @Nested
    @DisplayName("chatMessage(ChatMessage, Principal)")
    class ChatMessageTests {

        @Test
        @DisplayName("Publishes message to room topic and sets employeeUsername from Principal (happy path)")
        void chatMessage_publishesWithEmployeeUsername() {
            // Given
            ChatMessage msg = new ChatMessage();
            // Setup a room id and content to ensure they flow through
            Long roomId = 42L;
            msg.setChatRoomId(roomId);
            msg.setMessageContent("Hello world");
            Principal principal = principalWithName("alice");

            // When
            controller.chatMessage(msg, principal);

            // Then
            // employeeUsername should be set
            // Note: ChatMessage provides setEmployeeUsername in controller usage; assert effect here.
            // We avoid assert libraries to keep compatibility; basic assertions suffice.
            org.junit.jupiter.api.Assertions.assertEquals("alice", msg.getEmployeeUsername());

            // Destination should be based on chatRoomId
            String expectedDestination = "/sub/chat/room/" + roomId;
            verify(messagingTemplate, times(1)).convertAndSend(eq(expectedDestination), same(msg));

            // No unexpected interactions
            verifyNoMoreInteractions(messagingTemplate, chatService);
        }

        @Test
        @DisplayName("Handles null chatRoomId by still publishing to '.../null' destination")
        void chatMessage_nullRoomId_publishesToNullPath() {
            // Given
            ChatMessage msg = new ChatMessage();
            msg.setChatRoomId(null);
            msg.setMessageContent("No room");
            Principal principal = principalWithName("bob");

            // When
            controller.chatMessage(msg, principal);

            // Then
            org.junit.jupiter.api.Assertions.assertEquals("bob", msg.getEmployeeUsername());
            verify(messagingTemplate).convertAndSend(eq("/sub/chat/room/null"), same(msg));
            verifyNoMoreInteractions(messagingTemplate, chatService);
        }

        @Test
        @DisplayName("Throws NullPointerException when Principal is null")
        void chatMessage_nullPrincipal_throwsNPE() {
            // Given
            ChatMessage msg = new ChatMessage();
            msg.setChatRoomId(1L);
            msg.setMessageContent("test");

            // When/Then
            org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () ->
                controller.chatMessage(msg, null)
            );

            verifyNoInteractions(messagingTemplate, chatService);
        }
    }

    @Nested
    @DisplayName("chatForm(Long)")
    class ChatFormTests {

        @Test
        @DisplayName("Returns Chat/chat_page view name")
        void chatForm_returnsView() {
            String view = controller.chatForm(99L);
            org.junit.jupiter.api.Assertions.assertEquals("Chat/chat_page", view);
            verifyNoInteractions(messagingTemplate, chatService);
        }
    }

    @Nested
    @DisplayName("chatList(Principal, Model)")
    class ChatListTests {

        @Test
        @DisplayName("Populates model with roomList and returns Chat/chat_list (happy path with rooms)")
        void chatList_populatesModel_withRooms() {
            // Given
            Principal principal = principalWithName("charlie");
            List<ChatRoom> rooms = Arrays.asList(new ChatRoom(), new ChatRoom());
            when(chatService.getChatRoomList("charlie")).thenReturn(rooms);
            Model model = new ConcurrentModel();

            // When
            String view = controller.chatList(principal, model);

            // Then
            org.junit.jupiter.api.Assertions.assertEquals("Chat/chat_list", view);
            org.junit.jupiter.api.Assertions.assertTrue(model.containsAttribute("roomList"));
            Object actual = model.getAttribute("roomList");
            org.junit.jupiter.api.Assertions.assertSame(rooms, actual);
            verify(chatService, times(1)).getChatRoomList("charlie");
            verifyNoMoreInteractions(chatService);
            verifyNoInteractions(messagingTemplate);
        }

        @Test
        @DisplayName("Handles empty room list gracefully and still returns view")
        void chatList_emptyList() {
            // Given
            Principal principal = principalWithName("diana");
            when(chatService.getChatRoomList("diana")).thenReturn(Collections.emptyList());
            Model model = new ConcurrentModel();

            // When
            String view = controller.chatList(principal, model);

            // Then
            org.junit.jupiter.api.Assertions.assertEquals("Chat/chat_list", view);
            org.junit.jupiter.api.Assertions.assertTrue(model.containsAttribute("roomList"));
            Object listAttr = model.getAttribute("roomList");
            org.junit.jupiter.api.Assertions.assertTrue(listAttr instanceof List);
            org.junit.jupiter.api.Assertions.assertTrue(((List<?>) listAttr).isEmpty());
            verify(chatService).getChatRoomList("diana");
            verifyNoMoreInteractions(chatService);
            verifyNoInteractions(messagingTemplate);
        }

        @Test
        @DisplayName("Throws NullPointerException when Principal is null")
        void chatList_nullPrincipal_throwsNPE() {
            Model model = new ConcurrentModel();
            org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () ->
                controller.chatList(null, model)
            );
            verifyNoInteractions(chatService, messagingTemplate);
        }
    }
}