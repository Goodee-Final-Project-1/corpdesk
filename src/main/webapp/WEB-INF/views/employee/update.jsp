<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
	<form:form action="/employee/update/password" method="post" modelAttribute="employee">
		<form:label path="password">현재 비밀번호: </form:label>
		<form:password path="password" placeholder="현재 비밀번호"/>
		<form:errors path="password"/>
		<form:label path="passwordNew">새 비밀번호: </form:label>
		<form:password path="passwordNew" placeholder="새 비밀번호"/>
		<form:errors path="passwordNew"/>
		<form:label path="passwordCheck">새 비밀번호 확인: </form:label>
		<form:password path="passwordCheck" placeholder="새 비밀번호 확인"/>
		<form:errors path="passwordCheck"/>
		<form:button>비밀번호 변경</form:button>
	</form:form>
</div>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>