<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>

<%--	<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css' rel='stylesheet'>--%>
<%--	<link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css' rel='stylesheet'>--%>
	<link rel="stylesheet" href="/css/calendar/list.css">

	<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.19/index.global.min.js'></script>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>

<div class="modal fade" id="scheduleModal" tabindex="-1" role="dialog" aria-labelledby="scheduleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="scheduleModalLabel">일정 등록</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form method="POST" action="/personal-schedule">
				<div class="modal-body">
					<%-- TODO 인증 붙이면 input hidden 삭제 --%>
					<input type="hidden" name="username" value="jung_frontend">

					<div class="form-group">
						<label for="scheduleName">일정명</label>
						<input type="text" class="form-control" id="scheduleName" name="scheduleName" placeholder="일정명을 입력하세요">
					</div>
					<div class="form-group">
						<label for="scheduleDateTime">일시</label>
						<input type="datetime-local" class="form-control" id="scheduleDateTime" name="scheduleDateTime">
					</div>
					<div class="form-group">
						<label for="scheduleLocation">주소</label>
						<input type="text" class="form-control" id="scheduleLocation" name="address" placeholder="주소를 입력하세요">
					</div>
					<div class="form-group">
						<label for="scheduleContent">내용</label>
						<textarea class="form-control" id="scheduleContent" name="content" rows="3" placeholder="내용을 입력하세요"></textarea>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-primary">등록</button>
					<button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
				</div>
			</form>
		</div>
	</div>
</div>

<!-- 내용 시작 -->
<div class="d-flex">
	<div class="card card-default h-50">
		<div class="card-body">
			<jsp:include page="aside.jsp"/>
		</div>
	</div>

	<div class="card card-default w-75">
		<div class="card-header"></div>
		<div class="card-body">
			<div id='calendar'></div>
		</div>
	</div>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/calendar/list.js"></script>
<script src="/js/calendar/add.js"></script>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>