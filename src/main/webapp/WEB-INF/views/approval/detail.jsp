<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->

<%--      ${res.approvalFormId}
      ${res.formTitle}
      ${res.formContent}
      ${res.departmentId}--%>

			<div class="email-wrapper rounded border bg-white">
				<div class="row no-gutters justify-content-center">
				
				<div class="col-lg-4 col-xl-3 col-xxl-2">
					<div class="email-left-column email-options p-4 p-xl-5">
						<a href="email-compose.html" class="btn btn-block btn-primary btn-pill mb-4 mb-xl-5">새 결재 진행</a>
						
						<ul class="pb-2">
						  <li class="d-block active mb-4">
						    <a href="email-inbox.html">
						      <!-- <i class="mdi mdi-download mr-2"></i> --> 결재 대기 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">
						      <!-- <i class="mdi mdi-star-outline mr-2"></i> --> 결재 예정 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">
						      <!-- <i class="mdi mdi-playlist-edit mr-2"></i> --> 결재 요청 목록</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">
						      <!-- <i class="mdi mdi-open-in-new mr-2"></i> --> 결재 문서함</a>
						  </li>
						  <li class="d-block mb-4">
						    <a href="#">
						      <!-- <i class="mdi mdi-trash-can-outline mr-2"></i> --> 임시 저장 목록</a>
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
                  <th>기안부서</th>
                  <td>요청자 부서 자동입력</td>
                  <th>기안자</th>
                  <td>요청자 자동입력</td>
                </tr>

                <tr>
                  <th>기안일</th>
                  <td>현재 일자 자동입력</td>
                  <th>완료일</th>
                  <td>비워둠</td>
                </tr>

                </tbody>
              </table>

              <br>

              <table class="table table-bordered">
                <tbody>

                <%-- 신청란 --%>
                <tr>
                  <th class="align-middle" rowspan="3">신청</th>
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

                <%-- 승인란 --%>
                <tr>
                  <th class="align-middle" rowspan="3">승인</th>
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
              <%-- 1) 출장 신청 --%>
                <%--
                  <br>
                  <h5 class="p-3">출장 내용</h5>
                  <table class="table table-bordered">
                    <tbody>
                    <tr>
                      <th class="col-2">출장 기간</th>
                      <td></td>
                      <th class="col-2">출장지</th>
                      <td></td>
                    </tr>
                    <tr>
                      <th>교통편</th>
                      <td colspan="3"></td>
                    </tr>
                    <tr>
                      <th>출장목적</th>
                      <td colspan="3"></td>
                    </tr>
                    <tr>
                      <th>비고</th>
                      <td colspan="3"></td>
                    </tr>
                    </tbody>
                  </table>

                  <br>
                  <h5 class="p-3">출장자 정보</h5>
                  <table class="table table-bordered">
                    <tbody>
                    <tr>
                      <th class="col-2">성명</th>
                      <td></td>
                      <th class="col-2">직위</th>
                      <td></td>
                    </tr>
                    <tr>
                      <th>소속</th>
                      <td></td>
                      <th>전화번호</th>
                      <td></td>
                    </tr>
                    </tbody>
                  </table>

                  <!-- 출장여비 -->
                  <br>
                  <h5 class="p-3">출장여비</h5>
                  <table class="table table-bordered">
                    <tbody>
                    <tr>
                      <th class="col-2">구분</th>
                      <th>산출내역</th>
                      <th>금액</th>
                    </tr>
                    <tr>
                      <th>교통비</th>
                      <td></td>
                      <td></td>
                    </tr>
                    <tr>
                      <th>일비</th>
                      <td></td>
                      <td></td>
                    </tr>
                    <tr>
                      <th>식비</th>
                      <td></td>
                      <td></td>
                    </tr>
                    <tr>
                      <th>숙박비</th>
                      <td></td>
                      <td></td>
                    </tr>
                    <tr>
                      <th>기타</th>
                      <td></td>
                      <td></td>
                    </tr>
                    <tr>
                      <th class="text-center">계</th>
                      <th>asdf</th>
                      <th>100</th>
                    </tr>
                    </tbody>
                  </table>
                   --%>

              <%-- 2) 휴가 신청 --%>
              <%--
                <br>
              <table class="table table-bordered">
                <tbody>
                <tr>
                  <th class="col-2">휴가 종류</th>
                  <td></td>
                </tr>
                <tr>
                  <th>휴가 기간</th>
                  <td></td>
                </tr>
                <tr>
                  <th>사용일수</th>
                  <td></td>
                </tr>
                <tr>
                  <th>휴가 사유</th>
                  <td></td>
                </tr>
                </tbody>
              </table>
              --%>

              <%-- 3) 업무 기안 --%>
              <%--
                <br>
              <table class="table table-bordered">
                <tbody>
                <tr>
                  <th class="col-2">시행일자</th>
                  <td></td>
                  <th class="col-2">협조부서</th>
                  <td></td>
                </tr>
                <tr>
                  <th>합의</th>
                  <td colspan="3">소속부서명</td>
                </tr>
                <tr>
                  <th>제목</th>
                  <td colspan="3"></td>
                </tr>
                <tr>
                  <th>내용</th>
                  <td colspan="3"></td>
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