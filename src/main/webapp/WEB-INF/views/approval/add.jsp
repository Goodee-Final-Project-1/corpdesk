<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script defer type="text/javascript" src="/js/approval/approval.js"></script>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<!-- Modal -->
<div class="modal fade" id="newApprovalModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">

            <div class="modal-header">
                <h5 class="modal-title fs-5" id="exampleModalLabel">결재 양식 선택</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <div class="modal-body d-flex justify-content-between">

                <!-- left col -->
                <div class="card mb-4 p-0 w-75">

                    <div class="card-body p-4">
                        <ul class="list-unstyled">
                            <c:forEach items="${formList }" var="el">
                                <li class="text-start">
                                    <a data-approval-form-id="${el.approvalFormId }" class="approval-form-name btn px-0 mr-3 text-dark">${el.formTitle }</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>

                </div>

                <!-- right col -->
                <div class="card mb-4 p-0 ml-2 w-100">

                    <h5 class="card-title pt-4 px-4">상세정보</h5>

                    <div class="card-body p-4">
                        <ul class="list-unstyled">
                            <li class="d-flex py-2 text-dark">
                                <b class="col-5 p-0">제목</b>
                                <p id="approvalTitle"></p>
                            </li>
                            <li class="d-flex py-2 text-dark">
                                <b class="col-5 p-0">기안부서</b>
                                <p>asdfasdf</p>
                            </li>
                            <li class="d-flex py-2 text-dark align-items-center">
                                <label class="col-5 p-0">결재부서</label>
                                <div class="form-group">
                                    <select class="form-control" id="vacationType">

                                        <option value="연차">${el.departmentName}</option>

                                    </select>
                                </div>
                            </li>
                        </ul>
                    </div>

                </div>

            </div>

            <div class="modal-footer">
                <button type="button" id="formCheck" class="btn btn-info">확인</button>
                <button type="button" class="btn btn-light" data-bs-dismiss="modal">취소</button>
            </div>

        </div>
    </div>
</div>
<!--  -->

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<div class="email-wrapper rounded border bg-white">
				<div class="row no-gutters justify-content-center">
				
				<div class="col-lg-4 col-xl-3 col-xxl-2">
					<div class="email-left-column email-options p-4 p-xl-5">
                        <a data-bs-toggle="modal" data-bs-target="#newApprovalModal" id="newApproval" class="btn btn-block btn-primary btn-pill mb-4 mb-xl-5">새 결재 진행</a>

                        <ul class="pb-2">
                            <li class="d-block mb-4">
                                <a href="/approval/list?username=jung_frontend">전체</a> <%-- TODO 쿼리파라미터에서 username 삭제 --%>
                            </li>
                            <li class="d-block mb-4">
                                <a href="/approval/list?listType=request&username=jung_frontend">요청 목록</a> <%-- TODO 쿼리파라미터에서 username 삭제 --%>
                            </li>
                            <li class="d-block mb-4">
                                <a href="/approval/list?listType=wait&username=jung_frontend">대기 목록</a> <%-- TODO 쿼리파라미터에서 username 삭제 --%>
                            </li>
                            <li class="d-block mb-4">
                                <a href="/approval/list?listType=storage&username=jung_frontend">완료 목록</a> <%-- TODO 쿼리파라미터에서 username 삭제 --%>
                            </li>
                            <li class="d-block mb-4">
                                <a href="/approval/list?listType=temp&username=jung_frontend">임시보관함</a> <%-- TODO 쿼리파라미터에서 username 삭제 --%>
                            </li>
                        </ul>
					</div>
				</div>
				
				<div class="col-lg-8 col-xl-9 col-xxl-10">
					<div class="email-right-column p-4 p-xl-5">
						
						<!-- 양식 시작 -->
            <%-- 양식 헤더 --%>
						<div class="d-flex justify-content-between">
              <div>
						  	<button type="button" class="btn btn-info mr-1" data-toggle="modal" data-target="#modal-add-contact">결재 요청</button>
						  	<button type="button" class="btn btn-outline-info mr-1" data-toggle="modal" data-target="#modal-add-contact">임시저장</button>
							  <button type="button" class="btn btn-light" data-toggle="modal" data-target="#modal-add-contact">취소</button>
              </div>
              <div>
                <button type="button" class="btn btn-block btn-primary"><i class="mdi mdi-plus mr-1"></i>결재선 지정</button>
              </div>
						</div>
						<hr>
            <%--  --%>

            <%-- 양식 내용 --%>
            <form action="" style="max-width: 720px">
              <%-- 1. 공통 양식 --%>
              <h1 class="text-center p-4">양식 제목</h1>

              <table class="table table-bordered">
                <tbody>

                <tr>
                  <th class="col-2 table-light">기안부서</th>
                  <td class="col-4">${userInfo.departmentName}</td>
                  <th class="col-2 table-light">기안자</th>
                  <td class="col-4">${userInfo.name}</td>
                </tr>

                <tr>
                  <th class="table-light">기안일</th>
                  <td>${today}</td>
                  <th class="table-light">완료일</th>
                  <td></td>
                </tr>

                </tbody>
              </table>
              <span>※ 기안일은 [결재 요청] 버튼을 클릭한 날짜로 확정됩니다.</span>
              <br><br>

              <table class="table table-bordered">
                <tbody>

                <%-- 신청란 --%>
