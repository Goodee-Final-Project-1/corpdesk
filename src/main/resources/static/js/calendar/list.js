const checkAttendance = document.getElementById('customCheckPrimary');
const checkVacation = document.getElementById('customCheckSecondary');
const checkSchedule = document.getElementById('customCheckSuccess');
const checkEveryVacation = document.getElementById('customCheckWarning');

let currentDateInfo = null;

document.addEventListener('DOMContentLoaded', function () {
	const calendarEl = document.getElementById('calendar');
	const calendar = new FullCalendar.Calendar(calendarEl, {
		initialView: 'dayGridMonth',
		headerToolbar: {
			left: 'dayGridMonth dayGridWeek listDay',
			center: 'title',
			right: 'today prev,next',
		},
		views: {
			// dayGridMonth: {
			// 	year: 'numeric',
			// 	month: 'long',
			// },
			// listWeek: {
			// 	noEventsText: '이번 주 일정이 없습니다.',
			// 	buttonText: 'week'
			// },
			listDay: {
				noEventsContent: '오늘 일정이 없습니다.',
				buttonText: 'day'
			}
		},
		datesSet: function (info) {
			calendar.removeAllEvents();
			currentDateInfo = info;
			if (checkAttendance.checked == true) getAttendance(info);
		}
	});
	calendar.render();

	checkAttendance.addEventListener('change', function () {
		if (checkAttendance.checked == true) getAttendance(currentDateInfo);
		else {
			calendar.getEvents().forEach(event => {
				if (event.groupId == 'attendance') {
					event.remove();
				}
			})
		}
	})

	async function getAttendance(info) {
		try {
			console.log(info);
			const response = await fetch('/api/calendar/attendance', {
				method: 'POST',
				headers: {
					'content-type': 'application/json',
				},
				body: JSON.stringify({
					startDateTime: info.start,
					endDateTime: info.end
				})
			});

			if (!response.ok) throw new Error('수신 오류');
			const data = await response.json();

			data.forEach(a => {
				calendar.addEvent({
					id: a.attendanceId,
					title: a.workStatus,
					start: a.checkInDateTime,
					end: a.checkOutDateTime,
					groupId: 'attendance',
				});
			});
		} catch (error) {
			console.log(error)
		}
	}


});
