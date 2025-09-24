package com.goodee.corpdesk.Chat;

/*
  Testing library and framework: JUnit 5 (Jupiter) with Mockito for mocking.
  These tests validate ChatService's public methods with happy paths, edge cases, and failure propagation.
*/

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    @DisplayName("getChatRoomList returns repository results for valid username")
    void returnsListForValidUsername() {
        String username = "alice";
        List<ChatRoom> expected = Arrays.asList(mock(ChatRoom.class), mock(ChatRoom.class));
        when(chatRoomRepository.findAllByUsername(username)).thenReturn(expected);

        List<ChatRoom> result = chatService.getChatRoomList(username);

        assertSame(expected, result, "Should return the exact list from repository");
        verify(chatRoomRepository, times(1)).findAllByUsername(username);
        verifyNoMoreInteractions(chatRoomRepository);
    }

    @Test
    @DisplayName("getChatRoomList returns empty list when repository returns empty")
    void returnsEmptyList() {
        String username = "nobody";
        List<ChatRoom> expected = Collections.emptyList();
        when(chatRoomRepository.findAllByUsername(username)).thenReturn(expected);

        List<ChatRoom> result = chatService.getChatRoomList(username);

        assertTrue(result.isEmpty(), "Expected an empty list");
        assertSame(expected, result, "Should pass through the same list instance");
        verify(chatRoomRepository).findAllByUsername(username);
        verifyNoMoreInteractions(chatRoomRepository);
    }

    @Test
    @DisplayName("getChatRoomList propagates exceptions from repository")
    void propagatesExceptions() {
        String username = "error";
        RuntimeException ex = new RuntimeException("db down");
        when(chatRoomRepository.findAllByUsername(username)).thenThrow(ex);

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> chatService.getChatRoomList(username));

        assertSame(ex, thrown, "Should propagate repository exception instance");
        verify(chatRoomRepository).findAllByUsername(username);
        verifyNoMoreInteractions(chatRoomRepository);
    }

    @Test
    @DisplayName("getChatRoomList works with null username (delegates to repository)")
    void worksWithNullUsername() {
        when(chatRoomRepository.findAllByUsername(null)).thenReturn(Collections.emptyList());

        List<ChatRoom> result = chatService.getChatRoomList(null);

        assertNotNull(result, "Should not return null");
        assertTrue(result.isEmpty(), "Expected empty list for null username");
        verify(chatRoomRepository).findAllByUsername(null);
        verifyNoMoreInteractions(chatRoomRepository);
    }

    @Test
    @DisplayName("getChatRoomList passes through whitespace username")
    void passesThroughWhitespaceUsername() {
        String username = "   ";
        List<ChatRoom> expected = Collections.singletonList(mock(ChatRoom.class));
        when(chatRoomRepository.findAllByUsername(username)).thenReturn(expected);

        List<ChatRoom> result = chatService.getChatRoomList(username);

        assertSame(expected, result, "Should return repository-provided list");
        verify(chatRoomRepository).findAllByUsername(username);
        verifyNoMoreInteractions(chatRoomRepository);
    }

    @Test
    @DisplayName("getChatRoom is a no-op and should not throw or call repository")
    void getChatRoomDoesNotThrow() {
        assertDoesNotThrow(() -> chatService.getChatRoom());
        verifyNoInteractions(chatRoomRepository);
    }
}