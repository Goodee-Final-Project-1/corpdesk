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
		<form:form action="/employee/update/email" method="post" modelAttribute="employee">
			<li>
				<form:label path="externalEmail">이메일: </form:label>
				<form:input path="externalEmail" placeholder="이메일"/>
				<form:errors path="externalEmail"/>
			</li>
			<li>
				<form:label path="externalEmailPassword">이메일 비밀번호: </form:label>
				<form:input path="externalEmailPassword" placeholder="이메일 비밀번호"/>
				<form:errors path="externalEmailPassword"/>
			</li>
			<form:button class="btn btn-primary">이메일 변경</form:button>
		</form:form>
	</ul>
	<p>이메일 비밀번호 설정 방법</p>
	<ul>
		<li>
			<a href="https://help.naver.com/service/5640/contents/8584" target="_blank">네이버 예시</a>
		</li>
		<li>
			<a href="https://support.google.com/accounts/answer/185833" target="_blank">구글 예시</a>
		</li>
	</ul>

</div>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>