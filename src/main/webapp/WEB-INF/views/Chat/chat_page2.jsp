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
	<style>
	.messageContainer{
	height:400px;
	width:90%;
	overflow-y:scroll; 
	}
	
	</style>
</head>


<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			
		<sec:authentication property="principal.username" var="user"/>

	<input type="hidden" class="username" value="${user}">
	<input type="hidden" class="roomId" value="${roomId}">
	
	
	<div class = "messageContainer">
		<div id="chat-list">
			
			
		</div>
	</div>
	
	
	
	
	
			
    <input type="text" id="messageInput">
    <button type= "button" id="sendBtn">메시지 전송</button>
	
    
    	<script type="text/javascript" src="/js/chat/chatPage.js"></script>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>