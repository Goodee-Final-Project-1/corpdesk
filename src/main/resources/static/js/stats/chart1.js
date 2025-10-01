document.addEventListener('DOMContentLoaded', function () {

	const data1 = [];
	const cat = [];

	const searchForm = document.getElementById('searchForm');

	const options1 = {
		title: {
			text: '입퇴사자 및 재직자 통계',
			align: 'center',
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
			height: 350,
			type: 'line',
			stacked: false
		},
		legend: {
			horizontalAlign: 'left',
			offsetX: 40
		}
	};

	const chart = new ApexCharts(document.querySelector("#chart1"), options1);
	chart.render();

	async function getChart(start, end, department, position) {
		try {
			const response = await fetch('/api/stats/chart1', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({
					'start': start,
					'end': end,
					'department': department,
					'position': position
				})
			});
			if (!response.ok) throw new Error('수신 오류');
			const data = await response.json();

			console.log(data);

			chart.updateOptions({
				xaxis: {
					categories: data.months,
				}
			})

			chart.updateSeries([{
				name: '입사',
				type: 'column',
				data: data.joiner
			}, {
				name: '퇴사',
				type: 'column',
				data: data.resigner
			}, {
				name: '재직',
				type: 'line',
				data: data.resider
			}]);

		} catch (e) {
			console.error(e);
		}
	}

	getChart();


	searchForm.addEventListener('submit', function (e) {
		e.preventDefault();

		const start = document.getElementById('start').value;
		const end = document.getElementById('end').value;
		const department = document.getElementById('department').value;
		const position = document.getElementById('position').value;

		getChart(start, end, department, position);
	});

});