<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<aside class="left-sidebar sidebar-light" id="left-sidebar">
	<div id="sidebar" class="sidebar sidebar-with-footer">
		<!-- Aplication Brand -->
		<div class="app-brand">
			<a href="/dashboard"> <img src="/images/logo.png" alt="Corpdesk">
				<span class="brand-name">Corpdesk</span>
			</a>
		</div>
		<!-- begin sidebar scrollbar -->
		<div class="sidebar-left" data-simplebar style="height: 100%;">
			<!-- sidebar menu -->
			<ul class="nav sidebar-inner" id="sidebar-menu">
				
				<!-- 홈 -->
				<li class="active">
					<a class="sidenav-item-link" href="/dashboard">
						<i class="mdi mdi-home"></i> <span class="nav-text">홈</span>
					</a>
				</li>

				<li class="section-title pt-0 pb-0"><hr></li>
				<!--  -->
				
				<!-- 메신저, 메일, 전자결재, 캘린더, 게시판 -->
				<li>
					<a class="sidenav-item-link" href="/chat/room/list"> <!-- TODO href 수정 -->
						<i class="mdi mdi-wechat"></i> <span class="nav-text">메신저</span>
					</a>
				</li>
				
				<li>
					<a class="sidenav-item-link" href="/email/received"> <!-- TODO href 수정 -->
							<i class="mdi mdi-email"></i> <span class="nav-text">메일</span>
					</a>
				</li>

				<li>
					<a class="sidenav-item-link" href="/approval/list?username=jung_frontend"> <!-- TODO href 수정 -->
						<i class="mdi mdi-pencil-box-outline"></i> <span class="nav-text">전자결재</span>
					</a>
				</li>
				
				<li>
					<a class="sidenav-item-link" href="/calendar/list"> <!-- TODO href 수정 -->
					<i class="mdi mdi-calendar-check"></i> <span class="nav-text">캘린더</span>
					</a>
				</li>
				
				<li>
					<a class="sidenav-item-link" href="calendar.html"> <!-- TODO href 수정 -->
					<i class="mdi mdi-clipboard-text-outline"></i> <span class="nav-text">게시판</span>
					</a>
				</li>
				<!--  -->

				<li class="section-title pt-0 pb-0"><hr></li>

				<!-- 근태, 휴가, 일정 -->
				<li>
					<a class="sidenav-item-link" href="/attendance/list?username=jung_frontend"> <%-- TODO 쿼리스트링 제거 --%>
					<i class="mdi mdi-account-clock"></i> <span class="nav-text">근태</span>
					</a>
				</li>
				
				<li>
					<a class="sidenav-item-link" href="calendar.html"> <!-- TODO href 수정 -->
						<i class="mdi mdi-beach"></i> <span class="nav-text">휴가</span>
					</a>
				</li>
				
				<li>
					<a class="sidenav-item-link" href="/personal-schedule/list?username=jung_frontend"> <%-- TODO 쿼리스트링 제거 --%>
						<i class="mdi mdi-alarm-check"></i> <span class="nav-text">일정</span>
					</a>
				</li>
				<!--  -->

				<li class="section-title pt-0 pb-0"><hr></li>

				<!-- 조직관리, 인사관리 -->

				<li class="has-sub">
					<a class="sidenav-item-link" href="javascript:void(0)"
					data-toggle="collapse" data-target="#hr" aria-expanded="false">
						<i class="mdi mdi-account-group"></i> <span class="nav-text">조직관리</span>
						<b class="caret"></b>
					</a>

					<ul class="collapse" id="hr" data-parent="#sidebar-menu">
						<div class="sub-menu">

							<li>
								<a class="sidenav-item-link" href="/organization/list"> <!-- TODO href 수정 -->
										<span class="nav-text">조직설계</span>
								</a>
							</li>

							<li>
								<a class="sidenav-item-link" href="email-details.html"> <!-- TODO href 수정 -->
										<span class="nav-text">직위체계</span>
								</a>
							</li>

						</div>
					</ul>
				</li>

				<li>
					<a class="sidenav-item-link" href="/employee/list"> <!-- TODO href 수정 -->
						<i class="mdi mdi-account-details"></i> <span class="nav-text">인사관리</span>
					</a>
				</li>
				<li>
					<a class="sidenav-item-link" href="/salary"> <!-- TODO href 수정 -->
						<i class="mdi mdi-currency-krw"></i> <span class="nav-text">급여 관리</span>
					</a>
				</li>

			</ul>
		</div>
	</div>
</aside>