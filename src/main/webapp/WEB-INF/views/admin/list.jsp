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
		<div class="card-header">

		</div>
		<div class="card-body">
			<div>
				<table class="table table-striped">
					<tr>
						<th>ID</th>
						<th>이름</th>
						<th>부서</th>
						<th>직위</th>
						<th>권한</th>
					</tr>
					<c:forEach var="e" items="${employeeList}">
						<tr>
							<td>${e.username}</td>
							<td>${e.name}</td>
							<td>${e.departmentName}</td>
							<td>${e.positionName}</td>
							<td>
								<select class="form-control role" data-username="${e.username}">
									<c:forEach var="r" items="${roleList}">
										<option value="${r.roleId}" ${r.roleName == e.roleName ? 'selected' : ''}>${r.roleName}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/admin/list.js"></script>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>