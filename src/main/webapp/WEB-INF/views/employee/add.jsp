<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<h2>사원 등록</h2>
<form:form method="post" modelAttribute="employee">
    이름: <form:input path="name" /><br/>
    아이디(로그인용): <form:input path="username" /><br/>
    비밀번호: <form:password path="password" /><br/>
    이메일: <form:input path="externalEmail" /><br/>
    입사일: <form:input path="hireDate" type="date"/><br/>
    성별: <form:select path="gender">
            <form:option value="M" label="남"/>
            <form:option value="F" label="여"/>
         </form:select><br/>
    <input type="submit" value="등록"/>
</form:form>
</html>