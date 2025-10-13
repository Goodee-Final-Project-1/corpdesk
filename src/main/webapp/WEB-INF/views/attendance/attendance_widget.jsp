<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!-- 출퇴근 시간 표시 -->
<div class="card card-default mb-3">
  <div class="card-body d-flex justify-content-around p-3">

    <c:choose>
      <c:when test="${currAttd.workStatus eq '출근전' or (currAttd.workStatus eq '퇴근' and !currAttd.today)}">
        <div class="text-center">
          <p class="card-text mb-1">출근</p>
          <p class="card-text mb-0">00:00:00</p>
        </div>
        <div class="text-center">
          <p class="card-text mb-1">퇴근</p>
          <p class="card-text mb-0">00:00:00</p>
        </div>
      </c:when>
      <c:otherwise>
        <div class="text-center">
          <p class="card-text mb-1">출근</p>
          <p class="card-text mb-0">${fn:substring(currAttd.checkInTime, 0, 8)}</p>
        </div>
        <div class="text-center">
          <p class="card-text mb-1">퇴근</p>
          <p class="card-text mb-0">${currAttd.checkOutTime eq null ? '00:00:00' : fn:substring(currAttd.checkOutTime, 0, 8)}</p>
        </div>
      </c:otherwise>
    </c:choose>

  </div>
</div>

<!-- 출근/퇴근 버튼 -->
<div class="row no-gutters">
  <div class="col-6 pr-2">
    <form method="POST" action="/attendance">
      <button class="btn btn-info btn-lg btn-block"
      ${currAttd.workStatus eq '휴가' or currAttd.workStatus eq '출근' ? 'disabled' : ''}>출근
      </button>
    </form>
  </div>

  <div class="col-6 pl-2">
    <form method="post" action="/attendance/${currAttd.attendanceId}">
      <input type="hidden" name="_method" value="PATCH">
      <button class="btn btn-info btn-lg btn-block"
      ${currAttd.workStatus eq '휴가'
        or currAttd.workStatus eq '출근전'
        or currAttd.workStatus eq '퇴근' ? 'disabled' : ''}>퇴근
      </button>
    </form>
  </div>
</div>