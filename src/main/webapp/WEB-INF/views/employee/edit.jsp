<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사원 상세 페이지</title>
<input type="hidden" id="username" value="${employee.username}">
<c:import url="/WEB-INF/views/include/head.jsp"/>
<style>
/* 기존 스타일 그대로 유지 */
.headProfile{margin-top:70px;margin-bottom:70px;border:1px solid gray;padding:20px;flex-direction:row;display:flex;justify-content:flex-start;align-items:flex-start;gap:50px;}
.headProfile .d-flex { flex-direction: column; }
.info-container { display:flex; flex-direction:row; gap:50px; }
.employeeInformation{flex-direction: column; flex-wrap: wrap; jusify-content: space-evenly; margin-bottom:90px; margin-top:20px;}
.buttonBox{margin-top:50px; margin-left:600px;}
.tabs{margin-bottom:30px;}
.tab-content { display: none; }
.tab-content.active { display: block; }
.tab-button { border: none; background: none; padding: 10px 20px; cursor: pointer; }
.tab-button.active { font-weight: bold; border-bottom: 2px solid #007bff; }

</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 
<c:import url="/WEB-INF/views/include/sidebar.jsp"/>
<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>
<c:import url="/WEB-INF/views/include/header.jsp"/>
<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>

<!-- 사원 정보 폼 -->
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
                입사일: <form:input path="hireDate" type="date"/>
                휴대전화: <form:input path="mobilePhone" />
                직통번호: <form:input path="directPhone" /><br>
                외부이메일: <form:input path="externalEmail"/>
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
</div>
</form:form>

<!-- 출퇴근 탭 -->
<div class="tab-content" id="attendance">
    <form id="attendanceForm" method="post" action="/employee/${employee.username}/attendance/delete">
        <button type="submit" class="btn btn-danger btn-sm">삭제</button>
        <input type="hidden" name="username" value="${employee.username}"/>
        <table class="table table-light">
            <thead class="thead-light">
                <tr>
                    <th><input type="checkbox" id="checkAll"/></th>
                    <th>구분</th>
                    <th>일시</th>
                    <th>수정</th>
                </tr>
            </thead>
            <tbody>
			  <c:forEach var="att" items="${attendanceList}">
			    <tr data-attendance-id="${att.attendanceId}">
			        <td><input type="checkbox" name="attendanceIds" value="${att.attendanceId}"/></td>
			        <td class="workStatusCell" data-status="${att.workStatus != null ? att.workStatus : '-'}">
			            <span class="badge ${att.workStatus == '출근' ? 'badge-success' : 'badge-primary'}">
			                ${att.workStatus != null ? att.workStatus : '-'}
			            </span>
			        </td>
			        <td class="dateTimeCell" 
			            data-checkin="${att.checkInDateTimeForInput != null ? att.checkInDateTimeForInput : ''}" 
			            data-checkout="${att.checkOutDateTimeForInput != null ? att.checkOutDateTimeForInput : ''}">
			            ${att.checkInDateTimeForInput != null ? att.checkInDateTimeForInput : ''}
			        </td>
			        <td>
			            <button type="button" class="btn btn-warning updateAttendanceBtn">수정</button>
			        </td>
			    </tr>
			  </c:forEach>
			</tbody>
        </table>
    </form>
</div>

<a href="<d:url value='/employee/list'/>" class="btn btn-info">목록으로</a>

<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>

<script>
document.addEventListener("DOMContentLoaded", function() {

    // 탭 처리
    document.querySelectorAll(".tab-button").forEach(button => {
      button.addEventListener("click", () => {
        document.querySelectorAll(".tab-button").forEach(btn => btn.classList.remove("active"));
        document.querySelectorAll(".tab-content").forEach(tab => tab.classList.remove("active"));
        button.classList.add("active");
        const tabId = button.getAttribute("data-tab");
        document.getElementById(tabId).classList.add("active");
      });
    });

    // 이미지 미리보기
    const profileInput = document.getElementById('profileImageInput');
    if(profileInput){
        profileInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('profileImage').src = e.target.result;
                }
                reader.readAsDataURL(file);
            }
        });
    }

    // 사진 삭제
    function deleteProfileImage() {
        const username = document.getElementById('username').value;
        fetch('/employee/deleteProfileImage', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'username=' + encodeURIComponent(username)
        })
        .then(response => response.text())
        .then(result => {
            if (result == 'success') {
                document.getElementById('profileImage').src = '/images/default_profile.png';
                alert('사진이 삭제되었습니다.');
            } else {
                alert('사진 삭제에 실패했습니다.');
            }
        })
        .catch(error => { console.error('에러:', error); alert('사진 삭제 중 오류 발생'); });
    }
    window.deleteProfileImage = deleteProfileImage; // 버튼에서 접근 가능하도록 전역 등록

    // 체크박스 전체 선택
    const checkAll = document.getElementById("checkAll");
    if(checkAll){
        checkAll.addEventListener("click", function() {
          const checked = this.checked;
          document.querySelectorAll("input[name='attendanceIds']").forEach(cb => cb.checked = checked);
        });
    }

    // 출퇴근 수정 버튼
    document.querySelectorAll(".updateAttendanceBtn").forEach(button => {
        button.addEventListener("click", function () {
            const row = this.closest("tr");
            const workStatusCell = row.querySelector(".workStatusCell");
            const dateTimeCell = row.querySelector(".dateTimeCell");
            const username = document.getElementById("username").value;

            if (this.textContent == "수정") {
                // 수정 모드
                const currentStatus = (workStatusCell.dataset.status && workStatusCell.dataset.status != '-') ? workStatusCell.dataset.status : '출근';
                const dateTimeValue = currentStatus == "출근" 
                    ? (dateTimeCell.dataset.checkin ? dateTimeCell.dataset.checkin.replace(" ", "T") : '') 
                    : (dateTimeCell.dataset.checkout ? dateTimeCell.dataset.checkout.replace(" ", "T") : '');

                // 구분 select
                workStatusCell.innerHTML = `
                    <select class="form-control workStatusInput">
                        <option value="출근" ${currentStatus == "출근" ? "selected" : ""}>출근</option>
                        <option value="퇴근" ${currentStatus == "퇴근" ? "selected" : ""}>퇴근</option>
                    </select>`;

                // 일시 input
                dateTimeCell.innerHTML = `<input type="datetime-local" class="form-control dateTimeInput" value="${dateTimeValue}"/>`;

                // 버튼 텍스트 변경
                this.textContent = "완료";
                this.classList.remove("btn-warning");
                this.classList.add("btn-success");

            } else {
                // 완료 모드
                const selectEl = row.querySelector(".workStatusInput");
                const newStatus = selectEl ? selectEl.value : '출근';
                const newDateTime = row.querySelector(".dateTimeInput")?.value || '';
                const attendanceId = row.dataset.attendanceId;

                fetch(`/employee/${username}/attendance/edit`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        attendanceId: attendanceId,
                        workStatus: newStatus,
                        dateTime: newDateTime
                    })
                })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        // 완료 후 안전하게 span 표시
                        workStatusCell.innerHTML = ''; // 기존 내용 제거
                        const span = document.createElement('span');
                        span.className = `badge ${newStatus == '출근' ? 'badge-success' : 'badge-primary'}`;
                        span.textContent = newStatus;
                        workStatusCell.appendChild(span);
                        workStatusCell.dataset.status = newStatus;

                        // 일시 표시
                        dateTimeCell.textContent = newDateTime.replace("T", " ");
                        if (newStatus == "출근") dateTimeCell.dataset.checkin = newDateTime;
                        else dateTimeCell.dataset.checkout = newDateTime;

                        // 버튼 복구
                        button.textContent = "수정";
                        button.classList.remove("btn-success");
                        button.classList.add("btn-warning");
                    } else {
                        alert("수정 실패: " + data.error);
                    }
                })
                .catch(err => { console.error(err); alert("수정 중 오류 발생"); });
            }
        });
    });

});
</script>


</body>
</html>
