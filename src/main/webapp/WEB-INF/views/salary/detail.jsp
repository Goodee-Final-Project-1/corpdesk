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

<div class="card card-default">
	<div class="card-body">
		<main>
			<div>
				<h2>급여 상세</h2>
			</div>
			<div>
				<nav></nav>
				<article>
					<div>
						<h3>지급 항목 합계</h3>
						<ul id="salary"></ul>
						<ul id="allowance"></ul>
					</div>
					<div>
						<h3>공제 항목 합계</h3>
						<ul id="deduction"></ul>
					</div>
				</article>
			</div>
		</main>
	</div>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/salary/detail.js"></script>
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>