<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>사원 상세 페이지</title>
  <c:import url="/WEB-INF/views/include/head.jsp"/>
  <style>
  /* 기존 스타일 그대로 유지 */
  .headProfile{margin-top:70px;margin-bottom:70px;border:1px solid gray;padding:20px;align-items:stretch;gap:50px;}
  .headProfile .d-flex { flex-direction: column; }
  .info-container { flex:1; width:100%;}
  .employeeInformation{flex-direction: column; flex-wrap: wrap; jusify-content: space-evenly; margin-bottom:90px; margin-top:20px;}
  .buttonBox{margin-top: 50px;
      display: flex;
      justify-content: center;
      gap: 10px;}
  .tabs{margin-bottom:30px;}
  .tab-content { display: none; }
  .tab-content.active { display: block; }
  .tab-button { border: none; background: none; padding: 10px 20px; cursor: pointer; }
  .tab-button.active { font-weight: bold; border-bottom: 2px solid #007bff; }

  </style>

  <meta name="_csrf" content="${_csrf.token}"/>
  <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>
<c:import url="/WEB-INF/views/include/sidebar.jsp"/>
<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>
<c:import url="/WEB-INF/views/include/header.jsp"/>
<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 사원 정보 폼 -->
<div class="card card-default">
  <div class="card-body">
    <form:form method="post" modelAttribute="employee" action="/employee/edit" enctype="multipart/form-data">
    <form:hidden path="username"/>

    <div class="card-header">
      <h2 class="mb-0">사원 상세정보</h2>
    </div>

    <div class="box">
      <div class="headProfile d-flex justify-content-start align-items-start">
        <div class="fileBox me-5">
          <c:set var="profileImageUrl" value="/images/default_profile.jpg"/>
          <c:if test="${employeeFile != null and employeeFile.useYn}">
            <c:set var="profileImageUrl" value="/files/profile/${employeeFile.saveName}.${employeeFile.extension}"/>
          </c:if>
          <img id="profileImage" src="${profileImageUrl}" alt="Profile Image"
               style="width:150px; height:150px; border-radius:50%; object-fit: cover; margin-left:25px;">
          <input type="file" id="profileImageInput" name="profileImageFile" style="display:none;" accept="image/*">
          <div class="image-buttons" style="margin-top: 50px;">
            <button type="button" class="btn btn-primary btn-sm"
                    onclick="document.getElementById('profileImageInput').click()">사진 변경
            </button>
            <button type="button" class="btn btn-danger btn-sm" onclick="deleteProfileImage()">사진 삭제</button>
          </div>
        </div>
        <div class="info-container d-flex">
          <div class="row">
            <div class="col-md-3">
              이름<form:input path="name" class="form-control"/>
            </div>
            <div class="col-md-3">
              직원구분<form:select path="employeeType" class="form-control">
              <form:option value="" label="선택하세요"/>
              <form:option value="정규" label="정규"/>
              <form:option value="계약" label="계약"/>
              <form:option value="외주" label="외주"/>
              <form:option value="파견" label="파견"/>
              <form:option value="파트" label="파트"/>
              <form:option value="기타" label="기타"/>
            </form:select>
            </div>
            <div class="col-md-3">
              부서<form:select path="departmentId" class="form-control">
              <form:option value="" label="부서를 선택하세요"/>
              <form:options items="${departments}" itemValue="departmentId" itemLabel="departmentName"/>
            </form:select>
            </div>
          </div>
          <div class="row">
            <div class="col-md-3">
              입사일<form:input path="hireDate" type="date" class="form-control"/>
            </div>
            <div class="col-md-3">
              휴대전화<form:input path="mobilePhone" class="form-control"/>
            </div>
            <div class="col-md-3">
              직통번호<form:input path="directPhone" class="form-control"/>
            </div>
          </div>
          <div class="row">
            <div class="col-md-3">
              외부이메일<form:input path="externalEmail" class="form-control"/>
            </div>
            <div class="col-md-3">
              직위<form:select path="positionId" class="form-control">
              <form:option value="" label="직위를 선택하세요"/>
              <form:options items="${positions}" itemValue="positionId" itemLabel="positionName"/>
            </form:select>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="card card-defalut">
  <div class="card-body">
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
          <div class="row">
            <div class="col-md-2">
              주민(외국인)번호 <form:input path="residentNumber" class="form-control"/>
            </div>
            <div class="col-md-2">
              국적 <form:input path="nationality" class="form-control"/>
            </div>
            <div class="col-md-2">
              체류자격 <form:input path="visaStatus" class="form-control"/>
            </div>
          </div>
          <div class="row">
            <div class="col-md-2">
              영문이름 <form:input path="englishName" class="form-control"/>
            </div>
            <div class="col-md-2">
              성별 <form:select path="gender" class="form-control">
              <form:option value="M" label="남"/>
              <form:option value="F" label="여"/>
            </form:select>
            </div>
            <div class="col-md-2">
              생년월일 <form:input path="birthDate" type="date" class="form-control"/>
            </div>
          </div>
          <div class="row">
            <div class="col-md-2">
              주소<form:input path="address" class="form-control"/>
            </div>
            <div class="col-md-3">
              퇴사일자 <form:input path="lastWorkingDay" id="lastWorkingDay" type="date" class="form-control"/>
            </div>
          </div>
        </div>

        <div class="buttonBox">
          <input type="submit" value="수정" class="btn btn-primary">
          <button type="button" onclick="deleteEmployee('${employee.username}')" class="btn btn-danger">삭제</button>
        </div>
      </div>
    </div>

    </form:form>

    <!-- 출퇴근 탭 -->
    <div class="tab-content" id="attendance">

      <!-- 출퇴근 추가 폼 -->

      <div class="mb-3">
        <label>구분</label>
        <select id="newWorkStatus" class="form-control" style="width:150px; display:inline-block;">
          <option value="출근">출근</option>
          <option value="퇴근">퇴근</option>
          <option value="출근전">출근전</option>
          <option value="휴가">휴가</option>
        </select>

        <label>일시</label>
        <input type="datetime-local" id="newDateTime" class="form-control" style="width:200px; display:inline-block;">

        <button type="button" id="addAttendanceBtn" class="btn btn-info">추가</button>
      </div>


      <form id="attendanceForm">
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
			            <span class="badge badge-primary">
                      ${att.workStatus != null ? att.workStatus : '-'}
                  </span>
              </td>
              <td class="dateTimeCell"
                  data-checkin="${att.checkInDateTimeForInput != null ? att.checkInDateTimeForInput : ''}"
                  data-checkout="${att.checkOutDateTimeForInput != null ? att.checkOutDateTimeForInput : ''}">

                <c:choose>
                  <c:when test="${att.workStatus == '출근' and att.checkInDateTimeForInput != null}">
                    ${att.checkInDateTimeForInput.replace("T"," ")}
                  </c:when>
                  <c:when test="${att.workStatus == '퇴근' and att.checkOutDateTimeForInput != null}">
                    ${att.checkOutDateTimeForInput.replace("T"," ")}
                  </c:when>
                  <c:otherwise>
                    -
                  </c:otherwise>
                </c:choose>
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
  </div>
</div>
</div>
</div>
<div class="d-flex justify-content-center mt-4">
  <a href="/employee/list" class="btn btn-primary">목록으로</a>
</div>
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>

<script>
  document.addEventListener("DOMContentLoaded", function () {

    // ---------------- 탭 처리 ----------------
    document.querySelectorAll(".tab-button").forEach(button => {
      button.addEventListener("click", () => {
        document.querySelectorAll(".tab-button").forEach(btn => btn.classList.remove("active"));
        document.querySelectorAll(".tab-content").forEach(tab => tab.classList.remove("active"));
        button.classList.add("active");
        const tabId = button.getAttribute("data-tab");
        document.getElementById(tabId).classList.add("active");
      });
    });

    // 페이지 로드 시 hash 체크해서 해당 탭 활성화
    if (window.location.hash === "#attendance") {
      document.querySelectorAll(".tab-button").forEach(btn => btn.classList.remove("active"));
      document.querySelectorAll(".tab-content").forEach(tab => tab.classList.remove("active"));

      const attendanceButton = document.querySelector(".tab-button[data-tab='attendance']");
      const attendanceTab = document.getElementById("attendance");
      if (attendanceButton && attendanceTab) {
        attendanceButton.classList.add("active");
        attendanceTab.classList.add("active");
      }
    }

    // 이미지 미리보기
    const profileInput = document.getElementById('profileImageInput');
    if (profileInput) {
      profileInput.addEventListener('change', function (event) {
        const file = event.target.files[0];
        if (file) {
          const reader = new FileReader();
          reader.onload = function (e) {
            document.getElementById('profileImage').src = e.target.result;
          }
          reader.readAsDataURL(file);
        }
      });
    }

    // ---------------- 출퇴근 추가 ----------------
    document.getElementById("addAttendanceBtn").addEventListener("click", function () {
      const username = document.getElementById("username").value;
      const workStatus = document.getElementById("newWorkStatus").value;
      const dateTime = document.getElementById("newDateTime").value;

      if (!dateTime) {
        Swal.fire({
          text: "날짜와 시간을 입력해주세요.",
          icon: "warning"
        });
        return;
      }

      fetch(`/employee/${username}/attendance/add`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({workStatus, dateTime})
      })
          .then(res => res.json())
          .then(data => {
            if (data.success) {
              // 새로고침 전에 hash 설정
              location.hash = "#attendance";
              location.reload(); // 페이지 새로고침

            } else {
              Swal.fire({
                text: "추가에 실패했습니다.",
                icon: "error"
              });
            }
          })
          .catch(err => {
            console.error(err);
            Swal.fire({
              text: "추가 중 오류가 발생했습니다.",
              icon: "error"
            });
          });
    });


    // 사진 삭제
    function deleteProfileImage() {
      const username = document.getElementById('username').value;
      fetch('/employee/deleteProfileImage', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'username=' + encodeURIComponent(username)
      })
          .then(response => response.text())
          .then(result => {
            if (result == 'success') {
              document.getElementById('profileImage').src = '/images/default_profile.jpg';

              // 여기가 중요: input 초기화
              const profileInput = document.getElementById('profileImageInput');
              if (profileInput) profileInput.value = "";

              Swal.fire({
                text: "사진이 삭제되었습니다.",
                icon: "success"
              });

            } else {
              Swal.fire({
                text: "사진 삭제에 실패했습니다.",
                icon: "error"
              });
            }
          })
          .catch(error => {
            console.error('에러:', error);

            Swal.fire({
              text: "사진 삭제 중 오류가 발생했습니다.",
              icon: "error"
            });

          });
    }

    window.deleteProfileImage = deleteProfileImage; // 버튼에서 접근 가능하도록 전역 등록

    // 체크박스 전체 선택
    const checkAll = document.getElementById("checkAll");
    if (checkAll) {
      checkAll.addEventListener("click", function () {
        const checked = this.checked;
        document.querySelectorAll("input[name='attendanceIds']").forEach(cb => cb.checked = checked);
      });
    }

    // 출퇴근 삭제 처리
    const attendanceForm = document.getElementById("attendanceForm");
    if (attendanceForm) {
      attendanceForm.addEventListener("submit", function (e) {

        e.preventDefault(); // 기본 submit 막기

        // 선택된 체크박스 확인
        const selectedIds = Array.from(document.querySelectorAll("input[name='attendanceIds']:checked"))
            .map(cb => cb.value);
        if (selectedIds.length === 0) {
          Swal.fire({
            text: "삭제할 항목을 선택해주세요.",
            icon: "warning"
          });

          return;
        }

        Swal.fire({
          text: "정말 삭제하시겠습니까?",
          icon: "question",
          showCancelButton: true,
          confirmButtonText: "삭제",
          cancelButtonText: "취소",
          reverseButtons: true
        }).then(result => {

          if (result.isConfirmed) {

            const username = document.getElementById("username").value;

            fetch(`/employee/${username}/attendance/delete`, {
              method: 'POST',
              headers: {'Content-Type': 'application/json'},
              body: JSON.stringify({attendanceIds: selectedIds})
            })
                .then(res => res.json())
                .then(data => {
                  if (data.success) {
                    Swal.fire({
                      text: "삭제되었습니다.",
                      icon: "success"
                    }).then(result => {
                      // 확인 버튼을 누르거나 창 밖을 클릭했을 때 모두 실행
                      if (result.isConfirmed || result.isDismissed) {

                        // 삭제된 행 제거
                        selectedIds.forEach(id => {
                          const row = document.querySelector(`tr[data-attendance-id='${id}']`);
                          if (row) row.remove();
                          // 삭제 성공 후 hash 설정 + 페이지 새로고침
                          location.hash = "#attendance";
                          location.reload();
                        });

                      }
                    });

                  } else {
                    Swal.fire({
                      text: "삭제에 실패했습니다.",
                      icon: "error"
                    });
                  }
                })
                .catch(err => {
                  console.error(err);

                  Swal.fire({
                    text: "삭제 중 오류가 발생했습니다.",
                    icon: "error"
                  });

                });

          }

        });

      });
    }

  });

  //삭제 버튼
  function deleteEmployee(username) {
    const lastWorkingDayInput = document.getElementById('lastWorkingDay');
    if (lastWorkingDayInput && !lastWorkingDayInput.value) {

      Swal.fire({
        text: "퇴사일자를 먼저 설정해 주세요.",
        icon: "warning"
      });

      return;
    }

    Swal.fire({
      text: "정말 삭제하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "삭제",
      cancelButtonText: "취소",
      reverseButtons: true
    }).then(result => {
      if (result.isConfirmed) {

        // CSRF 메타 (둘 다 있을 때만 추가 → Invalid name 방지)
        const csrfTokenEl = document.querySelector('meta[name="_csrf"]');
        const csrfHeaderEl = document.querySelector('meta[name="_csrf_header"]');
        const token = csrfTokenEl ? csrfTokenEl.getAttribute('content') : null;
        const header = csrfHeaderEl ? csrfHeaderEl.getAttribute('content') : null;

        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        if (header && token) headers.append(header.trim(), token.trim());

        const body = new URLSearchParams({
          lastWorkingDay: lastWorkingDayInput ? lastWorkingDayInput.value : ''
        });

        fetch('/employee/delete/' + encodeURIComponent(username), {
          method: 'POST',
          headers,
          body: body.toString()
        })
            .then(res => res.json())
            .then(data => {
              if (data.success) {

                Swal.fire({
                  text: "삭제되었습니다.",
                  icon: "success"
                }).then(result => {
                  // 확인 버튼을 누르거나 창 밖을 클릭했을 때 모두 실행
                  if (result.isConfirmed || result.isDismissed) window.location.href = "/employee/list";
                });

              } else {
                Swal.fire({
                  text: "삭제에 실패했습니다.",
                  icon: "error"
                });
              }
            })
            .catch(err => {
              console.error(err);

              Swal.fire({
                text: "삭제 중 오류가 발생했습니다.",
                icon: "error"
              });

            });

      }
    });

  }

  //---------------- 출퇴근 수정 이벤트 함수 ----------------
  function attachUpdateEvent(button) {
    button.addEventListener("click", function () {
      const row = this.closest("tr");
      const workStatusCell = row.querySelector(".workStatusCell");
      const dateTimeCell = row.querySelector(".dateTimeCell");
      const username = document.getElementById("username").value;

      if (this.textContent.trim() == "수정") {
        let currentStatus = workStatusCell.dataset.status?.trim() || workStatusCell.innerText.trim() || "";

        let dateTimeValue = "";
        if (currentStatus == "출근") dateTimeValue = dateTimeCell.dataset.checkin || '';
        else if (currentStatus == "퇴근") dateTimeValue = dateTimeCell.dataset.checkout || '';

        if (dateTimeValue) dateTimeValue = dateTimeValue.replace(" ", "T").slice(0, 16);

        // select 생성
        workStatusCell.innerHTML = `
                <select class="form-control workStatusInput">
                    <option value="출근" ${currentStatus == "출근" ? "selected" : ""}>출근</option>
                    <option value="퇴근" ${currentStatus == "퇴근" ? "selected" : ""}>퇴근</option>
                    <option value="출근전" ${currentStatus == "출근전" ? "selected" : ""}>출근전</option>
                    <option value="휴가" ${currentStatus == "휴가" ? "selected" : ""}>휴가</option>
                </select>`;

        dateTimeCell.innerHTML = `<input type="datetime-local" class="form-control dateTimeInput" value="${dateTimeValue}"/>`;

        this.textContent = "완료";
        this.classList.remove("btn-warning");
        this.classList.add("btn-success");

      } else {
        const selectEl = row.querySelector(".workStatusInput");
        const newStatus = selectEl ? selectEl.value.trim() : "";
        const newDateTime = row.querySelector(".dateTimeInput")?.value || '';
        const attendanceId = row.dataset.attendanceId;

        fetch(`/employee/${username}/attendance/edit`, {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify({
            attendanceId: attendanceId,
            workStatus: newStatus,
            dateTime: newDateTime
          })
        })
            .then(res => res.json())
            .then(data => {
              if (data.success) {
                // 완료 후 span 표시
                workStatusCell.innerHTML = '';
                const span = document.createElement('span');
                span.className = 'badge badge-primary';
                span.textContent = newStatus;
                workStatusCell.appendChild(span);
                workStatusCell.dataset.status = newStatus;

                dateTimeCell.textContent = newDateTime.replace("T", " ");
                if (newStatus == "출근") dateTimeCell.dataset.checkin = newDateTime;
                else if (newStatus == "퇴근") dateTimeCell.dataset.checkout = newDateTime;

                this.textContent = "수정";
                this.classList.remove("btn-success");
                this.classList.add("btn-warning");
              } else {
                Swal.fire({
                  text: "수정에 실패했습니다.",
                  icon: "error"
                });
              }
            })
            .catch(err => {
              console.error(err);

              Swal.fire({
                text: "수정 중 오류가 발생했습니다.",
                icon: "error"
              });

            });
      }
    });
  }

  document.addEventListener("DOMContentLoaded", () => {
	  const englishInput = document.querySelector("input[name='englishName']");
	  if (englishInput) {
	    englishInput.addEventListener("input", (e) => {
	      // 1) 자동 대문자 변환
	      let value = e.target.value.toUpperCase();
	      // 2) 영문 대문자와 공백만 허용 (기타 문자 제거)
	      value = value.replace(/[^A-Z ]/g, '');
	      e.target.value = value;
	    });
	  }
	});

  // 기존 버튼에도 적용
  document.querySelectorAll(".updateAttendanceBtn").forEach(btn => attachUpdateEvent(btn));

</script>

</html>
