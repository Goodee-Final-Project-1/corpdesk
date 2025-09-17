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
			
			<div class="email-wrapper rounded border bg-white">
				<div class="row no-gutters justify-content-center">
				
				<div class="col-lg-4 col-xl-3 col-xxl-2">
					<div class="email-left-column email-options p-4 p-xl-5">
						<a href="email-compose.html" class="btn btn-block btn-primary btn-pill mb-4 mb-xl-5">새 결재 진행</a>
						
						<ul class="pb-2">
						  <li class="d-block active mb-4">
						    <a href="email-inbox.html">
						      <!-- <i class="mdi mdi-download mr-2"></i> --> 결재 대기 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">
						      <!-- <i class="mdi mdi-star-outline mr-2"></i> --> 결재 예정 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">
						      <!-- <i class="mdi mdi-playlist-edit mr-2"></i> --> 결재 요청 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">
						      <!-- <i class="mdi mdi-open-in-new mr-2"></i> --> 결재 문서함</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">
						      <!-- <i class="mdi mdi-trash-can-outline mr-2"></i> --> 임시 저장 목록</a>
						  </li>
						</ul>
					</div>
				</div>
				
				<div class="col-lg-8 col-xl-9 col-xxl-10">
					<div class="email-right-column p-4 p-xl-5">
						
						<!-- 양식 시작 -->
						<div>
							<button type="button" class="btn btn-info mr-1" data-toggle="modal" data-target="#modal-add-contact">결재 요청</button>
							<button type="button" class="btn btn-outline-info mr-1" data-toggle="modal" data-target="#modal-add-contact">임시저장</button>
							<button type="button" class="btn btn-light" data-toggle="modal" data-target="#modal-add-contact">취소</button>
						</div>
						<hr>
						
						<!--  -->
					
					</div>
				</div>
				
				</div>
			</div>
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>