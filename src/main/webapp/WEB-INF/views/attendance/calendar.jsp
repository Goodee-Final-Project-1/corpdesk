<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- FullCalendar 스타일(CDN) -->
<link
	href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.css"
	rel="stylesheet">

</head>
<body>

	<!-- 달력을 렌더링할 컨테이너 -->
	<div id="calendar"></div>
	<!-- 로딩 표시 -->
	<div id="loading">불러오는 중...</div>

	<!-- FullCalendar 스크립트(CDN, v6 글로벌 빌드) -->
	<script
		src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js"></script>
	<!-- 한국어 로케일(옵션) -->
	<script
		src="https://cdn.jsdelivr.net/npm/@fullcalendar/core@6.1.15/locales/ko.global.min.js"></script>

	<script>
		// DOM 로드 후 달력 초기화
		document
				.addEventListener(
						'DOMContentLoaded',
						function() {
							// 달력 컨테이너 가져오기
							const el = document.getElementById('calendar');

							// FullCalendar 인스턴스 생성
							const calendar = new FullCalendar.Calendar(
									el,
									{
										// 초기 뷰 설정(월간)
										initialView : 'dayGridMonth',
										// 한국어 로케일 적용
										locale : 'ko',
										// 클라이언트 시간대 사용(서버는 ISO 문자열을 그대로 반환)
										timeZone : 'local',
										// 헤더 툴바 구성
										headerToolbar : {
											left : 'prev,next today',
											center : 'title',
											right : 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
										},
										// 이벤트 소스 설정(우리 컨트롤러 엔드포인트)
										events : {
											// AttendanceEventsController의 GET /attendance/events
											url : '/attendance/events',
											// FullCalendar가 start/end 또는 startStr/endStr를 자동으로 붙여 보냄
											method : 'GET',
											// 추가 파라미터가 필요하면 여기서 덧붙일 수 있음(예: employeeId)
											// extraParams: { employeeId: 1001 },
											// 서버 오류 처리 콜백
											failure : function() {
												alert('이벤트를 불러오지 못했습니다.');
											}
										},
										// 로딩 상태 표시(네트워크 왕복 동안 "불러오는 중..." 보이기)
										loading : function(isLoading) {
											document.getElementById('loading').style.display = isLoading ? 'block'
													: 'none';
										},
										// 이벤트 클릭 시 동작(예: 상세 팝업 또는 이동)
										eventClick : function(info) {
											// 서버에서 내려준 이벤트 데이터 확인
											const ev = info.event;
											// 간단 확인용 알럿(필요 시 모달로 교체)
											alert('ID: '
													+ ev.id
													+ '\n제목: '
													+ ev.title
													+ '\n시작: '
													+ ev.startStr
													+ (ev.end ? '\n끝: '
															+ ev.endStr : ''));
										},
										// 셀 클릭 시 동작(예: 출근 기록 추가 폼 열기)
										dateClick : function(info) {
											// info.dateStr 은 ISO 날짜 문자열
											console.log('dateClick:',
													info.dateStr);
										}
									});

							// 달력 렌더링
							calendar.render();
						});
	</script>

</body>
</html>