<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri= "http://www.springframework.org/security/tags"%> 

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Chat Page</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
	<script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			
			<h2>그룹 채팅</h2>
			
	<p>채팅 상대입력</p><input type="text" id="otherPerson">

    <button onclick="connect()">채팅 연결</button>
    <input type="text" id="messageInput">
    <button onclick="sendMessage()">메시지 전송</button>
    <table></table>

    <div id="messages"></div>

    <script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
    <script>
        let stompClient = null;

        function connect(){
            // 쿠키는 자동으로 포함됨
            // 소켓이라는 통로를 이용하는 Stomp 객체 
            const socket = new SockJS("/ws");
            stompClient = Stomp.over(socket);

            stompClient.connect({}, frame => {
                console.log("연결됨: " + frame);

                // 예시: 채팅방 1번 구독
                stompClient.subscribe("/sub/chat/room/1", msg => {
                    const message = JSON.parse(msg.body);
                    document.getElementById("messages").innerHTML += "<p>" + message.sender + ": " + message.messageContent + "</p>";
                });
            });
        }

        function sendMessage(){
            const content = document.getElementById("messageInput").value;
            stompClient.send("/pub/chat/message", {}, JSON.stringify({
                chatRoomId: 1,
                messageContent: content
            }));
        }
        stompClient.subscribe("/sub/chat/room/1", msg => {
            const body = JSON.parse(msg.body);
            console.log("받은 메시지:", body);
        });
    </script>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>