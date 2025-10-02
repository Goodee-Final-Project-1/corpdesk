<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>부서글 수정</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<h2>부서 글 수정</h2>
      <form method="post" action="${pageContext.request.contextPath}/board/department/${post.boardId}">
        <div style="margin-bottom:8px;">
          <label>제목</label><br/>
          <input type="text" name="title" style="width:100%;" value="${post.title}" required />
        </div>
        <div style="margin-bottom:8px;">
          <label>내용</label><br/>
          <textarea name="content" rows="10" style="width:100%;" required>${post.content}</textarea>
        </div>
        <button type="submit">저장</button>
        <a href="${pageContext.request.contextPath}/board/department/${post.boardId}">취소</a>
      </form>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>