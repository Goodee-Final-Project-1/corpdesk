document.addEventListener('DOMContentLoaded', function () {

	const searchForm = document.getElementById('searchForm');

	const options2 = {
		title: {
			text: '근속기간 통계',
			align: 'left',
			style: {
				fontSize: '22px',
			}
		},
		series: [0],
		chart: {
			type: 'donut',
		},
		legend: {
			position: 'bottom',
			markers: {
				width: 24,
				height: 12,
				radius: 0
			}
		}
	};

	const chart2 = new ApexCharts(document.querySelector("#chart2"), options2);
	chart2.render();

	async function getChart2(start, end) {
		try {
			const response = await fetch('/api/stats/chart2', {
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

			// console.log(data);

			chart2.updateSeries(data.count);
			chart2.updateOptions({
				labels: data.diff,
			})

		} catch (e) {
			console.error(e);
		}
	}

	getChart2();


	searchForm.addEventListener('submit', function (e) {
		e.preventDefault();

		const start = document.getElementById('start').value;
		const end = document.getElementById('end').value;

		getChart2(start, end);
	});
});