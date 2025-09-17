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

		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp" />
		<!-- 내용 시작 -->
		<table id="productsTable" class="table table-hover table-product"
			style="width: 100%">
			<thead>
				<tr>
					<th>사원명</th>
					<th>ID</th>
					<th>부서</th>
					<th>직위</th>
					<th>휴대전화</th>
					<th>입사일</th>
					<th>퇴사일</th>
					<th>현재상태</th>
					<th>계정상태</th>
					<th>수정</th>
				</tr>
			</thead>
			<tbody>
		
		
				<c:forEach var="emp" items="${employees}">
					<tr>
						<td>${emp.name}</td>
						<td>${emp.username}</td>
						<td>${emp.departmentName}</td>
						<td>${emp.positionName}</td>
						<td>${emp.mobilePhone}</td>
						<td>${emp.hireDate}</td>
						<td><c:if test="${emp.lastWorkingDay == null}">
								<span>-</span>
							</c:if> <c:if test="${emp.lastWorkingDay != null}">
								<span>${emp.lastWorkingDay}</span>
							</c:if></td>
						<td><c:choose>
								<c:when test="${emp.status == '출근'}">
									<span style="color: green">출근</span>
								</c:when>
								<c:when test="${emp.status == '퇴근'}">
									<span style="color: purple">퇴근</span>
								</c:when>
								<c:when test="${emp.status == '휴가'}">
									<span style="color: pink">휴가</span>
								</c:when>
								<c:otherwise>
									<span style="color: orange">출근전</span>
								</c:otherwise>
							</c:choose></td>
						<td><c:choose>
								<c:when test="${emp.enabled}">
									<span style="color: green">정상</span>
								</c:when>
								<c:otherwise>
									<span style="color: red">비정상</span>
								</c:otherwise>
							</c:choose></td>
						<td><a href="<c:url value='/employee/edit/${emp.username}'/>">수정</a></td>
						<!-- TODO employeeId를 username으로 변경 -->
					</tr>
				</c:forEach>
		</table>
		<a href="<c:url value='/employee/add'/>"class="btn btn-primary">사원 등록</a>
		<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp" />

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>