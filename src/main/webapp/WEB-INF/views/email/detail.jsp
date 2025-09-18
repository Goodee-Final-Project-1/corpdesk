<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
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
		<jsp:include page="aside.jsp"/>
		<main class="col-lg-8 col-xl-9 col-xxl-10">
			<div class="email-right-column p-4 p-xl-5">
				<div class="border rounded email-details">
					<div class="email-details-header">
						<h4 id="subject" class="text-dark"></h4>
					</div>
					<div class="email-details-content">
						<div class="email-details-content-header">
							<ul>
								<li>
									<h6 id="from" class="mt-0 text-dark font-weight-bold"></h6>
								</li>
								<li>
									<span id="recipients"></span>
								</li>
								<li>
									<time id="sentDate" class="p-1 p-xl-2"></time>
								</li>
							</ul>
						</div>
						<div>
							<p id="content"></p>
						</div>
					</div>
				</div>
			</div>
		</main>
	</div>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/email/detail.js"></script>
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>