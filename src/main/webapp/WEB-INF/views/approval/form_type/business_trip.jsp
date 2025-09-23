<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<br>
<h5 class="p-3">출장 내용</h5>
<table class="table table-bordered">
  <tbody>
  <tr>
    <th class="col-2 table-light align-middle">출장 기간</th>
    <td class="col-4">
      <div class="d-flex align-items-center">
        <input name="startDate" type="date" class="form-control col-sm-10 mb-2" id="startDate"><p>&nbsp;&nbsp;&nbsp;&nbsp;~</p>
      </div>
      <input name="endDate" type="date" class="form-control col-sm-10" id="endDate">
    </td>
    <th class="col-2 table-light align-middle">출장지</th>
    <td class="col-4 align-middle"><input name="destination" type="text" class="form-control"></td>
  </tr>
  <tr>
    <th class="table-light align-middle">교통편</th>
    <td colspan="3"><input name="transportation" type="text" class="form-control"></td>
  </tr>
  <tr>
    <th class="table-light align-middle">출장목적</th>
    <td colspan="3"><input name="purpose" type="text" class="form-control"></td>
  </tr>
  <tr>
    <th class="table-light align-middle">비고</th>
    <td colspan="3"><input name="note" type="text" class="form-control"></td>
  </tr>
  </tbody>
</table>

<br>
<h5 class="p-3">출장자 정보</h5>
<table class="table table-bordered">
  <tbody>
  <tr>
    <th class="col-2 table-light align-middle">성명</th>
    <td><input name="travelerName" type="text" class="form-control"></td>
    <th class="col-2 table-light align-middle">직위</th>
    <td><input name="travelerPosition" type="text" class="form-control"></td>
  </tr>
  <tr>
    <th class="table-light align-middle">소속</th>
    <td><input name="travelerDepartment" type="text" class="form-control"></td>
    <th class="table-light align-middle">전화번호</th>
    <td><input name="travelerPhone" type="text" class="form-control"></td>
  </tr>
  </tbody>
</table>

<!-- 출장여비 -->
<br>
<h5 class="p-3">출장여비</h5>
<table class="table table-bordered">
  <tbody>
  <tr>
    <th class="col-2 table-light">구분</th>
    <th class="table-light">산출내역</th>
    <th class="table-light">금액</th>
  </tr>
  <tr>
    <th class="table-light align-middle">교통비</th>
    <td><input name="trafficBreakdown" type="text" class="form-control"></td>
    <td><input name="trafficAmount" type="number" class="form-control amount-input"></td>
  </tr>
  <tr>
    <th class="table-light align-middle">일비</th>
    <td><input name="dailyBreakdown" type="text" class="form-control"></td>
    <td><input name="dailyAmount" type="number" class="form-control amount-input"></td>
  </tr>
  <tr>
    <th class="table-light align-middle">식비</th>
    <td><input name="foodBreakdown" type="text" class="form-control"></td>
    <td><input name="foodAmount" type="number" class="form-control amount-input"></td>
  </tr>
  <tr>
    <th class="table-light align-middle">숙박비</th>
    <td><input name="lodgingBreakdown" type="text" class="form-control"></td>
    <td><input name="lodgingAmount" type="number" class="form-control amount-input"></td>
  </tr>
  <tr>
    <th class="table-light align-middle">기타</th>
    <td><input name="otherBreakdown" type="text" class="form-control"></td>
    <td><input name="otherAmount" type="number" class="form-control amount-input"></td>
  </tr>
  <tr>
    <th class="table-light">계</th>
    <th></th>
    <th id="total-amount" class="text-right">0</th>
  </tr>
  </tbody>
</table>