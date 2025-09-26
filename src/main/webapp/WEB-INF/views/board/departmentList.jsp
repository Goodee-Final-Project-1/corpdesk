<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
			<h1>${departmentId}부서 게시판</h1>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>제목</th>
            <th>작성일</th>
            <th>조회수</th>
          </tr>
        </thead>
        <tbody>
          <!-- 데이터가 있을 경우 -->
          <c:forEach var="board" items="${departmentPage.content}">
            <tr>
              <td>${board.boardId}</td>
              <td><a href="${pageContext.request.contextPath}/board/department/${departmentId}/${board.boardId}">${board.title}</a></td>
              <td><fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd" /></td>
              <td>${board.viewCount}</td>
            </tr>
          </c:forEach>

          <!-- 데이터가 없을 경우 -->
          <c:if test="${departmentPage.totalElements == 0}">
            <tr>
              <td>등록된 글이 없습니다</td>
            </tr>
          </c:if>
        </tbody>
      </table>

      <!-- 페이징 네비게이션 -->
      <div>
        <!-- 이전 페이지 -->
        <c:if test="${deptPage.hasPrevious()}">
         <a href="${pageContext.request.contextPath}/board/dept/${deptId}?page=${deptPage.number - 1}&size=${deptPage.size}">이전</a>
        </c:if>

        <!-- 현재 페이지 / 전체 페이지 -->
        <span>${deptPage.number + 1}</span> / <span>${deptPage.totalPages}</span>

        <!-- 다음 페이지 -->
        <c:if test="${deptPage.hasNext()}">
         <a href="${pageContext.request.contextPath}/board/dept/${deptId}?page=${deptPage.number + 1}&size=${deptPage.size}">다음</a>
        </c:if>
      </div>

      <!-- 글쓰기 버튼 -->
      <div><a href="${pageContext.request.contextPath}/board/department/${departmentId}/new">등록</a></div>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>