<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
		<%--	<%@ include file="aside.jsp"%>--%>
		<main class="col-lg-8 col-xl-9 col-xxl-10">
			<div class="email-right-column p-4 p-xl-5">
				<div class="email-body-head mb-5 ">
					<h4 class="text-dark">New Message</h4>
				</div>
				<form id="form_data" class="email-compose mb-5" action="/api/email/sending" method="post">
					<div class="form-group">
						<input type="email" class="form-control" name="to" id="exampleEmail" placeholder="To: ">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" name="subject" id="exampleSubject" placeholder="Subject ">
					</div>
					<div id="standalone">
						<div id="toolbar" class="ql-toolbar ql-snow">
                <span class="ql-formats">
                  <span class="ql-font ql-picker">
										<span class="ql-picker-label" tabindex="0" role="button" aria-expanded="false"
													aria-controls="ql-picker-options-0">
											<svg viewBox="0 0 18 18">
												<polygon class="ql-stroke" points="7 11 9 13 11 11 7 11"></polygon>
												<polygon class="ql-stroke" points="7 7 9 5 11 7 7 7"></polygon>
											</svg>
										</span>
										<span class="ql-picker-options" aria-hidden="true" tabindex="-1" id="ql-picker-options-0">
											<span tabindex="0" role="button" class="ql-picker-item ql-selected"></span>
											<span tabindex="0" role="button" class="ql-picker-item" data-value="serif"></span>
											<span tabindex="0" role="button" class="ql-picker-item" data-value="monospace"></span>
										</span>
									</span>
									<select class="ql-font" style="display: none;"><option selected="selected"></option>
										<option value="serif"></option>
										<option value="monospace"></option>
									</select>
                  <span class="ql-size ql-picker">
										<span class="ql-picker-label" tabindex="0" role="button" aria-expanded="false"
													aria-controls="ql-picker-options-1">
											<svg viewBox="0 0 18 18">
												<polygon class="ql-stroke" points="7 11 9 13 11 11 7 11"></polygon>
												<polygon class="ql-stroke" points="7 7 9 5 11 7 7 7"></polygon>
											</svg>
										</span>
										<span class="ql-picker-options" aria-hidden="true" tabindex="-1" id="ql-picker-options-1">
											<span tabindex="0" role="button" class="ql-picker-item" data-value="small"></span>
											<span tabindex="0" role="button" class="ql-picker-item ql-selected"></span>
											<span tabindex="0" role="button" class="ql-picker-item" data-value="large"></span>
											<span tabindex="0" role="button" class="ql-picker-item" data-value="huge"></span>
										</span>
									</span>
									<select class="ql-size" style="display: none;">
										<option value="small"></option>
										<option selected="selected"></option><option value="large">
									</option>
										<option value="huge"></option>
									</select>
                </span>
							<span class="ql-formats">
                  <button class="ql-bold" type="button"><svg viewBox="0 0 18 18"> <path class="ql-stroke"
																																												d="M5,4H9.5A2.5,2.5,0,0,1,12,6.5v0A2.5,2.5,0,0,1,9.5,9H5A0,0,0,0,1,5,9V4A0,0,0,0,1,5,4Z"></path> <path
													class="ql-stroke"
													d="M5,9h5.5A2.5,2.5,0,0,1,13,11.5v0A2.5,2.5,0,0,1,10.5,14H5a0,0,0,0,1,0,0V9A0,0,0,0,1,5,9Z"></path> </svg></button>
                  <button class="ql-italic" type="button"><svg viewBox="0 0 18 18"> <line class="ql-stroke" x1="7"
																																													x2="13" y1="4" y2="4"></line> <line
													class="ql-stroke" x1="5" x2="11" y1="14" y2="14"></line> <line class="ql-stroke" x1="8"
																																												 x2="10" y1="14" y2="4"></line> </svg></button>
                  <button class="ql-underline" type="button"><svg viewBox="0 0 18 18"> <path class="ql-stroke"
																																														 d="M5,3V9a4.012,4.012,0,0,0,4,4H9a4.012,4.012,0,0,0,4-4V3"></path> <rect
													class="ql-fill" height="1" rx="0.5" ry="0.5" width="12" x="3" y="15"></rect> </svg></button>
                </span>
							<span class="ql-formats">
                  <span class="ql-color ql-picker ql-color-picker"><span class="ql-picker-label" tabindex="0"
																																				 role="button" aria-expanded="false"
																																				 aria-controls="ql-picker-options-2"><svg
													viewBox="0 0 18 18"> <line class="ql-color-label ql-stroke ql-transparent" x1="3" x2="15"
																										 y1="15" y2="15"></line> <polyline class="ql-stroke"
																																											 points="5.5 11 9 3 12.5 11"></polyline> <line
													class="ql-stroke" x1="11.63" x2="6.38" y1="9" y2="9"></line> </svg></span><span
													class="ql-picker-options" aria-hidden="true" tabindex="-1" id="ql-picker-options-2"><span
													tabindex="0" role="button" class="ql-picker-item ql-selected ql-primary"></span><span
													tabindex="0" role="button" class="ql-picker-item ql-primary" data-value="#e60000"
													style="background-color: rgb(230, 0, 0);"></span><span tabindex="0" role="button"
																																								 class="ql-picker-item ql-primary"
																																								 data-value="#ff9900"
																																								 style="background-color: rgb(255, 153, 0);"></span><span
													tabindex="0" role="button" class="ql-picker-item ql-primary" data-value="#ffff00"
													style="background-color: rgb(255, 255, 0);"></span><span tabindex="0" role="button"
																																									 class="ql-picker-item ql-primary"
																																									 data-value="#008a00"
																																									 style="background-color: rgb(0, 138, 0);"></span><span
													tabindex="0" role="button" class="ql-picker-item ql-primary" data-value="#0066cc"
													style="background-color: rgb(0, 102, 204);"></span><span tabindex="0" role="button"
																																									 class="ql-picker-item ql-primary"
																																									 data-value="#9933ff"
																																									 style="background-color: rgb(153, 51, 255);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#ffffff"
													style="background-color: rgb(255, 255, 255);"></span><span tabindex="0" role="button"
																																										 class="ql-picker-item"
																																										 data-value="#facccc"
																																										 style="background-color: rgb(250, 204, 204);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#ffebcc"
													style="background-color: rgb(255, 235, 204);"></span><span tabindex="0" role="button"
																																										 class="ql-picker-item"
																																										 data-value="#ffffcc"
																																										 style="background-color: rgb(255, 255, 204);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#cce8cc"
													style="background-color: rgb(204, 232, 204);"></span><span tabindex="0" role="button"
																																										 class="ql-picker-item"
																																										 data-value="#cce0f5"
																																										 style="background-color: rgb(204, 224, 245);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#ebd6ff"
													style="background-color: rgb(235, 214, 255);"></span><span tabindex="0" role="button"
																																										 class="ql-picker-item"
																																										 data-value="#bbbbbb"
																																										 style="background-color: rgb(187, 187, 187);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#f06666"
													style="background-color: rgb(240, 102, 102);"></span><span tabindex="0" role="button"
																																										 class="ql-picker-item"
																																										 data-value="#ffc266"
																																										 style="background-color: rgb(255, 194, 102);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#ffff66"
													style="background-color: rgb(255, 255, 102);"></span><span tabindex="0" role="button"
																																										 class="ql-picker-item"
																																										 data-value="#66b966"
																																										 style="background-color: rgb(102, 185, 102);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#66a3e0"
													style="background-color: rgb(102, 163, 224);"></span><span tabindex="0" role="button"
																																										 class="ql-picker-item"
																																										 data-value="#c285ff"
																																										 style="background-color: rgb(194, 133, 255);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#888888"
													style="background-color: rgb(136, 136, 136);"></span><span tabindex="0" role="button"
																																										 class="ql-picker-item"
																																										 data-value="#a10000"
																																										 style="background-color: rgb(161, 0, 0);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#b26b00"
													style="background-color: rgb(178, 107, 0);"></span><span tabindex="0" role="button"
																																									 class="ql-picker-item"
																																									 data-value="#b2b200"
																																									 style="background-color: rgb(178, 178, 0);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#006100"
													style="background-color: rgb(0, 97, 0);"></span><span tabindex="0" role="button"
																																								class="ql-picker-item"
																																								data-value="#0047b2"
																																								style="background-color: rgb(0, 71, 178);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#6b24b2"
													style="background-color: rgb(107, 36, 178);"></span><span tabindex="0" role="button"
																																										class="ql-picker-item"
																																										data-value="#444444"
																																										style="background-color: rgb(68, 68, 68);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#5c0000"
													style="background-color: rgb(92, 0, 0);"></span><span tabindex="0" role="button"
																																								class="ql-picker-item"
																																								data-value="#663d00"
																																								style="background-color: rgb(102, 61, 0);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#666600"
													style="background-color: rgb(102, 102, 0);"></span><span tabindex="0" role="button"
																																									 class="ql-picker-item"
																																									 data-value="#003700"
																																									 style="background-color: rgb(0, 55, 0);"></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="#002966"
													style="background-color: rgb(0, 41, 102);"></span><span tabindex="0" role="button"
																																									class="ql-picker-item"
																																									data-value="#3d1466"
																																									style="background-color: rgb(61, 20, 102);"></span></span></span><select
											class="ql-color" style="display: none;"><option selected="selected"></option><option
											value="#e60000"></option><option value="#ff9900"></option><option value="#ffff00"></option><option
											value="#008a00"></option><option value="#0066cc"></option><option value="#9933ff"></option><option
											value="#ffffff"></option><option value="#facccc"></option><option value="#ffebcc"></option><option
											value="#ffffcc"></option><option value="#cce8cc"></option><option value="#cce0f5"></option><option
											value="#ebd6ff"></option><option value="#bbbbbb"></option><option value="#f06666"></option><option
											value="#ffc266"></option><option value="#ffff66"></option><option value="#66b966"></option><option
											value="#66a3e0"></option><option value="#c285ff"></option><option value="#888888"></option><option
											value="#a10000"></option><option value="#b26b00"></option><option value="#b2b200"></option><option
											value="#006100"></option><option value="#0047b2"></option><option value="#6b24b2"></option><option
											value="#444444"></option><option value="#5c0000"></option><option value="#663d00"></option><option
											value="#666600"></option><option value="#003700"></option><option value="#002966"></option><option
											value="#3d1466"></option></select>
                </span>
							<span class="ql-formats">
                  <button class="ql-blockquote" type="button"><svg viewBox="0 0 18 18"> <rect class="ql-fill ql-stroke"
																																															height="3" width="3" x="4"
																																															y="5"></rect> <rect
													class="ql-fill ql-stroke" height="3" width="3" x="11" y="5"></rect> <path
													class="ql-even ql-fill ql-stroke" d="M7,8c0,4.031-3,5-3,5"></path> <path
													class="ql-even ql-fill ql-stroke" d="M14,8c0,4.031-3,5-3,5"></path> </svg></button>
                </span>
							<span class="ql-formats">
                  <button class="ql-list" value="ordered" type="button"><svg viewBox="0 0 18 18"> <line
													class="ql-stroke" x1="7" x2="15" y1="4" y2="4"></line> <line class="ql-stroke" x1="7" x2="15"
																																											 y1="9" y2="9"></line> <line
													class="ql-stroke" x1="7" x2="15" y1="14" y2="14"></line> <line class="ql-stroke ql-thin"
																																												 x1="2.5" x2="4.5" y1="5.5"
																																												 y2="5.5"></line> <path
													class="ql-fill"
													d="M3.5,6A0.5,0.5,0,0,1,3,5.5V3.085l-0.276.138A0.5,0.5,0,0,1,2.053,3c-0.124-.247-0.023-0.324.224-0.447l1-.5A0.5,0.5,0,0,1,4,2.5v3A0.5,0.5,0,0,1,3.5,6Z"></path> <path
													class="ql-stroke ql-thin"
													d="M4.5,10.5h-2c0-.234,1.85-1.076,1.85-2.234A0.959,0.959,0,0,0,2.5,8.156"></path> <path
													class="ql-stroke ql-thin"
													d="M2.5,14.846a0.959,0.959,0,0,0,1.85-.109A0.7,0.7,0,0,0,3.75,14a0.688,0.688,0,0,0,.6-0.736,0.959,0.959,0,0,0-1.85-.109"></path> </svg></button>
                  <button class="ql-list" value="bullet" type="button"><svg viewBox="0 0 18 18"> <line class="ql-stroke"
																																																			 x1="6" x2="15"
																																																			 y1="4"
																																																			 y2="4"></line> <line
													class="ql-stroke" x1="6" x2="15" y1="9" y2="9"></line> <line class="ql-stroke" x1="6" x2="15"
																																											 y1="14" y2="14"></line> <line
													class="ql-stroke" x1="3" x2="3" y1="4" y2="4"></line> <line class="ql-stroke" x1="3" x2="3"
																																											y1="9" y2="9"></line> <line
													class="ql-stroke" x1="3" x2="3" y1="14" y2="14"></line> </svg></button>
                  <button class="ql-indent" value="-1" type="button"><svg viewBox="0 0 18 18"> <line class="ql-stroke"
																																																		 x1="3" x2="15"
																																																		 y1="14"
																																																		 y2="14"></line> <line
													class="ql-stroke" x1="3" x2="15" y1="4" y2="4"></line> <line class="ql-stroke" x1="9" x2="15"
																																											 y1="9" y2="9"></line> <polyline
													class="ql-stroke" points="5 7 5 11 3 9 5 7"></polyline> </svg></button>
                  <button class="ql-indent" value="+1" type="button"><svg viewBox="0 0 18 18"> <line class="ql-stroke"
																																																		 x1="3" x2="15"
																																																		 y1="14"
																																																		 y2="14"></line> <line
													class="ql-stroke" x1="3" x2="15" y1="4" y2="4"></line> <line class="ql-stroke" x1="9" x2="15"
																																											 y1="9" y2="9"></line> <polyline
													class="ql-fill ql-stroke" points="3 7 3 11 5 9 3 7"></polyline> </svg></button>
                </span>
							<span class="ql-formats">
                  <button class="ql-direction" value="rtl" type="button"><svg viewBox="0 0 18 18"> <polygon
													class="ql-stroke ql-fill" points="3 11 5 9 3 7 3 11"></polygon> <line
													class="ql-stroke ql-fill" x1="15" x2="11" y1="4" y2="4"></line> <path class="ql-fill"
																																																d="M11,3a3,3,0,0,0,0,6h1V3H11Z"></path> <rect
													class="ql-fill" height="11" width="1" x="11" y="4"></rect> <rect class="ql-fill" height="11"
																																													 width="1" x="13"
																																													 y="4"></rect> </svg><svg
													viewBox="0 0 18 18"> <polygon class="ql-stroke ql-fill"
																												points="15 12 13 10 15 8 15 12"></polygon> <line
													class="ql-stroke ql-fill" x1="9" x2="5" y1="4" y2="4"></line> <path class="ql-fill"
																																															d="M5,3A3,3,0,0,0,5,9H6V3H5Z"></path> <rect
													class="ql-fill" height="11" width="1" x="5" y="4"></rect> <rect class="ql-fill" height="11"
																																													width="1" x="7" y="4"></rect> </svg></button>
                  <span class="ql-align ql-picker ql-icon-picker"><span class="ql-picker-label" tabindex="0"
																																				role="button" aria-expanded="false"
																																				aria-controls="ql-picker-options-3"><svg
													viewBox="0 0 18 18"> <line class="ql-stroke" x1="3" x2="15" y1="9" y2="9"></line> <line
													class="ql-stroke" x1="3" x2="13" y1="14" y2="14"></line> <line class="ql-stroke" x1="3" x2="9"
																																												 y1="4"
																																												 y2="4"></line> </svg></span><span
													class="ql-picker-options" aria-hidden="true" tabindex="-1" id="ql-picker-options-3"><span
													tabindex="0" role="button" class="ql-picker-item ql-selected"><svg viewBox="0 0 18 18"> <line
													class="ql-stroke" x1="3" x2="15" y1="9" y2="9"></line> <line class="ql-stroke" x1="3" x2="13"
																																											 y1="14" y2="14"></line> <line
													class="ql-stroke" x1="3" x2="9" y1="4" y2="4"></line> </svg></span><span tabindex="0"
																																																	 role="button"
																																																	 class="ql-picker-item"
																																																	 data-value="center"><svg
													viewBox="0 0 18 18"> <line class="ql-stroke" x1="15" x2="3" y1="9" y2="9"></line> <line
													class="ql-stroke" x1="14" x2="4" y1="14" y2="14"></line> <line class="ql-stroke" x1="12"
																																												 x2="6" y1="4"
																																												 y2="4"></line> </svg></span><span
													tabindex="0" role="button" class="ql-picker-item" data-value="right"><svg viewBox="0 0 18 18"> <line
													class="ql-stroke" x1="15" x2="3" y1="9" y2="9"></line> <line class="ql-stroke" x1="15" x2="5"
																																											 y1="14" y2="14"></line> <line
													class="ql-stroke" x1="15" x2="9" y1="4" y2="4"></line> </svg></span><span tabindex="0"
																																																		role="button"
																																																		class="ql-picker-item"
																																																		data-value="justify"><svg
													viewBox="0 0 18 18"> <line class="ql-stroke" x1="15" x2="3" y1="9" y2="9"></line> <line
													class="ql-stroke" x1="15" x2="3" y1="14" y2="14"></line> <line class="ql-stroke" x1="15"
																																												 x2="3" y1="4"
																																												 y2="4"></line> </svg></span></span></span><select
											class="ql-align" style="display: none;"><option selected="selected"></option><option
											value="center"></option><option value="right"></option><option value="justify"></option></select>
                </span>
						</div>
					</div>
					<textarea name="text" id="text" hidden></textarea>
					<div id="editor" class="ql-container ql-snow"></div>
				</form>
				<button id="submit_btn" class="btn btn-primary btn-pill mb-5" type="submit">보내기</button>
			<%--				<div>--%>
				<%--					<main>--%>
				<%--						<form action="/api/email/sending" method="post">--%>
				<%--							<div>--%>
				<%--								<input type="text" name="to" id="to">--%>
				<%--							</div>--%>
				<%--							<div>--%>
				<%--								<input type="text" name="subject" id="subject">--%>
				<%--							</div>--%>
				<%--							<textarea name="text" id="text" cols="30" rows="10"></textarea>--%>
				<%--							<button class="btn btn-primary btn-pill mb-5">보내기</button>--%>
				<%--						</form>--%>
				<%--					</main>--%>
				<%--				</div>--%>
			</div>
		</main>
	</div>
</div>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/email/sending.js"></script>
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>