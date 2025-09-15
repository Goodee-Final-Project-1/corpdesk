<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
                                <form:button>이메일 변경</form:button>
                            </li>
                            <li>
                                <form:label path="externalEmailPassword">이메일 비밀번호: </form:label>
                                <form:input path="externalEmailPassword" placeholder="이메일 비밀번호"/>
                                <form:errors path="externalEmailPassword"/>
                            </li>
                        </form:form>
                </ul>
            </div>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>