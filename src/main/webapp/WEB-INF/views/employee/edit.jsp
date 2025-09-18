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
	flex-direction: row;
	
	display: flex;
    justify-content: flex-start; /* Aligns items to the start of the flex container */
    align-items: flex-start; /* Aligns items to the top */
    gap: 50px; /* ⭐ Add some space between the profile box and info box */
	
}

.headProfile .d-flex {
    flex-direction: column;
}

.info-container {
    display: flex;
    flex-direction: row; /* Stacks the two info divs vertically */
    gap: 50px;
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
	<input type="hidden" id="username" value="${employee.username}">
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<form:form method="post" modelAttribute="employee" action="/employee/edit" enctype="multipart/form-data">
    <form:hidden path="username" />
     
	    <h2>사원 상세정보</h2>
    <div class="box">
	    <div class="headProfile d-flex justify-content-start align-items-start">
	    	<div class="fileBox me-5">
			    <c:set var="profileImageUrl" value="/images/default_profile.jpg" />
			    <c:if test="${employeeFile != null}">
			        <c:set var="profileImageUrl" value="/files/profile/${employeeFile.saveName}.${employeeFile.extension}" />
			    </c:if>
			
			    <img id="profileImage" src="${profileImageUrl}" alt="Profile Image" style="width:150px; height:150px; border-radius:50%; object-fit: cover;">
			    
			    <input type="file" id="profileImageInput" name="profileImageFile" style="display:none;" accept="image/*">
			    
			    <div class="image-buttons" style="margin-top: 10px;">
			        <button type="button" class="btn btn-primary btn-sm" onclick="document.getElementById('profileImageInput').click()">사진 변경</button>
			        <button type="button" class="btn btn-danger btn-sm" onclick="deleteProfileImage()">사진 삭제</button>
			    </div>
			</div>
			
		  <div class="info-container d-flex">
		    <div>
			    이름: &nbsp;<form:input path="name" />
			    직원구분: &nbsp;<form:input path="employeeType"/>
			    부서: &nbsp;<form:select path="departmentId">
			            <form:option value="" label="부서를 선택하세요"/>
			            <form:options items="${departments}" itemValue="departmentId" itemLabel="departmentName"/>
			          </form:select><br/>
			 </div>
			 <div>
			    이메일: <form:input path="externalEmail"/>
			    입사일: <form:input path="hireDate" type="date"/>
		    	휴대전화: <form:input path="mobilePhone" /><br/>
		    	직위: &nbsp;&nbsp;&nbsp;&nbsp;<form:select path="positionId">
		            <form:option value="" label="직위를 선택하세요"/>
		            <form:options items="${positions}" itemValue="positionId" itemLabel="positionName"/>
		          </form:select><br/>
		      </div>
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

//⭐⭐⭐ 이미지 미리보기 기능 ⭐⭐⭐
//파일 input의 변화를 감지하여 이미지를 미리 보여줍니다.
document.getElementById('profileImageInput').addEventListener('change', function(event) {
 const file = event.target.files[0];
 if (file) {
     const reader = new FileReader();
     reader.onload = function(e) {
         document.getElementById('profileImage').src = e.target.result;
     }
     reader.readAsDataURL(file);
 }
});

//⭐⭐⭐ 사진 삭제 기능 (AJAX 요청) ⭐⭐⭐
//서버에 사진 삭제를 요청하고 성공하면 화면을 업데이트합니다.
function deleteProfileImage() {
    const username = document.getElementById('username').value;
    
    // fetch 요청의 body에 username을 담아 보냅니다.
    fetch('/employee/deleteProfileImage', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'username=' + encodeURIComponent(username)
    })
    .then(response => response.text())
    .then(result => {
        if (result === 'success') {
            document.getElementById('profileImage').src = '/images/default_profile.png';
            alert('사진이 삭제되었습니다.');
        } else {
            alert('사진 삭제에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('에러:', error);
        alert('사진 삭제 중 오류가 발생했습니다.');
    });
}



</script>
</html>