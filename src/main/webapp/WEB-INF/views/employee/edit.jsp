<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="d" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<style>
.headProfile{
	
	margin-top:70px;
	margin-bottom:70px;
	border: 1px solid gray;
	padding: 20px;
	flex-direction: column;
	
}

.employeeInformation{

	
	flex-direction: column;
	  flex-wrap: wrap;
	  jusify-content: space-evenly;
	  margin-bottom:90px;
	  margin-top:20px;
}


.buttonBox{
	margin-top: 50px;
	margin-left: 600px;
}

.tabs{

	margin-bottom: 30px;
}

.tab-content { display: none; }
.tab-content.active { display: block; }

.tab-button {
  border: none;
  background: none;
  padding: 10px 20px;
  cursor: pointer;
}
.tab-button.active {
  font-weight: bold;
  border-bottom: 2px solid #007bff;
}

</style>


	<meta charset="UTF-8">
	<title>사원 상세 페이지</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<form:form method="post" modelAttribute="employee" action="/employee/edit">
    <form:hidden path="username" /> <!-- TODO username으로 변경 -->
	    <h2>사원 상세정보</h2>
    <div class="box">
	    <div class="headProfile d-flex" >
		    <div>
			    이름:<form:input path="name" />
			    직원구분:<form:input path="employeeType"/>
			    부서:<form:select path="departmentId">
			            <form:option value="" label="부서를 선택하세요"/>
			            <form:options items="${departments}" itemValue="departmentId" itemLabel="departmentName"/>
			          </form:select><br/>
			 </div>
			 <div>
			    이메일:<form:input path="externalEmail"/>
			    입사일:<form:input path="hireDate" type="date"/>
		    	휴대전화:<form:input path="mobilePhone" /><br/>
		    	직위:<form:select path="positionId">
		            <form:option value="" label="직위를 선택하세요"/>
		            <form:options items="${positions}" itemValue="positionId" itemLabel="positionName"/>
		          </form:select><br/>
		    </div>
	    </div>
	    
	    
	    
	    
	    <div class="tabs">
		  <button type="button" class="tab-button active" data-tab="info">정보</button>
		  <span>&nbsp;|&nbsp;</span>
		  <button type="button" class="tab-button" data-tab="attendance">출퇴근 현황</button>
		</div>
		
		
		<!-- 정보 탭 -->
		<div class="tab-content active" id="info">
			<span class="badge badge-dark">직원정보</span>
		
	   	 <div class="employeeInformation d-flex">
			    <div class="employeebox">
			    주민(외국인)번호: <form:input path="residentNumber"/>
			    국적: <form:input path="nationality"/>
			    체류자격: <form:input path="visaStatus"/><br/>
			    
			    영문이름: <form:input path="englishName"/>
			    성별: <form:select path="gender">
			            <form:option value="M" label="남"/>
			            <form:option value="F" label="여"/>
			         </form:select>
			    생년월일: <form:input path="birthDate" type="date"/><br/>
			   
			    주소:<form:input path="address"/>
			    퇴사일자: <form:input path="lastWorkingDay" type="date"/>
			    </div>
			    
			    <div class="buttonBox">
				    <input type="submit" value="수정" class="btn btn-info">
				    <button type="button" onclick="deleteEmployee()" class="btn btn-info">삭제</button>
			    </div>
	    	</div>
    	</div>
    	<div class="tab-content" id="attendance">
	      <span class="badge badge-dark">출퇴근 현황</span>
	      <p>출퇴근 데이터가 여기에 표시됩니다.</p>
	    </div>
	    	
    	
    	
			    
			    
			    
			    
			    
			    <a href="<d:url value='/employee/list'/>" class="btn btn-info">목록으로</a>

    </div>
	</form:form>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>

<script>


document.querySelectorAll(".tab-button").forEach(button => {
  button.addEventListener("click", () => {
    document.querySelectorAll(".tab-button").forEach(btn => btn.classList.remove("active"));
    document.querySelectorAll(".tab-content").forEach(tab => tab.classList.remove("active"));

    button.classList.add("active");
    const tabId = button.getAttribute("data-tab");
    document.getElementById(tabId).classList.add("active");
  });
});



</script>
</html>