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
	
	<script defer type="text/javascript" src="/js/approval_list.js"></script>
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
                            <select class="form-control" id="departmentId">
                              <c:forEach items="${departmentList }" var="el">
                                  <option value="${el.departmentId}">${el.departmentName}</option>
                              </c:forEach>
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
                                <a href="/approval/list?username=team_leader">전체</a> <%-- TODO 쿼리파라미터에서 username 삭제 --%>
                            </li>
                            <li class="d-block mb-4">
						        <a href="/approval/list?listType=request&username=team_leader">요청 목록</a> <%-- TODO 쿼리파라미터에서 username 삭제 --%>
                            </li>
                            <li class="d-block mb-4">
                                <a href="/approval/list?listType=wait&username=team_leader">대기 목록</a> <%-- TODO 쿼리파라미터에서 username 삭제 --%>
                            </li>
                            <li class="d-block mb-4">
                                <a href="/approval/list?listType=storage&username=team_leader">완료 목록</a> <%-- TODO 쿼리파라미터에서 username 삭제 --%>
                            </li>
                            <li class="d-block mb-4">
                                <a href="/approval/list?listType=temp&username=team_leader">임시보관함</a> <%-- TODO 쿼리파라미터에서 username 삭제 --%>
                            </li>
						</ul>
					</div>
				</div>
				
				<div class="col-lg-8 col-xl-9 col-xxl-10">
					<div class="email-right-column p-4 p-xl-5">

                        <c:choose>

                            <c:when test="${reqList ne null and waitList ne null }">
                                <!-- section1 - 결재 요청 목록 -->
                                <section class="email-details pl-4 pr-4 pt-4">

                                    <div class="d-flex justify-content-between align-items-center">
                                        <h4 class="text-dark">결재 요청 목록</h4>
                                        <button type="button" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></button>
                                    </div>

                                    <div class="email-details-content pl-0 pr-0">
                                        <table class="table">

                                          <thead>
                                            <tr>
                                              <th class="col-2">기안일</th>
                                              <th class="col-6">제목</th>
                                              <th class="col-1">파일</th>
                                              <th class="col-2">부서</th>
                                              <th class="col-1">상태</th>
                                            </tr>
                                          </thead>

                                          <tbody>
                                            <c:forEach items="${reqList }" var="el">
                                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                                  <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                                  <td>${el.formTitle }</td>
                                                  <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                                  <td>${el.departmentName }</td>
                                                  <td>
                                                    <c:choose>
                                                        <c:when test="${(el.status eq 'Y'.charAt(0)) or (el.status eq 'y'.charAt(0)) }">
                                                            <span class="badge badge-info">승인</span>
                                                        </c:when>
                                                        <c:when test="${(el.status eq 'N'.charAt(0)) or (el.status eq 'n'.charAt(0)) }">
                                                            <span class="badge badge-danger">반려</span>
                                                        </c:when>
                                                        <c:when test="${(el.status eq 'W'.charAt(0)) or (el.status eq 'w'.charAt(0)) }">
                                                            <span class="badge badge-light">대기</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-outline-info">임시저장</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                          </tbody>

                                        </table>
                                    </div>

                                </section>
                                <!--  -->

                                <!-- section2 - 결재 대기 목록 -->
                                <section class="email-details pl-4 pr-4 pt-4">

                                    <div class="d-flex justify-content-between align-items-center">
                                        <h4 class="text-dark">결재 대기 목록</h4>
                                        <button type="button" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></button>
                                    </div>

                                    <div class="email-details-content pl-0 pr-0">
                                        <table class="table">

                                          <thead>
                                            <tr>
                                              <th class="col-2">기안일</th>
                                              <th class="col-4">제목</th>
                                              <th class="col-1">파일</th>
                                              <th class="col-2">기안부서</th>
                                              <th class="col-2">기안자</th>
                                              <th class="col-1">상태</th>
                                            </tr>
                                          </thead>

                                          <tbody>
                                            <c:forEach items="${waitList }" var="el">
                                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                                  <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                                  <td>${el.formTitle }</td>
                                                  <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                                  <td>${el.departmentName }</td>
                                                  <td>${el.username }</td>
                                                  <td>
                                                    <c:choose>
                                                        <c:when test="${(el.status eq 'Y'.charAt(0)) or (el.status eq 'y'.charAt(0)) }">
                                                            <span class="badge badge-info">승인</span>
                                                        </c:when>
                                                        <c:when test="${(el.status eq 'N'.charAt(0)) or (el.status eq 'n'.charAt(0)) }">
                                                            <span class="badge badge-danger">반려</span>
                                                        </c:when>
                                                        <c:when test="${(el.status eq 'W'.charAt(0)) or (el.status eq 'w'.charAt(0)) }">
                                                            <span class="badge badge-light">대기</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-outline-info">임시저장</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                          </tbody>

                                        </table>
                                    </div>

                                </section>
                                <!--  -->

                                <!-- section3 - 결재 완료 목록 -->
                                <section class="email-details pl-4 pr-4 pt-4">

                                    <div class="d-flex justify-content-between align-items-center">
                                        <h4 class="text-dark">결재 완료 목록</h4>
                                        <button type="button" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></button>
                                    </div>

                                    <div class="email-details-content pl-0 pr-0">
                                        <table class="table">

                                            <thead>
                                            <tr>
                                                <th class="col-2">기안일</th>
                                                <th class="col-4">제목</th>
                                                <th class="col-1">파일</th>
                                                <th class="col-2">기안부서</th>
                                                <th class="col-2">기안자</th>
                                                <th class="col-1">상태</th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach items="${storList }" var="el">
                                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                                    <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                                    <td>${el.formTitle }</td>
                                                    <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                                    <td>${el.departmentName }</td>
                                                    <td>${el.username }</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${(el.status eq 'Y'.charAt(0)) or (el.status eq 'y'.charAt(0)) }">
                                                                <span class="badge badge-info">승인</span>
                                                            </c:when>
                                                            <c:when test="${(el.status eq 'N'.charAt(0)) or (el.status eq 'n'.charAt(0)) }">
                                                                <span class="badge badge-danger">반려</span>
                                                            </c:when>
                                                            <c:when test="${(el.status eq 'W'.charAt(0)) or (el.status eq 'w'.charAt(0)) }">
                                                                <span class="badge badge-light">대기</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge badge-outline-info">임시저장</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>

                                        </table>
                                    </div>

                                </section>
                                <!--  -->

                                <!-- section4 - 임시 보관함 -->
                                <section class="email-details pl-4 pr-4 pt-4">

                                    <div class="d-flex justify-content-between align-items-center">
                                        <h4 class="text-dark">임시 보관함</h4>
                                        <button type="button" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></button>
                                    </div>

                                    <div class="email-details-content pl-0 pr-0">
                                        <table class="table">

                                            <thead>
                                            <tr>
                                                <th class="col-2">기안일</th>
                                                <th class="col-6">제목</th>
                                                <th class="col-1">파일</th>
                                                <th class="col-2">부서</th>
                                                <th class="col-1">상태</th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach items="${tempList }" var="el">
                                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                                    <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                                    <td>${el.formTitle }</td>
                                                    <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                                    <td>${el.departmentName }</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${(el.status eq 'Y'.charAt(0)) or (el.status eq 'y'.charAt(0)) }">
                                                                <span class="badge badge-info">승인</span>
                                                            </c:when>
                                                            <c:when test="${(el.status eq 'N'.charAt(0)) or (el.status eq 'n'.charAt(0)) }">
                                                                <span class="badge badge-danger">반려</span>
                                                            </c:when>
                                                            <c:when test="${(el.status eq 'W'.charAt(0)) or (el.status eq 'w'.charAt(0)) }">
                                                                <span class="badge badge-light">대기</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge badge-outline-info">임시저장</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>

                                        </table>
                                    </div>

                                </section>
                                <!--  -->
                            </c:when>

                            <c:when test="${request ne null or temp ne null}">
                                <!-- 결재 요청 목록 / 임시 저장 목록 -->
                                <section class="email-details pl-4 pr-4 pt-4">

                                    <div class="d-flex justify-content-between align-items-center">
                                        <h4 class="text-dark">${request ne null ? '결재 요청 목록' : '임시 보관함'}</h4>
                                        <button type="button" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></button>
                                    </div>

                                    <div class="email-details-content pl-0 pr-0">
                                        <table class="table">

                                            <thead>
                                            <tr>
                                                <th class="col-2">기안일</th>
                                                <th class="col-6">제목</th>
                                                <th class="col-1">파일</th>
                                                <th class="col-2">부서</th>
                                                <th class="col-1">상태</th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach items="${request ne null ? request : temp }" var="el">
                                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                                    <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                                    <td>${el.formTitle }</td>
                                                    <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                                    <td>${el.departmentName }</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${(el.status eq 'Y'.charAt(0)) or (el.status eq 'y'.charAt(0)) }">
                                                                <span class="badge badge-info">승인</span>
                                                            </c:when>
                                                            <c:when test="${(el.status eq 'N'.charAt(0)) or (el.status eq 'n'.charAt(0)) }">
                                                                <span class="badge badge-danger">반려</span>
                                                            </c:when>
                                                            <c:when test="${(el.status eq 'W'.charAt(0)) or (el.status eq 'w'.charAt(0)) }">
                                                                <span class="badge badge-light">대기</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge badge-outline-info">임시저장</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>

                                        </table>
                                    </div>

                                </section>
                                <!--  -->
                            </c:when>

                            <c:when test="${wait ne null or storage ne null}">
                                <!-- 결재 대기 목록 / 결재 문서함 -->
                                <section class="email-details pl-4 pr-4 pt-4">

                                    <div class="d-flex justify-content-between align-items-center">
                                        <h4 class="text-dark">${wait ne null ? '결재 대기 목록': '결재 완료 목록'}</h4>
                                        <button type="button" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></button>
                                    </div>

                                    <div class="email-details-content pl-0 pr-0">
                                        <table class="table">

                                            <thead>
                                            <tr>
                                                <th class="col-2">기안일</th>
                                                <th class="col-4">제목</th>
                                                <th class="col-1">파일</th>
                                                <th class="col-2">기안부서</th>
                                                <th class="col-2">기안자</th>
                                                <th class="col-1">상태</th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach items="${wait ne null ? wait : storage }" var="el">
                                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                                    <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                                    <td>${el.formTitle }</td>
                                                    <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                                    <td>${el.departmentName }</td>
                                                    <td>${el.username }</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${(el.status eq 'Y'.charAt(0)) or (el.status eq 'y'.charAt(0)) }">
                                                                <span class="badge badge-info">승인</span>
                                                            </c:when>
                                                            <c:when test="${(el.status eq 'N'.charAt(0)) or (el.status eq 'n'.charAt(0)) }">
                                                                <span class="badge badge-danger">반려</span>
                                                            </c:when>
                                                            <c:when test="${(el.status eq 'W'.charAt(0)) or (el.status eq 'w'.charAt(0)) }">
                                                                <span class="badge badge-light">대기</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge badge-outline-info">임시저장</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>

                                        </table>
                                    </div>

                                </section>
                                <%--  --%>
                            </c:when>

                        </c:choose>

                        <%-- 페이징 --%>
                        <c:if test="${request ne null or wait ne null}">
                            <br>
                            <nav class="d-flex justify-content-center">
                                <ul class="pagination pagination-flat pagination-flat-rounded">
                                    <li class="page-item">
                                        <a class="page-link" href="#" aria-label="Previous">
                                            <span aria-hidden="true" class="mdi mdi-chevron-left"></span>
                                            <span class="sr-only">Previous</span>
                                        </a>
                                    </li>
                                    <li class="page-item active">
                                        <a class="page-link" href="#">1</a>
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="#">2</a>
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="#">3</a>
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="#" aria-label="Next">
                                            <span aria-hidden="true" class="mdi mdi-chevron-right"></span>
                                            <span class="sr-only">Next</span>
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                        </c:if>

					</div>
				</div>
				
				</div>
			</div>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>