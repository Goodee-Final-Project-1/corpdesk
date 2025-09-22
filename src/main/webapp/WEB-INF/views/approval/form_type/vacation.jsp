<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<br>
<table class="table table-bordered">
  <tbody>
  <tr>
    <th class="col-2 table-light align-middle">휴가 종류</th>
    <td>
      <div class="form-group">
        <select name="vacationTypeId" class="form-control" id="vacationTypeId">
          <p>d${vacationTypeList }</p>
          <c:forEach items="${vacationTypeList }" var="el">
            <option value="${el.vacationTypeId}">${el.vacationTypeName}</option>
          </c:forEach>
        </select>
      </div>
    </td>
  </tr>
  <tr>
    <th class="table-light align-middle">휴가 기간</th>
    <td>
      <div class="d-flex align-items-center">
        <input name="startDate" type="date" class="form-control col-sm-5 mb-2" id="startDate"><p>&nbsp;&nbsp;&nbsp;&nbsp;~</p>
      </div>
      <input name="endDate" type="date" class="form-control col-sm-5" id="endDate">
    </td>
  </tr>
  <tr>
    <th class="table-light align-middle">사용일수</th>
    <td><input name="usedDays" type="number" class="form-control" id="usedDays" readonly></td>
  </tr>
  <tr>
    <th class="table-light align-middle">휴가 사유</th>
    <td>
      <textarea name="reason" class="form-control" rows="5"></textarea>
    </td>
  </tr>
  </tbody>
</table>

