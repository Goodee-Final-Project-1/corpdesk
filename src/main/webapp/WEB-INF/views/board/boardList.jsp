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

<div class="card card-default">
  <div class="card-header d-flex justify-content-between align-items-center">
    <h2 class="mb-0"><c:out value="${title != null ? title : '게시판'}"/></h2>
    <a class="btn btn-primary" href="${writePath}">
      <i class="mdi mdi-pencil mr-1"></i>글쓰기
    </a>
  </div>

  <div class="card-body">
    <!-- 탭/네비게이션 -->
    <div class="mb-4">
      <a href="${pageContext.request.contextPath}/board/notice" class="btn btn-outline-primary">공지 게시판</a>
      <a href="${pageContext.request.contextPath}/board/department" class="btn btn-outline-primary">부서 게시판</a>
    </div>

    <!-- 빈 목록 처리 -->
    <c:if test="${empty post}">
      <div class="text-center p-5 text-muted">
        <p class="mb-0">게시글이 없습니다.</p>
      </div>
    </c:if>

    <!-- 목록 -->
    <c:if test="${not empty post}">
      <div class="table-responsive">
        <table class="table table-hover">
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
              <td>
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
      </div>
    </c:if>
  </div>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>