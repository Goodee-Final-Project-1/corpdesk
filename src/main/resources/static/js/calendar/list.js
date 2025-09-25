document.addEventListener('DOMContentLoaded', function () {
	const calendarEl = document.getElementById('calendar');
	const calendar = new FullCalendar.Calendar(calendarEl, {
		initialView: 'dayGridMonth',
		headerToolbar: {
			left: 'dayGridMonth listWeek timeGridDay',
			center: 'title',
			right: 'today prev,next',
		},
		// views: {
		// 	dayGridMonth: {
		// 		year: 'numeric',
		// 		month: 'long',
		// 	},
		// 	listWeek: {
		// 		noEventsText: '이번 주 일정이 없습니다.',
		// 	},
		// 	timeGridDay: {
		// 		titleFormat: {
		// 			year: 'numeric',
		// 			month: 'long',
		// 			day: 'numeric',
		// 		},
		// 		nowIndicator: true,
		// 	},
		// },
		datesSet: function (info) {
			getAttendance(info);
		}
	});
	calendar.render();

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
				});
			});
		} catch (error) {
			console.log(error)
		}
	}
});
