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

  <!-- 출퇴근 버튼 영역 -->
  <div class="col-lg-3">
    <div class="card card-default">
      <div class="card-body">

        <!-- 출퇴근 시간 표시 -->
        <div class="card card-default mb-3">
          <div class="card-body d-flex justify-content-around p-3">
            <div class="text-center">
              <p class="card-text mb-1">출근시간</p>
              <p class="card-text mb-0">00:00</p>
            </div>
            <div class="text-center">
              <p class="card-text mb-1">퇴근시간</p>
              <p class="card-text mb-0">00:00</p>
            </div>
          </div>
        </div>

        <!-- 출근/퇴근 버튼 -->
        <div class="row no-gutters">
          <div class="col-6 pr-2">
            <button type="button" class="btn btn-info btn-lg btn-block">출근</button>
          </div>
          <div class="col-6 pl-2">
            <button type="button" class="btn btn-info btn-lg btn-block" disabled>퇴근</button>
          </div>
        </div>

      </div>
    </div>
  </div>

  <!-- 출퇴근 목록 영역 -->
  <div class="col-lg-9">
    <div class="card card-default">

      <div class="card-header">
        <h2>내 출퇴근 목록</h2>
      </div>

      <div class="card-body">

        <!-- 년, 월 선택 - 한 줄 배치 -->
        <div class="row mb-4 ml-0">
          <div class="mr-4">
            <div class="form-group">
              <div class="d-flex align-items-center">
                <select class="form-control mr-2" id="yearSelect">
                  <option>2024</option>
                  <option>2023</option>
                  <option>2022</option>
                  <option>2021</option>
                  <option>2020</option>
                </select>
                <span>년</span>
              </div>
            </div>
          </div>
          <div>
            <div class="form-group">
              <div class="d-flex align-items-center">
                <select class="form-control mr-2" id="monthSelect">
                  <option>1</option>
                  <option>2</option>
                  <option>3</option>
                  <option>4</option>
                  <option selected>9</option>
                  <option>10</option>
                  <option>11</option>
                  <option>12</option>
                </select>
                <span>월</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 근태현황, 근무시간 - 한 줄 배치 -->
        <br>
        <div class="row mb-4 col-11 m-auto">
          <div class="col-xl-6">
            <h5 class="mb-2">근태 현황</h5>
            <div class="card card-default">
              <div class="card-body p-4 d-flex justify-content-between flex-wrap">

                <div class="col-4 text-center">
                  <h6>지각</h6>
                  <br>
                  <b>2</b><span>회</span>
                </div>
                <div class="col-4 text-center">
                  <h6>조퇴</h6>
                  <br>
                  <b>2</b><span>회</span>
                </div>
                <div class="col-4 text-center">
                  <h6>결근</h6>
                  <br>
                  <b>2</b><span>회</span>
                </div>

              </div>
            </div>
          </div>

          <div class="col-xl-6">
            <h5 class="mb-2">근무 시간</h5>
            <div class="card card-default">
              <div class="card-body p-4 d-flex justify-content-around flex-wrap">

                <div class="col-4 text-center">
                  <h6>지각</h6>
                  <br>
                  <b>2</b><span>회</span>
                </div>
                <div class="col-4 text-center">
                  <h6>조퇴</h6>
                  <br>
                  <b>2</b><span>회</span>
                </div>

              </div>
            </div>
          </div>
        </div>

        <!-- 출퇴근 목록 테이블 -->
        <br>
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
            <tr>
              <th>출근 날짜</th>
              <th>부서</th>
              <th>이름</th>
              <th>출근 시간</th>
              <th>퇴근 시간</th>
              <th>근무 상태</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td>2024-09-25</td>
              <td>개발팀</td>
              <td>홍길동</td>
              <td>09:00</td>
              <td>18:00</td>
              <td>정상출근</td>
            </tr>
            </tbody>
          </table>
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