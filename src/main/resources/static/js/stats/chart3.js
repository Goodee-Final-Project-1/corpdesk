document.addEventListener('DOMContentLoaded', function () {

	const options3 = {
		title: {
			text: '나이 통계',
			align: 'center',
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
		}
	};

	const chart3 = new ApexCharts(document.querySelector("#chart3"), options3);
	chart3.render();

	async function getChart3() {
		try {
			const response = await fetch('/api/stats/chart3', {
				method: 'POST',
			});
			if (!response.ok) throw new Error('수신 오류');
			const data = await response.json();

			console.log(data);

			chart3.updateSeries(data.count);
			chart3.updateOptions({
				labels: data.diff,
			})

		} catch (e) {
			console.error(e);
		}
	}

	getChart3();

});