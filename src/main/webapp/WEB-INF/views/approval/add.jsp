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
            <ul class="list-unstyled">
              <c:forEach items="${employeeList }" var="el">
                <li class="text-start">
                  <a data-approval-form-id="${el.username }" class="approval-form-name btn px-0 mr-3 text-dark">${el.name }</a>
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
                <button data-bs-toggle="modal" data-bs-target="#selectApproverModal" id="selectApprover" type="button" class="btn btn-block btn-primary"><i class="mdi mdi-plus mr-1"></i>결재선 지정</button>
              </div>
						</div>
						<hr>
            <%--  --%>

            <%-- 양식 내용 --%>
            <form action="" style="max-width: 720px">
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

              <%-- 2. 결재 양식 종류에 따라 다른 HTML을 뿌림 --%>
              <c:choose>
                <c:when test="${form.approvalFormId eq 1}">
                  <c:import url="/WEB-INF/views/approval/form_type/vacation.jsp"/>
                </c:when>
                <c:when test="${form.approvalFormId eq 2}">
                  <c:import url="/WEB-INF/views/approval/form_type/business_trip.jsp"/>
                </c:when>
                <c:when test="${form.approvalFormId eq 3}">
                  <c:import url="/WEB-INF/views/approval/form_type/draft.jsp"/>
                </c:when>
              </c:choose>

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

