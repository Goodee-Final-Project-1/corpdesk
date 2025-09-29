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

<div class="row">

  <!-- 왼쪽 사이드바 영역 -->
  <div class="col-lg-3">
    <div class="card card-default">
      <div class="card-body">

        <!-- 휴가 신청 버튼 -->
        <form method="GET" action="/approval-form/1">
          <%-- TODO 인증 붙이면 input hidden 삭제 --%>
          <input type="hidden" name="departmentId" value="2">
          <input type="hidden" name="username" value="jung_frontend">
          <button class="btn btn-info btn-lg btn-block mb-6">휴가 신청</button>
        </form>

        <!-- 메뉴 리스트 -->
        <div class="email-options">
          <ul class="text-center">
            <li class="d-block mb-4">
              <%-- TODO 인증 붙이면 usernmae 파라미터 삭제 --%>
              <a href="/vacation/list?listType=request&username=jung_frontend">휴가 신청 현황</a>
            </li>
            <li class="d-block mb-4">
              <a href="/vacation/list?listType=company">전사 휴가 현황</a>
            </li>
          </ul>
        </div>

      </div>
    </div>
  </div>

  <!-- 메인 콘텐츠 영역 -->
  <div class="col-lg-9">
    <div class="card card-default">

      <div class="card-header d-flex justify-content-between align-items-center">
        <h2>휴가 신청 현황</h2>
        <div>
          <span>남은 연차 : </span>
          <span><strong>00</strong>일</span>
        </div>
      </div>

      <div class="card-body">

        <!-- 휴가 종류 선택 필터 -->
        <div class="row mb-4 ml-0">
          <form method="get" action="/leave/list">

            <input type="hidden" name="username" value="jung_frontend">

            <div class="mr-3">
              <div class="form-group">
                <div class="d-flex align-items-center">
                  <select class="form-control mr-2" id="leaveTypeSelect" name="leaveType">
                    <option value="">전체</option>
                    <option value="연차">연차</option>
                    <option value="반차">반차</option>
                    <option value="병가">병가</option>
                    <option value="경조사">경조사</option>
                    <option value="기타">기타</option>
                  </select>
                </div>
              </div>
            </div>

            <div>
              <button type="submit" class="btn btn-primary">조회</button>
            </div>

          </form>
        </div>

        <!-- 휴가 신청 목록 테이블 -->
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
            <tr>
              <th>상태</th>
              <th>휴가 종류</th>
              <th>휴가 일수</th>
              <th>휴가 기간</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td>
                <span class="badge badge-warning">승인 대기</span>
              </td>
              <td>연차</td>
              <td>2</td>
              <td>2025-09-11 ~ 2025-09-12</td>
            </tr>
            <tr>
              <td>
                <span class="badge badge-success">승인</span>
              </td>
              <td>연차</td>
              <td>1</td>
              <td>2025-08-11</td>
            </tr>
            </tbody>
          </table>
        </div>

      </div>
    </div>
  </div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>