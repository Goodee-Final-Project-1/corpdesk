<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title><c:out value="${post.title}" /></title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<h2><c:out value="${post.title}"/></h2>

      <div style="color:#666; margin-bottom:8px;">
        작성자: <c:out value="${post.username}"/> |
        작성일: <spring:eval expression="T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd HH:mm').format(post.createdAt)"/>
      </div>

      <div style="white-space:pre-wrap; line-height:1.6;">
        <c:out value="${post.content}"/>
      </div>

      <div style="margin-top:16px;">
        <a href="${pageContext.request.contextPath}/board/notice">목록으로</a>
        <c:if test="${isOwner}">
          <a href="${pageContext.request.contextPath}/board/notice/${post.boardId}/edit">수정</a>
          <form method="post" action="${pageContext.request.contextPath}/board/notice/${post.boardId}/delete" style="display:inline;" onsubmit="return confirm('삭제하시겠습니까?');">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">삭제</button>
          </form>
        </c:if>
      </div>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>