<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Corpdesk</title>

	<c:import url="/WEB-INF/views/include/head.jsp"/>

  <%-- Kakao Map API --%>
  <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${appkey}"></script>
  <script type="text/javascript" src="/js/dashboard/map.js"></script>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>

		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
      <div class="row">

        <%-- 1. 좌측 사이드 --%>
        <aside class="col-lg-3">

          <%-- 1) 사원 카드 --%>
          <section class="card card-default mb-4">
            <div class="card-body text-center p-4">
              <div class="mb-3">
                <c:choose>
                  <c:when test="${employee.saveName eq null}">
                    <img src="/images/default_profile.jpg" class="rounded-circle p-4 w-100" alt="프로필">
                  </c:when>
                  <c:otherwise>
                    <img src="/files/profile/${employee.saveName}.${employee.extension}" class="rounded-circle" alt="프로필">
                  </c:otherwise>
                </c:choose>
              </div>
              <h5 class="mb-1">${employee.name} ${employee.positionName}</h5>
              <p class="text-muted mb-4">${employee.departmentName}</p>
<%--              <hr>--%>
<%--              <div class="mb-3 d-flex justify-content-center">--%>
<%--                <div onclick="location.href='/personal-schedule/list'" style="cursor: pointer">--%>
<%--                  <h6 class="mb-2">오늘의 일정</h6>--%>
<%--                  <h3 class="mb-0">${personalSchedule.todayScheduleCnt}</h3>--%>
<%--                </div>--%>
<%--              </div>--%>
<%--              <hr>--%>
              <div class="text-left">
                <%-- TODO 아래 주석처리한 기능은 추가할 수 있으면 추가... --%>
<%--                <div class="d-flex justify-content-between mb-2">--%>
<%--                  <span>게시판 새 글</span>--%>
<%--                  <span>1</span>--%>
<%--                </div>--%>
                <div class="d-flex justify-content-between mb-2 align-items-center">
                  <span>일정</span>
                  <span class="${personalSchedule.todayScheduleCnt > 0 ? ' text-primary' : ''}">${personalSchedule.todayScheduleCnt}</span>
                </div>
                <div class="d-flex justify-content-between mb-2 align-items-center">
                  <span>결재 대기</span>
                  <span class="${approval.approvalCnt > 0 ? ' text-primary' : ''}">${approval.approvalCnt}</span>
                </div>
                <div class="d-flex justify-content-between align-items-center">
                  <span>잔여 연차</span>
                  <span class="${remainingVacation eq null and remainingVacation > 0 ? ' text-primary' : ''}">${remainingVacation eq null ? 0 : remainingVacation}일</span>
                </div>
              </div>
            </div>
          </section>

          <%-- 2) 근태 카드 --%>
          <section class="card card-default mb-4">
            <div class="card-header">
              <h2>근태</h2>
            </div>
            <div class="card-body">
              <c:import url="/WEB-INF/views/attendance/attendance_widget.jsp"/>
            </div>
          </section>

        </aside>

        <%-- 2. 메인 콘텐츠 영역 --%>
        <main class="col-lg-9">

          <%-- 1) 결재 대기 문서, 공지사항 --%>
          <section class="row">

            <%-- 1-1) 결재 대기 문서 --%>
            <article class="col-lg-6 mb-4">
              <div class="card card-default h-100">
                <div class="card-header">
                  <h2>결재 대기 문서</h2>
                </div>
                <div class="card-body">

                  <div class="email-details-content pl-0 pr-0 pt-0">
                    <table class="table">

                      <thead>
                      <tr>
                        <th class="col-2">기안일</th>
                        <th class="col-4">제목</th>
                      </tr>
                      </thead>

                      <tbody>
                      <c:forEach items="${approval.approvals }" var="el" begin="0" end="4">
                        <tr class="approval-row" onclick="location.href='/approval/${el.approvalId}'" style="cursor: pointer;">
                          <td>${fn:substring(el.createdAt, 0, 10) }</td>
                          <td>${el.formTitle }</td>
                        </tr>
                      </c:forEach>
                      </tbody>

                    </table>
                  </div>

                </div>
              </div>
            </article>

            <%-- 1-2) 공지사항 --%>
            <article class="col-lg-6 mb-4">
              <div class="card card-default h-100">
                <div class="card-header">
                  <h2>공지사항</h2>
                </div>
                <div class="card-body">
                  <ul class="list-unstyled mb-0">
                    <li class="mb-2">• 연말정산 안내</li>
                    <li class="mb-2">• 신년 워크샵 공지</li>
                    <li class="mb-0">• 보안 교육 안내</li>
                  </ul>
