<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chat Page</title>
<c:import url="/WEB-INF/views/include/head.jsp" />
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
<style>
 .messageContainer{
	height:75vh;

	overflow-y:scroll; 
	} 
html, body {
	overflow: hidden; /* 내부 스크롤도 완전히 제거 */
	height: 100%;
}
.profileImg{
width:50px;
height:50px;
}
.media-chat:not(.media-chat-right) .text-content {
  border-top-left-radius: 0 !important;
  border-bottom-left-radius: 25px !important;
}


</style>
</head>
<body>
	<sec:authentication property="principal.username" var="user" />

	<input type="hidden" class="username" value="${user}">
	<input type="hidden" class="roomId" value="${roomId}">


	<!-- <div class = "messageContainer">
		<div id="chat-list">
			
			
		</div>
	</div> -->

	<div class="card-body pb-0 messageContainer" 
		>
		<div id="chat-list">
			

			
		</div>
	</div>
	<!-- Chat Footer -->
	<div class="chat-footer">
		<form id="chatForm">
			<div class="input-group input-group-chat">
				<textarea class="form-control messageInput" id="messageInput"
					style="height: 100px;" aria-label="Text input with dropdown button"></textarea>
				<div class="input-group-append">
					<button type="button" id="sendBtn" class="btn btn-primary">
						메시지 전송</button>
				</div>
			</div>
		</form>
	</div>

	<script type="text/javascript" src="/js/chat/chatPage.js"></script>
	<!-- 내용 끝 -->


	<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp" />
</html>