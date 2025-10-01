document.addEventListener('DOMContentLoaded', function () {

	const data1 = [];
	const cat = [];

	const searchForm = document.getElementById('searchForm');

	const options2 = {
		series: [0],
		chart: {
			type: 'donut',
		},
		responsive: [{
			breakpoint: 480,
			options: {
				chart: {
					width: 200
				},
				legend: {
					position: 'bottom'
				}
			}
		}]
	};

	const chart2 = new ApexCharts(document.querySelector("#chart2"), options2);
	chart2.render();

	async function getChart2(start, end, department, position) {
		try {
			const response = await fetch('/api/stats/chart2', {
				method: 'POST',
			});
			if (!response.ok) throw new Error('수신 오류');
			const data = await response.json();

			console.log(data);

			chart2.updateSeries(data);
			chart2.updateOptions({
				labels: [],
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
		const department = document.getElementById('department').value;
		const position = document.getElementById('position').value;

		getChart2(start, end, department, position);
	});

});