<%--                <tr>--%>
<%--                  <th class="align-middle col-2" rowspan="3">신청</th>--%>
<%--                  <td>팀장</td>--%>
<%--                </tr>--%>

<%--                <tr>--%>
<%--                  <td>이유미</td>--%>
<%--                </tr>--%>

<%--                <tr>--%>
<%--                  <td>Lucia</td>--%>
<%--                </tr>--%>
                <%--  --%>

                <%-- 승인란 --%>
                <tr>
                  <th class="align-middle table-light" rowspan="3">승인</th>
                  <td>팀장</td>
                  <td>실장</td>
                  <td>팀장</td>
                  <td>실장</td>
                </tr>

                <tr>
                  <td>이유미</td>
                  <td>박우진</td>
                  <td>이유미</td>
                  <td>박우진</td>
                </tr>

                <tr>
                  <td>Lucia</td>
                  <td>Christ</td>
                  <td>Lucia</td>
                  <td>Christ</td>
                </tr>
                <%--  --%>
                </tbody>
              </table>
              <%--  --%>

              <%-- 결재 양식 종류에 따라 다른 HTML을 뿌림 --%>

<%--              <br>--%>
<%--              ${form.formContent}--%>

              <%-- 1) 출장 신청 --%>

                  <%--
                  <br>
                  <h5 class="p-3">출장 내용</h5>
                  <table class="table table-bordered">
                    <tbody>
                    <tr>
                      <th class="col-2 table-light align-middle">출장 기간</th>
                      <td class="col-4">
                          <div class="d-flex align-items-center">
                            <input type="date" class="form-control col-sm-10 mb-2"><p>&nbsp;&nbsp;&nbsp;&nbsp;~</p>
                          </div>
                          <input type="date" class="form-control col-sm-10">
                      </td>
                      <th class="col-2 table-light align-middle">출장지</th>
                      <td class="col-4 align-middle"><input type="text" class="form-control"></td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">교통편</th>
                      <td colspan="3"><input type="text" class="form-control"></td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">출장목적</th>
                      <td colspan="3"><input type="text" class="form-control"></td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">비고</th>
                      <td colspan="3"><input type="text" class="form-control"></td>
                    </tr>
                    </tbody>
                  </table>

                  <br>
                  <h5 class="p-3">출장자 정보</h5>
                  <table class="table table-bordered">
                    <tbody>
                    <tr>
                      <th class="col-2 table-light align-middle">성명</th>
                      <td><input type="text" class="form-control"></td>
                      <th class="col-2 table-light align-middle">직위</th>
                      <td><input type="text" class="form-control"></td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">소속</th>
                      <td><input type="text" class="form-control"></td>
                      <th class="table-light align-middle">전화번호</th>
                      <td><input type="text" class="form-control"></td>
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
                      <td><input type="text" class="form-control"></td>
                      <td><input type="number" class="form-control amount-input"></td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">일비</th>
                        <td><input type="text" class="form-control"></td>
                        <td><input type="number" class="form-control amount-input"></td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">식비</th>
                        <td><input type="text" class="form-control"></td>
                        <td><input type="number" class="form-control amount-input"></td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">숙박비</th>
                        <td><input type="text" class="form-control"></td>
                        <td><input type="number" class="form-control amount-input"></td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">기타</th>
                        <td><input type="text" class="form-control"></td>
                        <td><input type="number" class="form-control amount-input"></td>
                    </tr>
                    <tr>
                      <th class="table-light">계</th>
                      <th></th>
                      <th id="total-amount" class="text-right">0</th>
                    </tr>
                    </tbody>
                  </table>
                  --%>


              <%-- 2) 휴가 신청 --%>

                <br>
              <table class="table table-bordered">
                <tbody>
                <tr>
                  <th class="col-2 table-light align-middle">휴가 종류</th>
                  <td>
                      <div class="form-group">
                          <select class="form-control" id="departmentId">
                              <c:forEach items="${departmentList }" var="el">
                                  <option value="${el.departmentId}">${el.departmentName}</option>
                              </c:forEach>
                          </select>
                      </div>
                  </td>
                </tr>
                <tr>
                  <th class="table-light align-middle">휴가 기간</th>
                    <td>
                        <div class="d-flex align-items-center">
                            <input type="date" class="form-control col-sm-5 mb-2"><p>&nbsp;&nbsp;&nbsp;&nbsp;~</p>
                        </div>
                        <input type="date" class="form-control col-sm-5">
                    </td>
                </tr>
                <tr>
                  <th class="table-light align-middle">사용일수</th>
                  <td><input type="text" class="form-control"></td>
                </tr>
                <tr>
                  <th class="table-light align-middle">휴가 사유</th>
                  <td><input type="text" class="form-control"></td>
                </tr>
                </tbody>
              </table>


              <%-- 3) 업무 기안 --%>
