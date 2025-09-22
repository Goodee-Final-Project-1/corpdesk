<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<br>
<table class="table table-bordered">
  <tbody>
  <tr>
    <th class="col-2 table-light align-middle">시행일자</th>
    <td><input name="executionDate" type="date" class="form-control"></td>
    <th class="col-2 table-light align-middle">협조부서</th>
    <td>
      <div class="form-group">
        <select name="coopDepartmentId" class="form-control" id="departmentId">
          <c:forEach items="${departmentList }" var="el">
            <option value="${el.departmentId}">${el.departmentName}</option>
          </c:forEach>
        </select>
      </div>
    </td>
  </tr>
  <tr>
    <th class="table-light align-middle">합의</th>
    <td colspan="3">${userInfo.departmentName}</td>
  </tr>
  <tr>
    <th class="table-light align-middle">제목</th>
    <td colspan="3"><input name="draftTitle" type="text" class="form-control"></td>
  </tr>
  <tr>
    <th class="table-light align-middle">내용</th>
    <td colspan="3">
      <textarea name="draftContent" class="form-control" rows="5"></textarea>
    </td>
  </tr>
  </tbody>
</table>