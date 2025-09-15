<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.14/index.global.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.14/index.global.min.js"></script>

</head>
<body>

<h2>출퇴근 캘린더</h2>
<div id="calendar"></div>

<script>
	// 캘린더 초기화
	document.addEventListener('DOMContentLoaded', function() {
		const calendarEl = document.getElementById('calendar');
		const calendar = new FullCalendar.calendar(calendarEl, {
			initialView: 'dayGridMonth',
			locale: 'ko',
			 // RESTful URL로 이벤트 로딩(FullCalendar가 start/end를 자동으로 붙여서 요청)
			events: {
				url: '<c:url value="api/attendances"/>',
				method: 'GET',
				startParam: 'start',
				endParam: 'end',
				failure: function() {
					alert('이벤트 로딩 실패');
					}
			},
			// 필요시 추가 옵션
			headerToolbar: {
				left: 'prev,next today',
				center: 'title',
				right: 'dayGridMonth, timeGridWeek, timeGridDay'
			}
		});
		calendar.render();
	});

</script>

</body>
</html>