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
<script>
    const user = "${user}";   // 현재 로그인한 사용자
</script>
			
			<h2>${roomId}방</h2>
	<div class = "messageContainer">
		<div id="chat-list">
			
			
		</div>
	</div>
	
	
			
    <input type="text" id="messageInput">
    <button type= "button" id="sendBtn">메시지 전송</button>
	
    
	
	<!-- 메시지 연결 송수신-->
    <script>
    // 메세지 수신 박스
	const messageContainer = document.querySelector(".messageContainer")
	const listChat= document.getElementById('chat-list');
    
    
    	const roomId = ${roomId}
        const socket = new SockJS("/ws");
    	const stompClient = Stomp.over(socket);
    	const sendBtn = document.getElementById("sendBtn");
    	// 에러메세지가 두번 호출되는걸 막음 
    	let errorHandled=false;
    	// 메세지 수신
    	stompClient.connect({},function(frame){
    		stompClient.subscribe("/sub/chat/room/"+${roomId},(message)=>{
    			 const li = document.createElement("li");
    			 const msg = JSON.parse(message.body);
        	     li.innerText =  "["+msg.employeeUsername+"]"+msg.messageContent;
        	     li.setAttribute('data-no',msg.messageId);
        	    
        		 listChat.appendChild(li);
        		 messageContainer.scrollTop = messageContainer.scrollHeight;
        	})
        
    	},function(error){
    		if(errorHandled)return;
    		errorHandled=true;
    		
			alert("참여되지 않은 사용자입니다.");
			location.href="/chat/room/list";
    	}
    	)
    	
    	//메세지 전송
    	sendBtn.addEventListener("click",()=>{
    		const inputText = document.getElementById("messageInput");
    		const msg = {
    				chatRoomId: roomId,
    				employeeUsername : user ,
    				messageContent : inputText.value
    		};
    		stompClient.send("/pub/chat/message",{},JSON.stringify(msg));
    		inputText.value="";
    
    	});
    	
    </script>
	
	
	<!-- 메세지 스크롤 이벤트 처리 -->
	<script>
	// 이전 기록을 가져오기전에 스크롤을 상단으로 올릴 경우 채팅내역 로딩을 기다리게함 
		let isScrolled = false;
		let lastMessageId = null;
		let isEnd= false;
	
		
		fetchList();
		
		//스크롤 이벤트 발생 시 호출 
		messageContainer.addEventListener("scroll", ()=>{
			if(messageContainer.scrollTop<1 && isScrolled===false){
				isScrolled=true;
				fetchList();
			}
		});
		
	//서버에서 이전 메시지 가져오기
	    function fetchList(){
		//처음 데이터까지 다 가져온 경우면 db접근안하고 리턴
		if(isEnd == true){
			return;
		}
		//채팅 리스트를 가져올 시작 번호
		const firstLi = document.querySelector("#chat-list li");
		const endNo = firstLi ? Number(firstLi.getAttribute("data-no")) : 0;
		
		//채팅 리스트 가져오기
		fetch("/chat/message/list/"+roomId+"/"+endNo+"/"+20,{
			method:"GET"
		}).then(res=>res.json())
		  .then(res=>{
			const messages = res.messages;
			const size = res.size;
			
			if(size<20){
				isEnd=true;
			}
			// 리스트를 가져오기전 스크롤 높이를 저장
			const oldScrollHeight = messageContainer.scrollHeight;
			//메세지를 화면에 뿌려줌
			messages.forEach(msg=>{
				console.log(msg.messageId)
				const li = document.createElement('li');
				li.setAttribute('data-no',msg.messageId);
				li.textContent = "["+msg.employeeUsername+"]"+msg.messageContent;
				console.log(li.getAttribute("data-no"));
				listChat.insertBefore(li,listChat.firstChild);
				
				
			});
			// 스크롤 위치 보정
		      const newScrollHeight = messageContainer.scrollHeight;
		      messageContainer.scrollTop = newScrollHeight - oldScrollHeight;
			
			
		  })
		  .finally(()=>{
			  isScrolled=false;
		  });
		const li = document.querySelectorAll("#chat-list li");
		li.forEach(l=>{
			console.log(l.getAttribute("data-no"));
			})
	}
	    const last = document.querySelector("#chat-list li:last-child");
	
	//마지막으로 확인한 메세지 저장
	window.addEventListener("beforeunload",()=>{
		const last = document.querySelector("#chat-list li:last-child").getAttribute("data-no");
		if(last){
			navigator.sendBeacon("/chat/participant/lastMessage/"+last+"/"+roomId);	
		}
		
		
		
	})
	
	</script>
	
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>