<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
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
	<main>
		<div>
			<h1 id="subject"></h1>
		</div>
		<div>
			<div>
				<ul>
					<li>
						<h2 id="from"></h2>
					</li>
					<li>
						<h3 id="recipients"></h3>
					</li>
					<li>
						<p id="sentDate"></p>
					</li>
				</ul>
			</div>
			<div>
				<p id="content"></p>
			</div>
		</div>
	</main>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/email/detail.js"></script>
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>