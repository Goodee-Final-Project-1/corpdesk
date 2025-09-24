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
        <h1>전체 게시판 목록</h1>

        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>게시판 이름</th>
              <th>타입</th>
              <th>생성일</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="board" items="${boards}">
              <tr>
                <td>${board.boardId}</td>
                <td><a href="/post/list/${board.boardId}">${board.boardName}</a></td>
                <td>${board.boardType}</td>
                <td>${board.createdAt}</td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>