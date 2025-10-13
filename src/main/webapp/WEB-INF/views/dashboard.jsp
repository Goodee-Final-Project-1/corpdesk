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

  <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${appkey}"></script>
  <script type="text/javascript" src="/js/dashboard/map.js"></script>

<%--  <style>--%>
<%--      .customoverlay {position:relative;bottom:85px;border-radius:6px;border: 1px solid #ccc;border-bottom:2px solid #ddd;float:left;}--%>
<%--      .customoverlay:nth-of-type(n) {border:0; box-shadow:0px 1px 2px #888;}--%>
<%--      .customoverlay a {display:block;text-decoration:none;color:#000;text-align:center;border-radius:6px;font-size:14px;font-weight:bold;overflow:hidden;background: #d95050;background: #d95050 url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/arrow_white.png) no-repeat right 14px center;}--%>
<%--      .customoverlay .title {display:block;text-align:center;background:#fff;margin-right:35px;padding:10px 15px;font-size:14px;font-weight:bold;}--%>
<%--      .customoverlay:after {content:'';position:absolute;margin-left:-12px;left:50%;bottom:-12px;width:22px;height:12px;background:url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png')}--%>
<%--  </style>--%>
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
                  <h3 class="mb-0">${personalSchedule.todayScheduleCnt}</h3>
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
                  <span>${approval.approvalCnt}</span>
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

              <c:import url="/WEB-INF/views/attendance/attendance_widget.jsp"/>

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

                  <div class="email-details-content pl-0 pr-0 pt-0">
                    <table class="table">

                      <thead>
                      <tr>
                        <th class="col-2">기안일</th>
                        <th class="col-4">제목</th>
                        <th class="col-2">기안부서</th>
                        <th class="col-2">기안자</th>
                      </tr>
                      </thead>

                      <tbody>
                      <c:forEach items="${approval.approvals }" var="el">
                        <tr class="approval-row" onclick="location.href='/approval/${el.approvalId}'" style="cursor: pointer;">
                          <td>${fn:substring(el.createdAt, 0, 10) }</td>
                          <td>${el.formTitle }</td>
                          <td>${el.departmentName }</td>
                          <td>${el.username }</td>
                        </tr>
                      </c:forEach>
                      </tbody>

                    </table>
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
                        <h5 class="text-warning mb-0">${vacation.remainingVacation eq null ? 0 : vacation.remainingVacation}일</h5>
                      </div>
                      <div class="col-4">
                        <p class="mb-1">사용 연차</p>
                        <h5 class="mb-0">${vacation.usedVacation eq null ? 0 : vacation.usedVacation}일</h5>
                      </div>
                      <div class="col-4">
                        <p class="mb-1">총 연차</p>
                        <h5 class="mb-0">${vacation.totalVacation eq null ? 0 : vacation.totalVacation}일</h5>
                      </div>
                    </div>
                    <br>
<%--                    <p class="text-center text-muted mb-0">(예정된 휴가 목록)</p>--%>
                  </div>
                </div>
              </div>
            </div>

            <!-- 외부일정 -->
            <div class="col-lg-6">
              <div class="card card-default">
                <div class="card-header">
                  <h2>외부일정 장소</h2>
                </div>
                <div class="card-body">
                  <div id="map" style="width:100%;height:400px;"></div>
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