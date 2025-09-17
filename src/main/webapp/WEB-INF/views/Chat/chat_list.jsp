<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
			
				<table>
					<tr>
						<th>채팅방 번호</th>
						<th>채팅방 제목</th>
					</tr>
					<c:forEach items="${roomList}" var="room">
						<tr class="chatListOne" data-roomId="${room.chatRoomId }" style="cursor:pointer;">
								<td>${room.chatRoomId}</td>
								<td>${room.chatRoomTitle}</td>
						</tr>
					</c:forEach>
				</table>
				
				<br>
				<p>채팅방 생성</p>
				<p>username</p>
				<input type="text" id="user">
				<p>방 제목</p>
				<input type= "text" id="roomTitle">
				<button type= "button" id="createBtn">생성하기</button>
				
				
				
				
				<script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
  			    <script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
				<script type="text/javascript">
				const socket = new SockJS("/ws");
	            stompClient = Stomp.over(socket);
	            console.log("개인 메시지:");
	            
	            stompClient.connect({}, function(frame) {
	                console.log("소켓 연결됨:", frame);

	                // 기본적으로 본인 개인큐 구독 (알림/DM 수신)
	                stompClient.subscribe("/user/queue/notifications",(message)=>{
	                	
	                });
	               
	            });    
				
				const createBtn = document.getElementById("createBtn");
				
				createBtn.addEventListener("click",()=>{
					 const username = document.getElementById("user").value;
					 const roomTitle = document.getElementById("roomTitle").value;
					
					 const roomdata =  {
							 roomTitle: roomTitle,
							 username : username
							 
					 };
					 fetch("/chat/room/createRoom",{
			            	method: "POST",
			            	headers: {"Content-Type" :"application/json"},
			            	body : JSON.stringify(roomdata)
			            })
			            .then(res=>res.json())
			            .then(roomId=>{
			            	
			            	stompClient.subscribe("/sub/chat/room/"+roomId,(message)=>{
			            		
			            	})
			         
						
			            });
					 
				})
          
	            
				const chatListOne = document.querySelectorAll(".chatListOne").forEach(list =>{
					const roomId= list.getAttribute('data-roomId');
					list.addEventListener("click",()=>{
						window.open("/chat/room/detail/"+roomId);
					})
					
				});
				
				
			
				</script>
			
			
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>