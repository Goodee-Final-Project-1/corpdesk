<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- Header -->
<header class="main-header" id="header">
  <nav class="navbar navbar-expand-lg navbar-light" id="navbar">
    <!-- Sidebar toggle button -->
    <button id="sidebar-toggler" class="sidebar-toggle">
      <span class="sr-only">Toggle navigation</span>
    </button>

    <span class="page-title">
    	<!-- TODO 여기에 카테고리명 입력 -->
    	<span>여기에 카테고리명 입력</span>
    </span>

    <div class="navbar-right ">

      <ul class="nav navbar-nav">
        <!-- Offcanvas -->
        <li class="custom-dropdown">
          <button class="notify-toggler custom-dropdown-toggler">
            <i class="mdi mdi-bell-outline icon"></i>
            <span class="badge badge-xs rounded-circle">21</span>
          </button>
          <div class="dropdown-notify">
						
						<!-- 알림 헤더 -->
            <header>
              <div class="nav nav-underline" id="nav-tab" role="tablist">
                <a class="nav-item nav-link active" id="all-tabs" data-toggle="tab" href="#all" role="tab" aria-controls="nav-home"
                  aria-selected="true">전체 (5)</a> <!-- TODO 추후 사용시 () 안에 실제 테이터 넣기 -->
                <a class="nav-item nav-link" id="message-tab" data-toggle="tab" href="#message" role="tab" aria-controls="nav-profile"
                  aria-selected="false">메시지 (4)</a> <!-- TODO 추후 사용시 () 안에 실제 테이터 넣기 -->
                <a class="nav-item nav-link" id="other-tab" data-toggle="tab" href="#other" role="tab" aria-controls="nav-contact"
                  aria-selected="false">기타 (1)</a> <!-- TODO 추후 사용시 () 안에 실제 테이터 넣기 -->
              </div>
            </header>
            <!--  -->
						
						<!-- 알림 내용 -->
						<!-- TODO 알림 기능 화면 구현시, 아래의 샘플들을 참고하여 구현 -->
            <div class="" data-simplebar style="height: 325px;">
              <div class="tab-content" id="myTabContent">
								
								<!-- 전체 알림 -->
                <div class="tab-pane fade show active" id="all" role="tabpanel" aria-labelledby="all-tabs">

                  <div class="media media-sm p-4 mb-0">
                    <div class="media-sm-wrapper">
                      <a href="/employee/detail"> <!-- TODO 추후 href 변경 -->
                        <img src="/images/user/user-sm-03.jpg" alt="User Image">
                      </a>
                    </div>
                    <div class="media-body">
                      <a href="/employee/detail"> <!-- TODO 추후 href 변경 -->
                        <span class="title mb-0">Sagge Hudson</span>
                        <span class="discribe">On disposal of as landlord Afraid at highly months do things on at.</span>
                        <span class="time">
                          <time>1 hrs ago</time>...
                        </span>
                      </a>
                    </div>
                  </div>

                  <div class="media media-sm p-4 mb-0">
                    <div class="media-sm-wrapper bg-info-dark">
                      <a href="/employee/detail"> <!-- TODO 추후 href 변경 -->
                        <i class="mdi mdi-account-multiple-check"></i>
                      </a>
                    </div>
                    <div class="media-body">
                      <a href="/employee/detail"> <!-- TODO 추후 href 변경 -->
                        <span class="title mb-0">Add request</span>
                        <span class="discribe">Add Dany Jones as your contact.</span>
                        <span class="time">
                          <time>6 hrs ago</time>...
                        </span>
                      </a>
                    </div>
                  </div>

                </div>
								<!--  -->
								
								<!-- 메시지 알림 -->
                <div class="tab-pane fade" id="message" role="tabpanel" aria-labelledby="message-tab">

                  <div class="media media-sm p-4 mb-0">
                    <div class="media-sm-wrapper">
                      <a href="/employee/detail"> <!-- TODO 추후 href 변경 -->
                        <img src="/images/user/user-sm-04.jpg" alt="User Image">
                      </a>
                    </div>
                    <div class="media-body">
                      <a href="/employee/detail"> <!-- TODO 추후 href 변경 -->
                        <span class="title mb-0">Albrecht Straub</span>
                        <span class="discribe"> Beatae quia natus assumenda laboriosam, nisi perferendis aliquid consectetur expedita non tenetur.</span>
                        <span class="time">
                          <time>Just now</time>...
                        </span>
                      </a>
                    </div>
                  </div>

                </div>
                <!--  -->
                
                <!-- 기타 알림 -->
                <div class="tab-pane fade" id="other" role="tabpanel" aria-labelledby="contact-tab">

                  <div class="media media-sm p-4 mb-0">
                    <div class="media-sm-wrapper bg-info-dark">
                      <a href="/employee/detail">
                        <i class="mdi mdi-account-multiple-check"></i>
                      </a>
                    </div>
                    <div class="media-body">
                      <a href="/employee/detail"> <!-- TODO 추후 href 변경 -->
                        <span class="title mb-0">Add request</span>
                        <span class="discribe">Add Dany Jones as your contact.</span>
                        <span class="time">
                          <time>6 hrs ago</time>...
                        </span>
                      </a>
                    </div>
                  </div>

                </div>
              	<!--  -->
              </div>
            </div>
						<!--  -->
						
          </div>
        </li>
        <!-- User Account -->
        <li class="dropdown user-menu">
          <button class="dropdown-toggle nav-link" data-toggle="dropdown">
            <img src="/images/user/user-xs-01.jpg" class="user-image rounded-circle" alt="User Image" /> <!-- TODO 추후 사용시 src 변경 -->
            <span class="d-none d-lg-inline-block">이름</span>
          </button>
          <ul class="dropdown-menu dropdown-menu-right">
            <li>
              <a class="dropdown-link-item" href="/employee/detail"> <!-- TODO 추후 사용시 href 변경 -->
                <i class="mdi mdi-account-outline"></i>
                <span class="nav-text">내 정보</span>
              </a>
            </li>

            <li class="dropdown-footer">
              <a class="dropdown-link-item" href="sign-in.html"> <!-- TODO 추후 사용시 href 변경 -->
              	<i class="mdi mdi-logout"></i> 로그아웃
              </a>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </nav>


</header>