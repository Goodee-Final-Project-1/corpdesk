<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2>사원 상세 정보</h2>
<form:form method="post" modelAttribute="employee" action="/employee/edit">
    <form:hidden path="employeeId" />
    
    이름: <form:input path="name" /><br/>
    이메일: <form:input path="externalEmail" /><br/>
    입사일: <form:input path="hireDate" type="date"/><br/>
    성별: <form:select path="gender">
            <form:option value="M" label="남"/>
            <form:option value="F" label="여"/>
         </form:select><br/>
    휴대전화: <form:input path="mobilePhone" /><br/>
    부서: <form:select path="department.departmentId">
            <form:option value="" label="부서를 선택하세요"/>
            <form:options items="${departments}" itemValue="departmentId" itemLabel="departmentName"/>
          </form:select><br/>
    직위: <form:select path="position.positionId">
            <form:option value="" label="직위를 선택하세요"/>
            <form:options items="${positions}" itemValue="positionId" itemLabel="positionName"/>
          </form:select><br/>
    계정상태: <form:select path="enabled">
                <form:option value="true" label="활성"/>
                <form:option value="false" label="비활성"/>
              </form:select><br/>
    퇴사일자: <form:input path="lastWorkingDay" type="date"/><br/>
    <input type="submit" value="수정"/>
    <button type="button" onclick="deleteEmployee()">삭제</button>
    <a href="<c:url value='/employee/list'/>">목록으로</a>
</form:form>
<script>
    function deleteEmployee() {
        const lastWorkingDay = document.querySelector('[name="lastWorkingDay"]').value;
        const employeeId = document.querySelector('[name="employeeId"]').value;
    
        if (!lastWorkingDay) {
            alert("퇴사일자를 먼저 입력하세요.");
            return;
        }
    
        if (confirm("정말 삭제하시겠습니까?")) {
            // 숨겨진 form을 만들어서 POST 요청
            const form = document.createElement("form");
            form.method = "post";
            form.action = "/employee/delete/" + employeeId;
            document.body.appendChild(form);
            form.submit();
        }
    }
    </script>
</html>
