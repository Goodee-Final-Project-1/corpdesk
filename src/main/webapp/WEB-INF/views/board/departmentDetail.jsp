<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
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
			<!-- department board가 비어 있을 때 -->
      <c:if test="${empty department}">
        <h2>${departmentId} 부서 게시판</h2>
        <div>게시글이 없습니다</div>
      </c:if>

      <!-- department board가 있을 때 -->
      <c:if test="${not empty department}">
        <h2>${departmentId} 부서 게시판</h2>

    <!-- 게시글 목록 테이블 -->
    <table border="1" style="width: 100%; border-collapse: collapse; text-align: center;">
      <thead>
        <tr style="background-color: #f2f2f2;">
          <th>제목</th>
          <th>작성자</th>
          <th>조회수</th>
          <th>내용</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${department}" var="b">
          <tr>
            <td>${b.title}</td>
            <td>${b.username}</td>
            <td>${b.viewCount != null ? b.viewCount : 0}</td>
            <td style="text-align: left;">${b.content}</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>

        <!-- 목록 버튼 -->
        <div>
          <a href="${pageContext.request.contextPath}/department/department/list">목록으로</a>
        </div>
      </c:if>      

			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>