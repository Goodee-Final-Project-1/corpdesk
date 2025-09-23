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
<div class="card-footer card-profile-footer">
	<ul class="nav nav-border-top justify-content-center">
		<li class="nav-item">
			<a class="nav-link active" href="/employee/detail">내 정보</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="/employee/update/email">이메일 변경</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="/employee/update/password">비밀번호 변경</a>
		</li>
	</ul>
</div>

<div class="card card-default">
	<div class="card-header">
		<h2 class="mb-5">내 정보</h2>
	</div>

	<div class="card-body">
		<ul>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">이름:</h6>
				<span class="col-sm-8 col-lg-10">${employee.name}</span>
			</li>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">이메일:</h6>
				<span class="col-sm-8 col-lg-10">${employee.externalEmail}</span>
			</li>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">부서:</h6>
				<span class="col-sm-8 col-lg-10">${department.departmentName}</span>
			</li>
			<%--		<li>직책: ${employee.}</li>--%>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">직위:</h6>
				<span class="col-sm-8 col-lg-10">${position.positionName}</span>
			</li>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">휴대전화:</h6>
				<span class="col-sm-8 col-lg-10">${employee.mobilePhone}</span>
			</li>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">직통전화:</h6>
				<span class="col-sm-8 col-lg-10">${employee.directPhone}</span>
			</li>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">입사일자:</h6>
				<span class="col-sm-8 col-lg-10">${employee.hireDate}</span>
			</li>

			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">주민 (외국인)</br>등록 번호:</h6>
				<span class="col-sm-8 col-lg-10">${employee.residentNumber}</span>
			</li>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">국적:</h6>
				<span class="col-sm-8 col-lg-10">${employee.nationality}</span>
			</li>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">체류 자격:</h6>
				<span class="col-sm-8 col-lg-10">${employee.visaStatus}</span>
			</li>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">영문 이름:</h6>
				<span class="col-sm-8 col-lg-10">${employee.englishName}</span>
			</li>

			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">성별:</h6>
				<span class="col-sm-8 col-lg-10">${employee.gender}</span>
			</li>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">생년월일:</h6>
				<span class="col-sm-8 col-lg-10">${employee.birthDate}</span>
			</li>
			<li class="row mb-1">
				<h6 class="col-sm-4 col-lg-2">주소:</h6>
				<span class="col-sm-8 col-lg-10">${employee.address}</span>
			</li>
			<%--		<li>현재 기본급: ${employee.}</li>--%>
			<%--		<li>급여 은행: ${employee.}</li>--%>
			<%--		<li>급여 계좌: ${employee.}</li>--%>
		</ul>
	</div>
</div>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>