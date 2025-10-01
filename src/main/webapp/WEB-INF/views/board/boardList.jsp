<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>게시판</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
      <!-- 탭/네비게이션 -->
      <div style="margin-bottom:12px;">
        <a href="${pageContext.request.contextPath}/board/notice">공지 게시판</a> |
        <a href="${pageContext.request.contextPath}/board/me">부서 게시판</a>
      </div>

      <!-- 게시판 제목 -->
      <h2><c:out value="${title != null ? title : '게시판'}"/></h2>

      <!-- 빈 목록 처리 -->
      <c:if test="${empty post}">
        <div>게시글이 없습니다.</div>
      </c:if>

      <!-- 목록 -->
      <c:if test="${not empty post}">
        <table border="1" style="width:100%; border-collapse:collapse; text-align:center;">
          <thead>
            <tr>
              <th style="width:55%;">제목</th>
              <th style="width:15%;">작성자</th>
              <th style="width:15%;">작성일</th>
              <th style="width:15%;">조회수</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${post}" var="post">
              <tr>
                <td style="text-align:left;">
                  <c:choose>

                    <c:when test="${title eq '공지 게시판'}">
                      <a href="${pageContext.request.contextPath}/board/notice/${post.boardId}">
                        <c:out value="${post.title}"/>
                      </a>
                    </c:when>

                    <c:otherwise>
                      <a href="${pageContext.request.contextPath}/board/department/${post.boardId}">
                        <c:out value="${post.title}"/>
                      </a>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td><c:out value="${post.username}"/></td>
                <td>
                  <spring:eval expression="T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd HH:mm').format(post.createdAt)"/>
                </td>
                <td><c:out value="${post.viewCount != null ? post.viewCount : 0}"/></td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:if>


			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>