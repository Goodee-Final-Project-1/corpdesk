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

    <script defer type="text/javascript" src="/js/approval/approval_detail.js"></script>

    <style>
        /* 테이블 전체의 위쪽 테두리 제거 */
        .no-top-border {
            border-top: none;
        }

        /* 테이블 헤더(thead) 내부의 th/td 셀의 위쪽 테두리 제거 */
        /* table-bordered 클래스에 의해 셀에도 테두리가 적용되므로 이를 제거합니다. */
        .no-top-border thead th,
        .no-top-border thead td {
            border-top: none;
        }

        /* Bootstrap 5의 경우, thead, tbody, tfoot 요소에도 기본적으로 border-top이 적용될 수 있습니다. */
        .no-top-border > :not(caption) > * > * {
            border-top: none;
        }
    </style>
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

<!-- Modal2 - 결재선 지정 -->
<div class="modal fade" id="selectApproverModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title fs-5" id="exampleModalLabel">결재선 지정</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <div class="modal-body d-flex justify-content-between">

        <!-- left col -->
        <div class="card mb-4 p-0 w-75">

          <div class="card-body p-4">
            <ul class="list-unstyled" id="employee-list">
              <c:forEach items="${employeeList }" var="el">
                  <c:if test="${el.username ne userInfo.username}"> <%-- 기안자를 제외한 나머지 직원들의 데이터만 뿌림 --%>
                        <li class="text-start d-flex align-items-center" draggable="true"
                            data-username="${el.username}" data-name="${el.name}"
                            data-department-name="${el.departmentName}" data-position-name="${el.positionName}">
                            <div class="col-lg-3">
                                <img class="mr-2 img-fluid" style="border-radius: 50%;"
                                     src="${el.oriName ne null ?
                                                '/files/employee/' += el.oriName += '.' += el.extension
                                                : '/images/default_profile.jpg'}"> <%-- TODO 프로필이미지가 정확히 어떤 경로에 저장되는지 준수님과 이야기해야 함 --%>
                            </div>
                            <a class="approval-form-name btn px-0 mr-3 text-dark">${el.departmentName } ${el.positionName } ${el.name }</a>
                        </li>
                  </c:if>
              </c:forEach>
            </ul>
          </div>

        </div>

        <!-- right col -->
        <div class="card mb-4 p-0 ml-2 w-75">

<%--          <h5 class="card-title pt-4 px-4">상세정보</h5>--%>

          <div class="card-body p-4" id="approver-drop-area">
              <ul id="approver-list" class="list-unstyled"></ul>
          </div>

        </div>

      </div>

      <div class="modal-footer">
        <button type="button" id="approverCheck" class="btn btn-info" data-bs-dismiss="modal">확인</button>
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
						<div data-approval-id="${res.approvalId}">
              <button type="button" class="btn btn-info mr-1 btn-action" id="btnEdit">수정</button>
              <button type="button" class="btn btn-outline-danger mr-1 btn-action" id="btnDelete">삭제</button>
              <button type="button" class="btn btn-light" id="btnCancel">취소</button>
						</div>
						<hr>
            <%--  --%>

            <%-- 양식 내용 --%>
            <form id="approvalContentCommon">
              <%-- 1. 공통 양식 --%>
              <h1 class="text-center p-4">${form.formTitle}</h1>

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
                <%-- 승인란 --%>
                <tr id="approverPosition">
                    <th class="align-middle table-light col-2" rowspan="3">승인</th>
                    <div>
                        <td class="text-center" style="width: 20%;">&nbsp;</td>
                        <td class="text-center" style="width: 20%;"></td>
                        <td class="text-center" style="width: 20%;"></td>
                        <td class="text-center" style="width: 20%;"></td>
                    </div>
                </tr>

                <tr id="approverName" style="height: 90px;">
                    <div>
                        <td class="text-center align-middle"></td>
                        <td class="text-center align-middle"></td>
                        <td class="text-center align-middle"></td>
                        <td class="text-center align-middle"></td>
                    </div>
                </tr>

                <tr id="approvalDate">
                    <div>
                        <td class="text-center">&nbsp;</td>
                        <td class="text-center"></td>
                        <td class="text-center"></td>
                        <td class="text-center"></td>
                    </div>
                </tr>
                <%--  --%>
                </tbody>
              </table>
              <%--  --%>
              <div id="newApproverRow"></div>

              <%-- 서버 전송용 input hidden --%>
              <input type="hidden" name="username" value="${userInfo.username}">
              <input type="hidden" name="departmentId" value="${targetDept.departmentId}">
              <input type="hidden" name="approvalFormId" value="${form.approvalFormId}">
            </form>

            ${res}
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

