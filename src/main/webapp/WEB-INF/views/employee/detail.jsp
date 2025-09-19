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
		<li>부서: ${department.departmentName}</li>
<%--		<li>직책: ${employee.}</li>--%>
		<li>직위: ${position.positionName}</li>
		<li>휴대전화: ${employee.mobilePhone}</li>
		<li>직통전화: ${employee.directPhone}</li>
		<li>입사일자: ${employee.hireDate}</li>

		<li>주민(외국인)번호: ${employee.residentNumber}</li>
		<li>국적: ${employee.nationality}</li>
		<li>체류 자격: ${employee.visaStatus}</li>
		<li>영문 이름: ${employee.englishName}</li>

		<li>성별: ${employee.gender}</li>
		<li>생년월일: ${employee.birthDate}</li>
		<li>주소: ${employee.address}</li>
<%--		<li>현재 기본급: ${employee.}</li>--%>
<%--		<li>급여 은행: ${employee.}</li>--%>
<%--		<li>급여 계좌: ${employee.}</li>--%>
	</ul>

</div>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>