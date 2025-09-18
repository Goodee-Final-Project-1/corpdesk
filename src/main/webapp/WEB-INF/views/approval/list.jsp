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
			
			<div class="email-wrapper rounded border bg-white">
				<div class="row no-gutters justify-content-center">
				
				<div class="col-lg-4 col-xl-3 col-xxl-2">
					<div class="email-left-column email-options p-4 p-xl-5">
						<a href="email-compose.html" class="btn btn-block btn-primary btn-pill mb-4 mb-xl-5">새 결재 진행</a>
						
						<ul class="pb-2">
						  <li class="d-block active mb-4">
						    <a href="email-inbox.html">결재 대기 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">결재 예정 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">결재 요청 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">결재 문서함</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">임시 저장 목록</a>
						  </li>
						</ul>
					</div>
				</div>
				
				<div class="col-lg-8 col-xl-9 col-xxl-10">
					<div class="email-right-column p-4 p-xl-5">
						
						<!-- section1 - 결재 요청 문서 목록 -->
						<section class="email-details pl-4 pr-4 pt-4">
							
							<div class="d-flex justify-content-between align-items-center">
								<h4 class="text-dark">결재 요청 문서 목록</h4>
								<button type="button" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></button>
							</div>
							
							<div class="email-details-content pl-0 pr-0">
								<table class="table">
								  
								  <thead>
								    <tr>
								      <th class="col-2">기안일</th>
								      <th class="col-6">제목</th>
								      <th class="col-2">부서</th>
								      <th class="col-2">상태</th>
								    </tr>
								  </thead>
								  
								  <tbody>
								  	<c:forEach items="${reqList }" var="el">
									    <tr style="cursor: pointer;">
									      <td>${fn:substring(el.createdAt, 0, 10) }</td>
									      <td>${el.formType }</td>
									      <td>${el.departmentId }</td>
									      <td>
									      	<c:choose>
									      		<c:when test="${el.status eq 89}">
									      			<span class="badge badge-info">승인</span>
									      		</c:when>
									      		<c:when test="${el.status eq 78}">
									      			<span class="badge badge-danger">반려</span>
									      		</c:when>
									      		<c:when test="${el.status eq 87}">
									      			<span class="badge badge-light">대기</span>
									      		</c:when>
									      		<c:otherwise>
									      			<span class="badge badge-outline-info">임시저장</span>
									      		</c:otherwise>
									      	</c:choose>
										    </td>
									    </tr>
								  	</c:forEach>
								  </tbody>
								  
								</table>
							</div>
							
						</section>
						<!--  -->
						
						<!-- section2 - 결재 대기 문서 목록 -->
						<section class="email-details pl-4 pr-4 pt-4">
							
							<div class="d-flex justify-content-between align-items-center">
								<h4 class="text-dark">결재 대기 문서 목록</h4>
								<button type="button" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></button>
							</div>
							
							<div class="email-details-content pl-0 pr-0">
								<table class="table">
								  
								  <thead>
								    <tr>
								      <th class="col-2">기안일</th>
								      <th class="col-4">제목</th>
								      <th class="col-2">기안부서</th>
								      <th class="col-2">기안자</th>
								      <th class="col-2">상태</th>
								    </tr>
								  </thead>
								  
								  <tbody>
								  	<c:forEach items="${waitList }" var="el">
									    <tr style="cursor: pointer;">
									      <td>${fn:substring(el.createdAt, 0, 10) }</td>
									      <td>${el.formType }</td>
									      <td>${el.departmentId }</td>
									      <td>${el.username }</td>
									      <td>
									      	<c:choose>
									      		<c:when test="${el.status eq 89}">
									      			<span class="badge badge-info">승인</span>
									      		</c:when>
									      		<c:when test="${el.status eq 78}">
									      			<span class="badge badge-danger">반려</span>
									      		</c:when>
									      		<c:when test="${el.status eq 87}">
									      			<span class="badge badge-light">대기</span>
									      		</c:when>
									      		<c:otherwise>
									      			<span class="badge badge-outline-info">임시저장</span>
									      		</c:otherwise>
									      	</c:choose>
										    </td>
									    </tr>
								  	</c:forEach>
								  </tbody>
								  
								</table>
							</div>
							
						</section>
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