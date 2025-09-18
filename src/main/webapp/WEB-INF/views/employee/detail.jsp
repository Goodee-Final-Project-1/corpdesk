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
	<ul>
		<li>이름: ${employee.name}</li>
		<li>이메일: ${employee.externalEmail}</li>
		<li>부서:</li>
		<li>직책:</li>
		<li>직위:</li>
		<li>휴대전화:</li>
		<li>직통전화:</li>
		<li>입사일자:</li>

		<li>주민(외국인)번호:</li>
		<li>국적:</li>
		<li>체류 자격:</li>
		<li>영문 이름:</li>

		<li>성별:</li>
		<li>생년월일:</li>
		<li>주소:</li>
		<li>현재 기본급:</li>
		<li>급여 은행:</li>
		<li>급여 계좌:</li>
	</ul>

</div>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>