<%--                  <div class="p-5 text-center">--%>
<%--                    <p class="text-muted">(공지사항 목록)</p>--%>
<%--                  </div>--%>
                </div>
              </div>
            </article>

          </section>

          <%-- 2) 오늘의 일정, 휴가 --%>
          <section class="row">

            <!-- 2-1) 오늘의 일정 -->
            <article class="col-lg-7 mb-4">
              <div class="card card-default">
                <div class="card-header d-flex justify-content-between align-items-center">
                  <h2>오늘의 일정</h2>
                  <span class="badge badge-primary">2건</span>
                </div>
                <div class="card-body">
                  <div id="map" style="width:100%;height:250px;"></div>
                  <div class="p-3">
                    <div class="schedule-item">
                      <h6 class="mb-1 small">📍 10:00 - 클라이언트 미팅</h6>
                      <div class="d-flex justify-content-between align-items-center">
                        <p class="text-muted small mb-0">강남구 테헤란로 123</p>
                        <a href="#" class="btn btn-sm btn-outline-primary btn-sm">길찾기</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </article>

            <%-- 2-2) 휴가 --%>
            <article class="col-lg-5 mb-4">
              <div class="card card-default">
                <div class="card-header">
                  <h2>휴가</h2>
                </div>
                <div class="card-body">
<%--                  <div class="p-4">--%>
<%--                    <div class="row text-center">--%>
<%--                      <div class="col-4">--%>
<%--                        <p class="mb-1 text-warning">잔여 연차</p>--%>
<%--                        <h5 class="text-warning mb-0">${vacation.remainingVacation eq null ? 0 : vacation.remainingVacation}일</h5>--%>
<%--                      </div>--%>
<%--                      <div class="col-4">--%>
<%--                        <p class="mb-1">사용 연차</p>--%>
<%--                        <h5 class="mb-0">${vacation.usedVacation eq null ? 0 : vacation.usedVacation}일</h5>--%>
<%--                      </div>--%>
<%--                      <div class="col-4">--%>
<%--                        <p class="mb-1">총 연차</p>--%>
<%--                        <h5 class="mb-0">${vacation.totalVacation eq null ? 0 : vacation.totalVacation}일</h5>--%>
<%--                      </div>--%>
<%--                    </div>--%>
<%--                    <br>--%>
<%--                    &lt;%&ndash;                    <p class="text-center text-muted mb-0">(예정된 휴가 목록)</p>&ndash;%&gt;--%>
<%--                  </div>--%>

                  <%-- temp --%>
                  <div class="row text-center">
                    <div class="col-12 mb-3">
                      <p class="text-muted mb-1">잔여 연차</p>
                      <h4 class="text-warning mb-0">${vacation.remainingVacation eq null ? 0 : vacation.remainingVacation}일</h4>
                    </div>
                    <div class="col-6">
                      <p class="text-muted mb-1">사용</p>
                      <h6 class="mb-0">${vacation.usedVacation eq null ? 0 : vacation.usedVacation}일</h6>
                    </div>
                    <div class="col-6">
                      <p class="text-muted mb-1">총</p>
                      <h6 class="mb-0">${vacation.totalVacation eq null ? 0 : vacation.totalVacation}일</h6>
                    </div>
                  </div>
                </div>
              </div>
            </article>

          </section>

        </main>

      </div>

			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>