<%--
                <br>
              <table class="table table-bordered">
                <tbody>
                <tr>
                  <th class="col-2">시행일자</th>
                  <td><input type="date" class="form-control"></td>
                  <th class="col-2">협조부서</th>
                  <td>
                      <div class="form-group">
                          <select class="form-control" id="departmentId">
                              <c:forEach items="${departmentList }" var="el">
                                  <option value="${el.departmentId}">${el.departmentName}</option>
                              </c:forEach>
                          </select>
                      </div>
                  </td>
                </tr>
                <tr>
                  <th>합의</th>
                  <td colspan="3">${userInfo.departmentName}</td>
                </tr>
                <tr>
                  <th>제목</th>
                  <td colspan="3"><input type="text" class="form-control"></td>
                </tr>
                <tr>
                  <th>내용</th>
                  <td colspan="3">
                      <textarea class="form-control" rows="5"></textarea>
                  </td>
                </tr>
                </tbody>
              </table>
--%>
            </form>
            <%--  --%>
						<!--  -->
					
					</div>
				</div>
				
				</div>
			</div>
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>

<script>
    /**
     * 출장 신청 폼 - 금액 합계 자동 계산
     */
    // 금액 입력 필드들을 모두 선택
    const amountInputs = document.querySelectorAll('.amount-input');
    const totalDisplay = document.getElementById('total-amount');

    // 총합을 계산하는 함수
    function calculateTotal() {
        let total = 0;

        amountInputs.forEach(input => {
            const value = parseFloat(input.value) || 0; // 빈 값이면 0으로 처리
            total += value;
        });

        // 총합을 천 단위 구분자와 함께 표시
        totalDisplay.textContent = total.toLocaleString('ko-KR');
    }

    // 각 금액 입력 필드에 이벤트 리스너 추가
    amountInputs.forEach(input => {
        // 값이 변경될 때마다 총합 계산
        input.addEventListener('input', calculateTotal);

        // 포커스가 벗어날 때도 총합 계산 (더 확실하게)
        input.addEventListener('blur', calculateTotal);
    });

    // 페이지 로드 시 초기 계산
    calculateTotal();

    /**
     * 휴가 신청 폼 - 휴가 사용 일수 자동 계산
     */
        // 날짜 입력 필드 참조
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const usedDaysInput = document.getElementById('usedDays');

    // 평일(주말 제외) 계산 함수
    function calculateWeekdays(startDate, endDate) {
        if (!startDate || !endDate || startDate > endDate) {
            return 0;
        }

        let count = 0;
        let currentDate = new Date(startDate);

        while (currentDate <= endDate) {
            const dayOfWeek = currentDate.getDay();
            // 0: 일요일, 6: 토요일 제외
            if (dayOfWeek !== 0 && dayOfWeek !== 6) {
                count++;
            }
            currentDate.setDate(currentDate.getDate() + 1);
        }

        return count;
    }

    // 사용일수 업데이트 함수
    function updateUsedDays() {
        const startDate = startDateInput.value ? new Date(startDateInput.value) : null;
        const endDate = endDateInput.value ? new Date(endDateInput.value) : null;

        if (startDate && endDate) {
            if (startDate > endDate) {
                usedDaysInput.value = '종료일이 시작일보다 빨라요';
                usedDaysInput.style.color = '#dc3545';
            } else {
                const weekdays = calculateWeekdays(startDate, endDate);
                usedDaysInput.value = `${weekdays}일`;
                usedDaysInput.style.color = '#198754';
            }
        } else {
            usedDaysInput.value = '';
            usedDaysInput.style.color = '#6c757d';
        }
    }

    // 이벤트 리스너 추가
    startDateInput.addEventListener('change', updateUsedDays);
    endDateInput.addEventListener('change', updateUsedDays);

    // 시작일 변경 시 종료일 최소값 설정
    startDateInput.addEventListener('change', function() {
        if (this.value) {
            endDateInput.min = this.value;
            // 종료일이 시작일보다 빠른 경우 초기화
            if (endDateInput.value && endDateInput.value < this.value) {
                endDateInput.value = '';
            }
        }
    });
</script>