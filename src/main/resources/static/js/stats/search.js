import {getChart} from './chart1.js';
import {getChart2} from 'chart2.js';
import {getChart3} from 'chart3.js';
import {getChart4} from 'chart4.js';
import {getChart5} from 'chart5.js';

document.addEventListener('DOMContentLoaded', function () {
	const searchForm = document.getElementById('searchForm');


	searchForm.addEventListener('submit', function (e) {
		e.preventDefault();

		const start = document.getElementById('start').value;
		const end = document.getElementById('end').value;
		const department = document.getElementById('department').value;
		const position = document.getElementById('position').value;

		getChart(start, end, department, position);
		getChart2(start, end, department, position);
		getChart3(start, end, department, position);
		getChart4(start, end, department, position);
		getChart5(start, end, department, position);
	});
});