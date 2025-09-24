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
				<p>채팅방 생성</p>
				<p>username</p>
				<input type= "text" id="user">
				<button type= "button" id="createBtn">생성하기</button>
				
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
				<script type="text/javascript">
				const socket = new SockJS("/ws");
	            stompClient = Stomp.over(socket);
	            
	            stompClient.connect({}, function(frame) {
	                // 기본적으로 본인 개인큐 구독 (알림/DM 수신)
	                stompClient.subscribe("/user/queue/notifications",(message)=>{
	                	
	                	const chatRoom = JSON.parse(message.body)
	                	const table = document.querySelector("table");
	            		const newRow = document.createElement("tr");
	            		
	            		newRow.classList.add("chatListOne");
	            		newRow.dataset.roomId=chatRoom.chatRoomId;
	            		newRow.style.cursor = "pointer";
	            		newRow.innerHTML = "<td>"+chatRoom.chatRoomId+"</td><td>"+chatRoom.chatRoomTitle+"</td>"+"<td>"+chatRoom.unreadCount+"</td>";
	            		newRow.addEventListener("click",()=>{
	            			window.open("/chat/room/detail/"+chatRoom.chatRoomId,"","width=500,height=600 ,left=600, top=100");
	            		});
	            		table.appendChild(newRow);
	                	
	                	
	                });
	           
	               
	            });    
				//1대1 채팅 생성
				const createBtn = document.getElementById("createBtn");
				createBtn.addEventListener("click",()=>{
					 const username = document.getElementById("user").value;
					 const roomdata =  {
							 username : username,
							 chatRoomType : "direct"
					 };
					 fetch("/chat/room/createRoom",{
			            	method: "POST",
			            	headers: {"Content-Type" :"application/json"},
			            	body : JSON.stringify(roomdata)
			            })
			            .then(res=>res.json())
			            .then(chatRoom=>{
			            	
			            	stompClient.subscribe("/sub/chat/room/"+chatRoom.chatRoomId,(message)=>{
			            		
			            	})
			            	//방생성 후 화면에서도 바로 목록을 보여줌
			            	let existing = false;
			            	document.querySelectorAll(".chatListOne").forEach(tr=>{
			            		if(tr.getAttribute("data-roomId")==chatRoom.chatRoomId){
			            			existing=true;
			            		}
			            	});
			            	
			            	
			            	// 중복 확인
			            	if(!existing){
			            		const table = document.querySelector("table");
			            		const newRow = document.createElement("tr");
			            		
			            		newRow.classList.add("chatListOne");
			            		newRow.dataset.roomId=chatRoom.chatRoomId;
			            		newRow.style.cursor = "pointer";
			            		newRow.innerHTML = "<td>"+chatRoom.chatRoomId+"</td><td>"+chatRoom.chatRoomTitle+"</td>";
			            		newRow.addEventListener("click",()=>{
			            			window.open("/chat/room/detail/"+chatRoom.chatRoomId,"","width=500,height=600 ,left=600, top=100");
			            			
			            		});
			            		table.appendChild(newRow);
			            		console.log("1");
			            	}else{
			            		window.open("/chat/room/detail/"+chatRoom.chatRoomId,"","width=500,height=600 ,left=600, top=100");
			            	}
			            	
			         
						
			            });
					 
				})
          
	            //채팅방 클릭시 해당 채팅방을 띄움 , 동시에 마지막 확인 메세지를 변경
				const chatListOne = document.querySelectorAll(".chatListOne").forEach(list =>{
					const roomId= list.getAttribute('data-roomId');
					list.addEventListener("click",()=>{
						window.open("/chat/room/detail/"+roomId,"","width=500,height=600 ,left=600, top=100");
						fetch("/chat/participant/lastMessage/" + roomId, {
							  method: "POST"
							});
						list.querySelector(".unreadCount").innerText="";
						list.setAttribute("data-unreadCount","0");
					})
					
				});
				
				
				//그룹 채팅 생성 모달 
				const modal = document.getElementById("createRoomModal");
				const btn = document.getElementById("createRoomBtn");
				const span = document.querySelector(".close");

				// 열기
				btn.onclick = function() {
				  modal.style.display = "block";
				};

				// 닫기
				span.onclick = function() {
				  modal.style.display = "none";
				};

				// 바깥 클릭 시 닫기
				window.onclick = function(event) {
				  if (event.target == modal) {
				    modal.style.display = "none";
				  }
				};
				
			
				// 생성버튼 
				document.getElementById("createRoomConfirmBtn").addEventListener("click", () => {
					  const roomTitle = document.getElementById("roomTitle").value;
					  const selected = Array.from(document.querySelectorAll("#participantList input:checked"))
					                        .map(cb => cb.value);

					  const payload = {
					    roomTitle: roomTitle,
					    participants: selected
					  };

					  fetch("/chat/room/createRoom", {
					    method: "POST",
					    headers: { "Content-Type": "application/json" },
					    body: JSON.stringify(payload)
					  })
					  .then(res => res.json())
					  .then(chatRoom => {
					    // 목록에 새 방 추가
					    console.log("방 생성됨:", chatRoom);
					    modal.style.display = "none";
					  });
					});
				</script>
			
			
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>