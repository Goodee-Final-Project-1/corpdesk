<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>

		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<div>
				<aside class="col-lg-4 col-xl-3 col-xxl-2">
					<button>메일 쓰기</button>
					<ul>
						<li>받은 메일</li>
						<li>보낸 메일</li>
					</ul>

				</aside>
				<main class="col-lg-8 col-xl-9 col-xxl-10">
					<div>
						<%-- 페이징 --%>
					</div>
					<div>
						<table>
							<c:forEach var="msg" items="${messageList}">
								<tr>
									<td>${msg.from}</td>	<!-- 보낸 사람 -->
									<td>${msg.subject}</td>	<!-- 내용 -->
									<td>${msg.sentDate}</td>	<!-- 날짜 -->
								</tr>
							</c:forEach>
							<tr>
								<td>보낸 사람</td>	<!-- 보낸 사람 -->
								<td>내용</td>	<!-- 내용 -->
								<td>날짜</td>	<!-- 날짜 -->
							</tr>
						</table>
					</div>

				</main>
			</div>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>