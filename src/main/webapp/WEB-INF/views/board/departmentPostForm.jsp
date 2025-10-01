<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
			<h1>${departmentId}부서 게시글 작성</h1>
      
      <!-- 스프링 시큐리티를 사용 중이라면 CSRF 토큰을 같이 전송 -->

      <!-- 부서 글 등록 폼 -->
      <form method="post" action="${pageContext.request.contextPath}/board/department/${departmentId}">
        <c:if test="${net empty _csrf}">
          <input type="hidden" name="${csrfParam}" value="${csrfToken}">
        </c:if>

        <div>
          <label for="title">제목</label>
          <input type="text" id="title" name="title" required maxlength="255">
        </div>

        <div>
          <label for="content">내용</label>
          <textarea id="content" name="content"></textarea>
        </div>

        <div>
          <button type="submit">등록</button>
          <a href="${pageContext.request.contextPath}/board/department/${departmentId}">게시글 목록으로 이동</a>
        </div>
      </form>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>