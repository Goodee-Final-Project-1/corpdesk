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
						<th>채팅방 내용</th>
						<th>참여자</th>
					</tr>
					<c:forEach items="${roomList }" var="room">
						<tr id="chatListOne" style="cursor:pointer;">
							
								<td>${room.chatRoomId}</td>
								<td>${room.chatRoomTitle}</td>
							
						</tr>
					</c:forEach>
									
				</table>
			<script type="text/javascript">
				const chatListOne = document.getElementById;
				
				chatListOne.addEventListener("click",()=>{
					window.open("chat_page")
				})
			
			</script>
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>