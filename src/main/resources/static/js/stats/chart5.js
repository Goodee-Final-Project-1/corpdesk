document.addEventListener('DOMContentLoaded', function () {

	const searchForm = document.getElementById('searchForm');

	const options5 = {
		title: {
			text: '근무시간 통계',
			align: 'left',
			style: {
				fontSize: '22px',
			}
		},
		series: [{
			name: 'default',
			type: 'column',
			data: []
		}],
		chart: {
			type: 'bar'
		},
		legend: {
			position: 'top',
			markers: {
				width: 24,
				height: 12,
				radius: 0
			}
		},
		theme: {
			palette: 'palette3'
		}
	};

	const chart5 = new ApexCharts(document.querySelector("#chart5"), options5);
	chart5.render();

	async function getChart5(start, end) {
		try {
			const response = await fetch('/api/stats/chart5', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({
					'start': start,
					'end': end
				})
			});
			if (!response.ok) throw new Error('수신 오류');
			const data = await response.json();

			console.log(data);

			chart5.updateOptions({
				xaxis: {
					categories: data.months,
				}
			})

			chart5.updateSeries([{
				name: '정규근무',
				type: 'column',
				data: data.fixed
			}, {
				name: '연장근무',
				type: 'column',
				data: data.overtime
			}]);

		} catch (e) {
			console.error(e);
		}
	}

	getChart5();


	searchForm.addEventListener('submit', function (e) {
		e.preventDefault();

		const start = document.getElementById('start').value;
		const end = document.getElementById('end').value;

		getChart5(start, end);
	});
});