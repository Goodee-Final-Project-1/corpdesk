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
<c:import url="modal.jsp"/>

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<div class="email-wrapper rounded border bg-white">
				<div class="row no-gutters justify-content-center">

				<div class="col-lg-4 col-xl-3 col-xxl-2">
					<div class="email-left-column email-options p-4 p-xl-5">
            <c:import url="widget_content.jsp"/>
					</div>
				</div>

				<div class="col-lg-8 col-xl-9 col-xxl-10">
					<div class="email-right-column p-4 p-xl-5">

						<!-- 양식 시작 -->
            <%-- 양식 헤더 --%>
						<div id="btnBox" data-approval-id="${detail.approvalId}" data-approver-id="${approverInfo.approverId}">
              <c:choose>
                <c:when test="${detail.username eq userInfo.username}">
                  <button type="button" class="btn btn-info mr-1 btn-action" id="btnEdit">수정</button>
                  <button type="button" class="btn btn-outline-danger mr-1 btn-action" id="btnDelete">삭제</button>
                </c:when>
                <c:otherwise>
                  <button type="button" class="btn btn-info mr-1 btn-action" id="btnApproval">승인</button>
                  <button type="button" class="btn btn-outline-danger mr-1 btn-action" id="btnReject">반려</button>
                </c:otherwise>
              </c:choose>
              <button type="button" class="btn btn-light" id="btnCancel">취소</button>
						</div>
						<hr>
            <%--  --%>

            <%-- 양식 내용 --%>
            <div class="col-lg-7">
            <form id="approvalContentCommon">
              <%-- 1. 공통 양식 --%>
              <h1 class="text-center p-4">${detail.formTitle}</h1>

              <table class="table table-bordered">
                <tbody>

                <tr>
                  <th class="col-2 table-light">기안부서</th>
                  <td class="col-4">${detail.departmentName}</td>
                  <th class="col-2 table-light">기안자</th>
                  <td class="col-4">${detail.name}</td>
                </tr>

                <tr>
                  <th class="table-light">기안일</th>
                  <td>${fn:substring(detail.createdAt, 0, 10)}</td>
                  <th class="table-light">완료일</th>
                  <td>
                    <c:if test="${detail.status eq 'N' or detail.status eq 'Y'}">
                      ${fn:substring(detail.updatedAt, 0, 10)}
                    </c:if>
                  </td>
                </tr>

                </tbody>
              </table>
              <span>※ 기안일은 [결재 요청] 버튼을 클릭한 날짜로 확정됩니다.</span>
              <br><br>

              <%-- 결재자 수 계산 --%>
              <c:set var="approverCount" value="${fn:length(detail.approverDTOList)}" />
              <c:set var="approverRows" value="${approverCount eq 0 ? 1 : (approverCount + 3) / 4}" /> <%-- 4명씩 나눈 행 수 (올림) --%>

              <table class="table table-bordered">
                <tbody>
                <%-- 결재자 행을 동적으로 생성 --%>
                <c:forEach var="rowIndex" begin="0" end="${approverRows - 1}">
                  <%-- 첫 번째 행에만 "승인" 헤더를 rowspan으로 추가 --%>
                  <tr id="approverPosition${rowIndex}">
                    <c:if test="${rowIndex == 0}">
                      <th class="align-middle table-light col-2" rowspan="${approverRows * 3}">승인</th>
                    </c:if>

                      <%-- 현재 행의 결재자들 (최대 4명) --%>
                    <c:forEach var="colIndex" begin="0" end="3">
                      <c:set var="approverIndex" value="${rowIndex * 4 + colIndex}" />
                      <c:choose>
                        <c:when test="${approverIndex < approverCount}">
                          <%-- 결재자가 있는 경우 --%>
                          <td class="text-center" style="width: 20%;">
                              ${detail.approverDTOList[approverIndex].positionName}
                          </td>
                        </c:when>
                        <c:otherwise>
                          <%-- 빈 셀 --%>
                          <td class="text-center" style="width: 20%;">&nbsp;</td>
                        </c:otherwise>
                      </c:choose>
                    </c:forEach>
                  </tr>

                  <%-- 이름 행 --%>
                  <tr id="approverName${rowIndex}" style="height: 90px;">
                    <c:forEach var="colIndex" begin="0" end="3">
                      <c:set var="approverIndex" value="${rowIndex * 4 + colIndex}" />
                      <c:choose>
                        <c:when test="${approverIndex < approverCount}">
                          <%-- 결재자가 있는 경우 --%>
                          <td class="text-center align-middle" style="width: 20%;">
                              ${detail.approverDTOList[approverIndex].name}
                          </td>
                        </c:when>
                        <c:otherwise>
                          <%-- 빈 셀 --%>
                          <td class="text-center align-middle" style="width: 20%;">&nbsp;</td>
                        </c:otherwise>
                      </c:choose>
                    </c:forEach>
                  </tr>

                  <%-- 승인일자 행 --%>
                  <tr id="approvalDate${rowIndex}">
                    <c:forEach var="colIndex" begin="0" end="3">
                      <c:set var="approverIndex" value="${rowIndex * 4 + colIndex}" />
                      <c:choose>
                        <c:when test="${approverIndex < approverCount}">
                          <%-- 결재자가 있는 경우 --%>
                          <td class="text-center align-middle" style="width: 20%;">
                              <c:if test="${detail.approverDTOList[approverIndex].approveYn eq 'N'.charAt(0)}">
                                <p class="text-danger">반려: ${fn:substring(detail.approverDTOList[approverIndex].createdAt, 0, 10)}</p>
                              </c:if>
                              <c:if test="${detail.approverDTOList[approverIndex].approveYn eq 'Y'.charAt(0)}">
                                <p class="text-info">승인: ${fn:substring(detail.approverDTOList[approverIndex].createdAt, 0, 10)}</p>
                              </c:if>
                              &nbsp;
                          </td>
                        </c:when>
                        <c:otherwise>
                          <%-- 빈 셀 --%>
                          <td class="text-center align-middle" style="width: 20%;">&nbsp;</td>
                        </c:otherwise>
                      </c:choose>
                    </c:forEach>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
              <%--  --%>
            </form>

            <form id="approvalContentByType">
              <%-- 2. 결재 양식 종류에 따라 다른 HTML을 뿌림 --%>
              <c:choose>
                <c:when test="${detail.approvalFormId eq 1}">
                  <c:import url="/WEB-INF/views/approval/form_type/vacation.jsp"/>
                </c:when>
                <c:when test="${detail.approvalFormId eq 2}">
                  <c:import url="/WEB-INF/views/approval/form_type/business_trip.jsp"/>
                </c:when>
                <c:when test="${detail.approvalFormId eq 3}">
                  <c:import url="/WEB-INF/views/approval/form_type/draft.jsp"/>
                </c:when>
              </c:choose>
            </form>

            <c:if test="${detail.files ne null}">
              <div class="form-group">
                <label for="file" class="form-label mt-4 font-weight-bold">첨부파일 (${fn:length(detail.files)})</label>
                <div class="card">
                  <div class="list-group list-group-flush">
                    <c:forEach items="${detail.files}" var="el">
                      <%-- 확장자별 색상 설정 --%>
                      <c:choose>
                        <c:when test="${el.extension eq 'pdf'}"><c:set var="bgColor" value="bg-danger"/></c:when>
                        <c:when test="${el.extension eq 'doc' or el.extension eq 'docx'}"><c:set var="bgColor" value="bg-primary"/></c:when>
                        <c:when test="${el.extension eq 'xls' or el.extension eq 'xlsx'}"><c:set var="bgColor" value="bg-success"/></c:when>
                        <c:when test="${el.extension eq 'jpg' or el.extension eq 'jpeg' or el.extension eq 'png' or el.extension eq 'gif'}"><c:set var="bgColor" value="bg-info"/></c:when>
                        <c:when test="${el.extension eq 'zip' or el.extension eq 'rar' or el.extension eq '7z'}"><c:set var="bgColor" value="bg-warning"/></c:when>
                        <c:otherwise><c:set var="bgColor" value="bg-secondary"/></c:otherwise>
                      </c:choose>

                      <a href="/file/${approvalPath}/${el.fileId}" class="list-group-item list-group-item-action d-flex align-items-center">
                        <div class="d-flex align-items-center justify-content-center ${bgColor} text-white rounded mr-3" style="width: 40px; height: 40px; font-size: 12px; flex-shrink: 0;">
                          <strong>${fn:toUpperCase(el.extension)}</strong>
                        </div>
                        <div class="flex-grow-1 text-truncate">
                          <div class="font-weight-medium text-dark">${el.oriName}.${el.extension}</div>
                        </div>
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#6c757d" stroke-width="2" class="flex-shrink-0">
                          <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4M7 10l5 5 5-5M12 15V3"/>
                        </svg>
                      </a>
                    </c:forEach>
                  </div>
                </div>
              </div>
            </c:if>

            </div>

<%--            ${detail}--%>
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

