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
			<div>
        <h2>부서게시판 생성</h2>

        <form method="post" action="${pageContext.request.contextPath}/board/department">
          <div>
            <label for="">부서 ID (1부터 가능)</label>
            <input type="number" id="departmentId" name="departmentId" min="1" required />
          </div>

          <div>
            <label for="title">부서명</label>
            <input type="text" id="title" name="title" maxlength="255" required />
          </div>

          <div>
            <button type="submit">생성</button>
            <a href="${pageContext.request.contextPath}/board/department">목록으로 이동</a>
          </div>
          
        </form>

        <c:if test="${not empty error}">
          <p><c:out value="${error}"></c:out></p>
        </c:if>

      </div>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>