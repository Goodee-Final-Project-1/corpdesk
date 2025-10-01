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

<!-- 일정 등록 모달 -->
<div class="modal fade" id="scheduleModal" tabindex="-1" role="dialog" aria-labelledby="scheduleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="scheduleModalLabel">일정 등록</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <form method="POST" action="/personal-schedule">
        <div class="modal-body">
          <%-- TODO 인증 붙이면 input hidden 삭제 --%>
          <input type="hidden" name="username" value="jung_frontend">

          <div class="form-group">
            <label for="scheduleName">일정명</label>
            <input type="text" class="form-control" id="scheduleName" name="scheduleName" placeholder="일정명을 입력하세요">
          </div>
          <div class="form-group">
            <label for="scheduleDateTime">일시</label>
            <input type="datetime-local" class="form-control" id="scheduleDateTime" name="scheduleDateTime">
          </div>
          <div class="form-group">
            <label for="scheduleLocation">주소</label>
            <input type="text" class="form-control" id="scheduleLocation" name="address" placeholder="주소를 입력하세요">
          </div>
          <div class="form-group">
            <label for="scheduleContent">내용</label>
            <textarea class="form-control" id="scheduleContent" name="content" rows="3" placeholder="내용을 입력하세요"></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-primary">등록</button>
          <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        </div>
      </form>
    </div>
  </div>
</div>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 내용 시작 -->

<div class="row">
  <!-- 왼쪽 사이드바 -->
  <div class="col-lg-3">
    <div class="card card-default">
      <div class="card-body">
        <button type="button" class="btn btn-info btn-lg btn-block mb-7" data-toggle="modal" data-target="#scheduleModal">
          일정 등록
        </button>


        <h5  class="mb-4">오늘의 일정</h5>
        <div class="list-group list-group-flush">
          <a href="#" class="list-group-item list-group-item-action border-0 px-0 py-2">
            <div class="d-flex align-items-start">
              <i class="mdi mdi-checkbox-blank-circle text-primary mr-3"></i>
              <div>
                <div class="font-weight-semibold text-dark">제목</div>
                <small class="text-muted">주소</small>
              </div>
            </div>
          </a>

          <a href="#" class="list-group-item list-group-item-action border-0 px-0 py-2">
            <div class="d-flex align-items-start">
              <i class="mdi mdi-checkbox-blank-circle text-success mr-3"></i>
              <div>
                <div class="font-weight-semibold text-dark">10:00&nbsp;&nbsp;제목</div>
                <small class="text-muted">주소</small>
              </div>
            </div>
          </a>
        </div>

        </div>
      </div>
    </div>

  <!-- 오른쪽 메인 콘텐츠 -->
  <div class="col-lg-9">
    <div class="card card-default">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h2 class="mb-0">일정 목록</h2>
      </div>

      <div class="card-body">

        <!-- 년, 월 선택 - 한 줄 배치 -->
        <div class="row mb-4 ml-0">

          <%-- TODO 추후 인증정보를 사용하게 되면 username 삭제 --%>
          <form method="get" action="/attendance/list">

            <input type="hidden" name="username" value="jung_frontend">

            <div class="mr-3">
              <div class="form-group">
                <div class="d-flex align-items-center">
                  <select class="form-control mr-2" id="yearSelect" name="year">

                    <c:choose>
                      <c:when test="${selectedYear ne null}">
                        <option value="" ${selectedYear eq '' ? 'selected' : ''}>전체</option>
                        <c:forEach var="year" items="${yearRange}">
                          <option value="${year}" ${selectedYear eq year ? 'selected' : ''}>
                              ${year}
                          </option>
                        </c:forEach>
                      </c:when>
                      <c:otherwise>
                        <option value="">전체</option>
                        <c:forEach var="year" items="${yearRange}">
                          <option value="${year}">
                              ${year}
                          </option>
                        </c:forEach>
                      </c:otherwise>
                    </c:choose>

                  </select>
                  <span>년</span>
                </div>
              </div>
            </div>

            <div class="mr-6">
              <div class="form-group">
                <div class="d-flex align-items-center">
                  <select class="form-control mr-2" id="monthSelect" name="month">

                    <c:choose>
                      <c:when test="${selectedMonth ne null}">
                        <option value="" ${selectedMonth eq '' ? 'selected' : ''}>전체</option>
                        <c:forEach begin="1" end="12" var="num">
                          <option value="${num}" ${selectedMonth eq num ? 'selected' : ''}>${num}</option>
                        </c:forEach>
                      </c:when>

                      <c:otherwise>
                        <option value="">전체</option>
                        <c:forEach begin="1" end="12" var="num">
                          <option value="${num}">${num}</option>
                        </c:forEach>
                      </c:otherwise>
                    </c:choose>

                  </select>
                  <span>월</span>
                </div>
              </div>
            </div>

            <div>
              <%-- TODO 조회 버튼 클릭시 년, 월 데이터로 페이지 다시 조회 --%>
              <button class="btn btn-primary">조회</button>
            </div>

          </form>
        </div>

        <%-- 테이블 --%>
        <table class="table table-hover">
          <thead>
            <tr>
              <th>날짜</th>
              <th>시간</th>
              <th>일정명</th>
              <th>주소</th>
              <th>내용</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>2025-09-08</td>
              <td>09:00</td>
              <td>일정명</td>
              <td>주소</td>
              <td>내용</td>
            </tr>
            <tr>
              <td>2025-09-08</td>
              <td>09:00</td>
              <td>일정명</td>
              <td>주소</td>
              <td>내용</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>