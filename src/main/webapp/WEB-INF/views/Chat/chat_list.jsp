<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>채팅 목록</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<sec:authentication property="principal.username" var="user" />
			
		
			<p>연락처 목록 </p>
			<table class= "contact">
					<tr>
						<th>부서</th>&nbsp;
						<th>직위</th>&nbsp;
						<th>이름</th>
					</tr>
					<c:forEach items="${employeeList}" var="employee">
						<tr class="employeeListOne" data-employee="${employee.username }" style="cursor:pointer;">
								<td>${employee.departmentName } &nbsp;</td>
								
								<td>${employee.positionName} &nbsp;</td>
								
								<td>${employee.name }</td>
						</tr>
						
					</c:forEach>
				</table>
				<br>
			<br><br><br>
			
				<table class = "chatList">
					<tr>
						<th>채팅방 번호</th>
						<th>채팅방 제목</th>
						<th>읽음</th>
					</tr>
					<c:forEach items="${roomList}" var="room">
						<tr class="chatListOne" data-roomId="${room.chatRoomId }" data-unreadCount="${room.unreadCount }" style="cursor:pointer;">
								<td>${room.chatRoomId}</td>
								<td>${room.chatRoomTitle}</td>
								<td class="unreadCount">&nbsp; <c:if test="${room.unreadCount ne 0}">${room.unreadCount }</c:if></td>
						</tr>
					</c:forEach>
				</table>
				
				<br>
				<button type="button" id="createRoomBtn">채팅방 생성하기</button>
				
				
				
				<!-- 사원 목록 모달 창 -->
				<div id="createRoomModal" class="modal" style="display: none;">
					<div class="modal-content">
						<span class="close">&times;</span>
						<h2>채팅방 생성</h2>
				
						<label>방 제목</label> <input type="text" id="roomTitle" /> <label>참여자
							선택</label>
						<div id="participantList">
							<!-- 사원 목록을 체크박스로 뿌려줌 -->
							<label><input type="checkbox" value="wjdrlfgns2"> wjdrlfgns2</label><br />
							<label><input type="checkbox" value="wjdrlfgns1"> wjdrlfgns1</label><br />
						</div>
				
						<button id="createRoomConfirmBtn">생성</button>
					</div>
				</div>
				
				<script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
  			    <script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
				<script src="/js/chat/chatList.js" ></script>
				
				
			
			
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>