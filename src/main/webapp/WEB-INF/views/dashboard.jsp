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

      <div class="row">

        <!-- 좌측 사이드 영역 -->
        <div class="col-lg-3">

          <!-- 고투현 사원 카드 -->
          <div class="card card-default mb-3">
            <div class="card-body text-center">
              <div class="mb-3">
                <img src="https://via.placeholder.com/80" class="rounded-circle" alt="프로필">
              </div>
              <h5 class="mb-1">고투현 사원</h5>
              <p class="text-muted mb-3">경영부</p>
              <hr>
              <div class="d-flex justify-content-around mb-2">
                <div>
                  <h6 class="mb-0">오늘의 일정</h6>
                  <h4 class="mb-0">1</h4>
                </div>
              </div>
              <hr>
              <div class="text-left small">
                <div class="d-flex justify-content-between mb-2">
                  <span>게시판 새 글</span>
                  <span>1</span>
                </div>
                <div class="d-flex justify-content-between mb-2">
                  <span>결재 대기 문서</span>
                  <span>1</span>
                </div>
                <div class="d-flex justify-content-between">
                  <span>진여 연차</span>
                  <span>1일</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 근태 카드 -->
          <div class="card card-default">
            <div class="card-body">
              <h6 class="mb-3">근태</h6>
              <div class="d-flex justify-content-around mb-3">
                <div class="text-center">
                  <p class="mb-1 small">출근</p>
                  <p class="mb-0 font-weight-bold">00:00</p>
                </div>
                <div class="text-center">
                  <p class="mb-1 small">퇴근</p>
                  <p class="mb-0 font-weight-bold">00:00</p>
                </div>
              </div>
              <div class="row no-gutters">
                <div class="col-6 pr-1">
                  <button class="btn btn-dark btn-block">출근</button>
                </div>
                <div class="col-6 pl-1">
                  <button class="btn btn-dark btn-block">퇴근</button>
                </div>
              </div>
            </div>
          </div>

        </div>

        <!-- 메인 콘텐츠 영역 -->
        <div class="col-lg-9">

          <div class="row mb-3">

            <!-- 결재 대기 문서 -->
            <div class="col-lg-6 mb-3">
              <div class="card card-default h-100">
                <div class="card-body">
                  <h5 class="mb-4">결재 대기 문서</h5>
                  <div class="bg-light p-5 text-center">
                    <p class="text-muted">(결재 대기 문서 목록)</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 공지사항 -->
            <div class="col-lg-6 mb-3">
              <div class="card card-default h-100">
                <div class="card-body">
                  <h5 class="mb-4">공지사항</h5>
                  <div class="bg-light p-5 text-center">
                    <p class="text-muted">(공지사항 목록)</p>
                  </div>
                </div>
              </div>
            </div>

          </div>

          <div class="row">

            <!-- 휴가 -->
            <div class="col-lg-6 mb-3">
              <div class="card card-default h-100">
                <div class="card-body">
                  <h5 class="mb-4">휴가</h5>
                  <div class="bg-light p-4">
                    <div class="row text-center">
                      <div class="col-4">
                        <p class="small mb-1 text-warning">잔여 연차</p>
                        <h5 class="text-warning mb-0">-20d</h5>
                      </div>
                      <div class="col-4">
                        <p class="small mb-1">사용 연차</p>
                        <h5 class="mb-0">39d</h5>
                      </div>
                      <div class="col-4">
                        <p class="small mb-1">총 연차</p>
                        <h5 class="mb-0">19d</h5>
                      </div>
                    </div>
                    <p class="text-center text-muted small mt-3 mb-0">(예정된 휴가 목록)</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 외부일정 -->
            <div class="col-lg-6 mb-3">
              <div class="card card-default h-100">
                <div class="card-body">
                  <h5 class="mb-4">외부일정</h5>
                  <div class="bg-light p-5 text-center">
                    <p class="text-muted">(캘린더)</p>
                  </div>
                </div>
              </div>
            </div>

          </div>

        </div>

      </div>

			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>