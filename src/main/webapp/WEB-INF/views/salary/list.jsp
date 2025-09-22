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
<main>
	<table id="productsTable" class="table table-hover table-product" style="width:100%">
		<thead>
		<tr>
			<th>ID</th>
			<th>사원명</th>
			<th>부서</th>
			<th>직위</th>
			<th>직책</th>
			<th>기본급</th>
			<th>수당</th>
			<th>공제</th>
			<th>실지급액</th>
			<th>지급일</th>
		</tr>
		</thead>
		<tbody id="tbody">
		</tbody>
	</table>

</main>


<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/salary/list.js"></script>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>