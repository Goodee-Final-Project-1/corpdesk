<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>사원 목록</h2>


<form method="get">
    <select name="condition">
        <option value="all">전체</option>
        <option value="name">이름</option>
        <option value="account">계정</option>
    </select>
    <input type="text" name="keyword" value="${keyword}">
    <button type="submit">조회</button>
</form>

<table border="1">
    <tr>
        <th>사번</th><th>사원명</th><th>ID</th><th>부서</th>
        <th>직위</th><th>휴대전화</th><th>입사일</th><th>퇴사일</th><th>현재상태</th><th>계정상태</th><th>수정</th>
    </tr>
    <c:forEach var="emp" items="${employees}">
        <tr>
            <td>${emp.employeeId}</td>
            <td>${emp.name}</td>
            <td>${emp.username}</td>
            <td>${emp.department.departmentName}</td>
            <td>${emp.position.positionName}</td>
            <td>${emp.mobilePhone}</td>
            <td>${emp.hireDate}</td>
            <td>
                <c:if test="${emp.lastWorkingDay == null}"><span>-</span></c:if>
                <c:if test="${emp.lastWorkingDay != null}"><span>${emp.lastWorkingDay}</span></c:if>
                
            </td>
            <td>
                <c:choose>
                    <c:when test="${emp.status == '출근'}"><span style="color:green">출근</span></c:when>
                    <c:when test="${emp.status == '퇴근'}"><span style="color:purple">퇴근</span></c:when>
                    <c:when test="${emp.status == '휴가'}"><span style="color:pink">휴가</span></c:when>
                    <c:otherwise><span style="color:orange">출근전</span></c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${emp.enabled}"><span style="color:green">정상</span></c:when>
                    <c:otherwise><span style="color:red">비정상</span></c:otherwise>
                </c:choose>
            </td>
            <td><a href="<c:url value='/employee/edit/${emp.employeeId}'/>">수정</a></td>
        </tr>
    </c:forEach>
</table>
<a href="<c:url value='/employee/add'/>">사원 등록</a>
<!-- 페이징 -->
<div>
    <c:if test="${page.totalPages > 0}">
    <c:forEach begin="0" end="${page.totalPages - 1}" var="i">
        <a href="?page=${i}&keyword=${keyword}">${i+1}</a>
    </c:forEach>
	</c:if>
</div>
</html>