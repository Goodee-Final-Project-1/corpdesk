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
          <div class="card card-default mb-4">
            <div class="card-body text-center p-4">
              <div class="mb-3">
                <c:choose>
                  <c:when test="${employee.saveName eq null}">
                    <img src="/images/default_profile.jpg" class="rounded-circle" alt="프로필">
                  </c:when>
                  <c:otherwise>
                    <img src="/files/employee/${employee.saveName}.${employee.extension}" class="rounded-circle" alt="프로필">
                  </c:otherwise>
                </c:choose>
              </div>
              <h5 class="mb-1">${employee.name} ${employee.positionName}</h5>
              <p class="text-muted mb-4">${employee.departmentName}</p>
              <hr>
              <div class="mb-3 d-flex justify-content-center">
                <div onclick="location.href='/personal-schedule/list'" style="cursor: pointer">
                  <h6 class="mb-2">오늘의 일정</h6>
                  <h3 class="mb-0">${todayScheduleCnt}</h3>
                </div>
              </div>
              <hr>
              <div class="text-left">
                <%-- TODO 아래 주석처리한 기능은 추가할 수 있으면 추가... --%>
<%--                <div class="d-flex justify-content-between mb-2">--%>
<%--                  <span>게시판 새 글</span>--%>
<%--                  <span>1</span>--%>
<%--                </div>--%>
                <div class="d-flex justify-content-between mb-2">
                  <span>결재 대기 문서</span>
                  <span>${reqApprovalCnt}</span>
                </div>
                <div class="d-flex justify-content-between">
                  <span>진여 연차</span>
                  <span>${remainingVacation eq null ? 0 : remainingVacation}일</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 근태 카드 -->
          <div class="card card-default">
            <div class="card-body">
              <h5 class="mb-3">근태</h5>

              <!-- 출퇴근 시간 표시 -->
              <div class="card card-default mb-3">
                <div class="card-body d-flex justify-content-around p-3">
                  <div class="text-center">
                    <p class="card-text mb-1">출근</p>
                    <p class="card-text mb-0">00:00:00</p>
                  </div>
                  <div class="text-center">
                    <p class="card-text mb-1">퇴근</p>
                    <p class="card-text mb-0">00:00:00</p>
                  </div>
                </div>
              </div>

              <!-- 출근/퇴근 버튼 -->
              <div class="row no-gutters">
                <div class="col-6 pr-2">
                  <button class="btn btn-info btn-lg btn-block">출근</button>
                </div>
                <div class="col-6 pl-2">
                  <button class="btn btn-info btn-lg btn-block">퇴근</button>
                </div>
              </div>
            </div>
          </div>

        </div>

        <!-- 메인 콘텐츠 영역 -->
        <div class="col-lg-9">

          <div class="row">

            <!-- 결재 대기 문서 -->
            <div class="col-lg-6">
              <div class="card card-default">
                <div class="card-header">
                  <h2>결재 대기 문서</h2>
                </div>
                <div class="card-body">
                  <div class="p-5 text-center">
                    <p class="text-muted">(결재 대기 문서 목록)</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 공지사항 -->
            <div class="col-lg-6">
              <div class="card card-default">
                <div class="card-header">
                  <h2>공지사항</h2>
                </div>
                <div class="card-body">
                  <div class="p-5 text-center">
                    <p class="text-muted">(공지사항 목록)</p>
                  </div>
                </div>
              </div>
            </div>

          </div>

          <br>

          <div class="row">

            <!-- 휴가 -->
            <div class="col-lg-6">
              <div class="card card-default">
                <div class="card-header">
                  <h2>휴가</h2>
                </div>
                <div class="card-body">
                  <div class="p-4">
                    <div class="row text-center">
                      <div class="col-4">
                        <p class="mb-1 text-warning">잔여 연차</p>
                        <h5 class="text-warning mb-0">-20d</h5>
                      </div>
                      <div class="col-4">
                        <p class="mb-1">사용 연차</p>
                        <h5 class="mb-0">39d</h5>
                      </div>
                      <div class="col-4">
                        <p class="mb-1">총 연차</p>
                        <h5 class="mb-0">19d</h5>
                      </div>
                    </div>
                    <br>
                    <p class="text-center text-muted mb-0">(예정된 휴가 목록)</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 외부일정 -->
            <div class="col-lg-6">
              <div class="card card-default">
                <div class="card-header">
                  <h2>외부일정</h2>
                </div>
                <div class="card-body">
                  <div class="p-5 text-center">
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