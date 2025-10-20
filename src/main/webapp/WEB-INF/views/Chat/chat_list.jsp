<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>채팅 목록</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
<%--  <link rel="stylesheet" href="/css/chat/chat_list.css">--%>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<sec:authentication property="principal.username" var="user" />
			<input type="hidden" class="username" value="${user }">
	
<div class = "row">
  <!-- 연락처 목록 -->
  <div class="col-lg-4 col-xl-3">
    <div class="card card-default">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h2 class="mb-0">연락처 목록</h2>
        <span class="mdi mdi-account-multiple-plus" data-toggle="modal" data-target="#createRoomStep1" style="cursor: pointer; font-size: 24px;"></span>
      </div>
      <div class="card-body p-0">
        <!-- 검색창 -->
        <div class="p-4 pb-0">
          <div class="input-group mb-3">
            <input type="text" class="form-control" id="searchContactList" placeholder="이름 검색">
          </div>
        </div>
        <!-- 연락처 리스트 -->
        <ul class="list-group list-group-flush" id="contactList" data-simplebar style="max-height: 562px;">
          <c:forEach items="${contactList}" var="employee">
            <li class="list-group-item list-group-item-action d-flex align-items-center employeeListOne" data-employee="${employee.username}">
              <img src="${employee.imgPath}" class="rounded-circle mr-3" alt="Profile" style="width: 50px; height: 50px;">
              <div class="flex-fill">
                <strong class="employeeName d-block">${employee.name}</strong>
                <small class="text-muted">${employee.departmentName} ${employee.positionName}</small>
              </div>
            </li>
          </c:forEach>
        </ul>
      </div>
    </div>
  </div>

  <!-- 채팅방 목록 -->
  <div class="col-lg-8 col-xl-9">
    <div class="card card-default">
      <div class="card-header">
        <h2 class="mb-0">채팅방 목록</h2>
      </div>
      <div class="card-body p-0">
        <ul class="list-group list-group-flush chatList" data-simplebar style="max-height: 677px;">
          <c:forEach items="${roomList}" var="room">
            <li class="list-group-item list-group-item-action chatListOne"
                data-roomId="${room.chatRoomId}"
                data-unreadCount="${room.unreadCount}">
              <div class="d-flex align-items-center">
                <div class="mr-3">
                  <img class="rounded-circle" src="${room.imgPath}" alt="Room Image" style="width: 60px; height: 60px;">
                </div>
                <div class="flex-fill">
                  <div class="d-flex justify-content-between align-items-center mb-1">
                    <strong class="username text-dark">${room.chatRoomTitle}</strong>
                    <div class="d-flex align-items-center">
                      <c:choose>
                        <c:when test="${room.unreadCount ne 0}">
                          <span class="badge badge-primary unreadCount mr-2">${room.unreadCount}</span>
                        </c:when>
                        <c:otherwise>
                          <span class="unreadCount"></span>
                        </c:otherwise>
                      </c:choose>
                      <small class="text-muted last-msg-time" data-lastMessageTime="${room.lastMessageTime}"></small>
                    </div>
                  </div>
                  <p class="last-msg text-muted mb-0 text-truncate">${room.chatRoomLastMessage}</p>
                </div>
                <div class="dropdown ml-2">
                  <a class="dropdown-toggle icon-burger-mini" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></a>
                  <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuLink">
                    <a class="dropdown-item room-out" href="javascript:void(0)">채팅방 나가기</a>
                  </div>
                </div>
              </div>
            </li>
          </c:forEach>
        </ul>
      </div>
    </div>
  </div>
</div>

<%--<c:forEach items ="${notificationList}" var="notification">--%>
<%--	<p>${notification.imgPath}</p>--%>
<%--	<p>${notification.viewName}</p>--%>
<%--	<p>${notification.messageContent}</p>--%>
<%--	<p>${notification.sentAt}</p>--%>
<%--	<p>${notification.chatRoomId}</p>--%>
<%--</c:forEach>--%>


<!-- Step 1: 참여자 선택 모달 -->
<div class="modal fade" id="createRoomStep1" tabindex="-1" role="dialog" aria-labelledby="step1Label" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="step1Label">대화상대 선택</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="닫기">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <!-- 검색창 -->
        <div class="input-group mb-3">
          <input type="text" class="form-control" id="searchUserInput" placeholder="이름 검색">
        </div>
        <!-- 사원 목록 (스크롤 가능) -->
        <ul class="list-group" id="participantList" style="max-height: 300px; overflow-y: auto;">
          <c:forEach items="${contactList}" var="employee">
            <c:if test="${employee.username ne user }">
              <li class="list-group-item d-flex align-items-cent er">
                <img src="${employee.imgPath}" class="rounded-circle mr-3" alt="Profile" style="width:40px; height:40px;">
                <div class="flex-fill">
                  <strong class="employeeName d-block">${employee.name}</strong><br>
                  <small class="text-muted">${employee.departmentName} ${employee.positionName}</small>
                </div>
                <input type="checkbox" value="${employee.username}" class="participant-checkbox">
              </li>
            </c:if>
          </c:forEach>
        </ul>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        <button type="button" class="btn btn-primary" id="nextStepBtn">다음</button>
      </div>
    </div>
  </div>
</div>

<!-- Step 2: 방 제목 입력 모달 -->
<div class="modal fade" id="createRoomStep2" tabindex="-1" role="dialog" aria-labelledby="step2Label" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="step2Label">채팅방 제목 설정</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="닫기">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <label for="roomTitle">채팅방 제목</label>
        <input type="text" id="roomTitle" class="form-control" placeholder="채팅방 이름 입력">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-light" data-dismiss="modal">취소</button>
        <button type="button" class="btn btn-info" id="createRoomConfirmBtn">생성</button>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
<script src="/js/chat/chatList.js"></script>

<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end2.jsp"/>
</html>