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
	  <div class="modal-dialog modal-dialog-centered">
	    <div class="modal-content">
	    
	      <div class="modal-header">
	        <h5 class="modal-title fs-5" id="exampleModalLabel">결재 양식 선택</h5>
	        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	      
	      <div class="modal-body d-flex justify-content-between">
	        	
        	<!-- left col -->
        	<div class="card mb-4 p-0 w-50">
	          
					<div class="card-body p-4">
						<ul class="list-unstyled">
                            <%-- TODO 아래 li들의 1, 2, 3은 예시이고, 최종적으로는 서버에서 값을 뿌려 줘야 함 --%>
							<li class="text-start">
                                <a data-approval-form-id="1" class="approval-form-name btn px-0 mr-3 text-dark">업무 기안</a>
                            </li>
							<li class="text-start">
                                <a data-approval-form-id="2" class="approval-form-name btn px-0 mr-3 text-dark">출장 신청</a>
                            </li>
							<li class="text-start">
                                <a data-approval-form-id="3" class="approval-form-name btn px-0 mr-3 text-dark">휴가 신청</a>
                            </li>
						</ul>
					</div>
	          
	        </div>
        	
        	<!-- right col -->
              <%-- TODO 모달을 맨 처음 열었을 때는 맨첫번째 항목에 대한 상세정보 표출 --%>
		      <div class="card mb-4 p-0 ml-2 w-100">
	          
	          <h5 class="card-title pt-4 px-4">상세정보</h5>
	          
	          <div class="card-body p-4">
	            <ul class="list-unstyled">
	            	<li class="d-flex py-2 text-dark">
	            		<b class="col-5 p-0">제목</b>
	            		<p id="approvalTitle">asdfasdf</p>
	            	</li>
	            	<li class="d-flex py-2 text-dark">
	            		<b class="col-5 p-0">기안부서</b>
	            		<p>asdfasdf</p>
	            	</li>
	            	<li class="d-flex py-2 text-dark align-items-center">
	            		<label class="col-5 p-0">결재부서</label>
	            		<div class="form-group">
	 								  <select class="form-control" id="departmentId">
								      <option value="1">1</option>
								      <option value="2">2</option>
								      <option value="3">3</option>
								      <option value="4">4</option>
								      <option value="5">5</option>
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
						  <li class="d-block active mb-4">
						    <a href="email-inbox.html">결재 대기 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">결재 예정 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">결재 요청 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">결재 문서함</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">임시 저장 목록</a>
						  </li>
						</ul>
					</div>
				</div>
				
				<div class="col-lg-8 col-xl-9 col-xxl-10">
					<div class="email-right-column p-4 p-xl-5">
						
						<!-- section1 - 결재 요청 문서 목록 -->
						<section class="email-details pl-4 pr-4 pt-4">
							
							<div class="d-flex justify-content-between align-items-center">
								<h4 class="text-dark">결재 요청 문서 목록</h4>
								<button type="button" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></button>
							</div>
							
							<div class="email-details-content pl-0 pr-0">
								<table class="table">
								  
								  <thead>
								    <tr>
								      <th class="col-2">기안일</th>
								      <th class="col-6">제목</th>
								      <th class="col-2">부서</th>
								      <th class="col-2">상태</th>
								    </tr>
								  </thead>
								  
								  <tbody>
								  	<c:forEach items="${reqList }" var="el">
									    <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
									      <td>${fn:substring(el.createdAt, 0, 10) }</td>
									      <td>${el.formType }</td>
									      <td>${el.departmentId }</td>
									      <td>
									      	<c:choose>
									      		<c:when test="${el.status eq 89}">
									      			<span class="badge badge-info">승인</span>
									      		</c:when>
									      		<c:when test="${el.status eq 78}">
									      			<span class="badge badge-danger">반려</span>
									      		</c:when>
									      		<c:when test="${el.status eq 87}">
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
						
						<!-- section2 - 결재 대기 문서 목록 -->
						<section class="email-details pl-4 pr-4 pt-4">
							
							<div class="d-flex justify-content-between align-items-center">
								<h4 class="text-dark">결재 대기 문서 목록</h4>
								<button type="button" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></button>
							</div>
							
							<div class="email-details-content pl-0 pr-0">
								<table class="table">
								  
								  <thead>
								    <tr>
								      <th class="col-2">기안일</th>
								      <th class="col-4">제목</th>
								      <th class="col-2">기안부서</th>
								      <th class="col-2">기안자</th>
								      <th class="col-2">상태</th>
								    </tr>
								  </thead>
								  
								  <tbody>
								  	<c:forEach items="${waitList }" var="el">
									    <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
									      <td>${fn:substring(el.createdAt, 0, 10) }</td>
									      <td>${el.formType }</td>
									      <td>${el.departmentId }</td>
									      <td>${el.username }</td>
									      <td>
									      	<c:choose>
									      		<c:when test="${el.status eq 89}">
									      			<span class="badge badge-info">승인</span>
									      		</c:when>
									      		<c:when test="${el.status eq 78}">
									      			<span class="badge badge-danger">반려</span>
									      		</c:when>
									      		<c:when test="${el.status eq 87}">
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
					
					</div>
				</div>
				
				</div>
			</div>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>