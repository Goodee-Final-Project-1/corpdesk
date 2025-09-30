const tbody = document.getElementById('tbody');
const pathArr = location.pathname.split('/');
if (pathArr.length <= 3) pathArr.push('1');

let page = parseInt(pathArr[3]);

async function getSalary() {

	try {

		const response = await fetch('/api/salary/list/' + page, {
			method: 'POST'
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.json();

		console.log(data);

		data.content.forEach(function (e) {
			const tr = document.createElement('tr');

			tr.setAttribute("onclick", `detail(${e.paymentId})`)
			tr.setAttribute("style", "cursor:pointer")
			// tr.classList.add('pe-auto');

			// <td onclick="detail(${e.paymentId})">조회</td>
			tr.innerHTML = `
				<td>${e.name}</td>
				<td>${e.paymentId}</td>
				<td>${e.departmentName}</td>
				<td>${e.positionName}</td>
				<td>${e.responsibility}</td>
				<td>${e.baseSalary.toLocaleString('ko-KR')}</td>
				<td>${e.allowanceAmount.toLocaleString('ko-KR')}</td>
				<td>${e.deductionAmount.toLocaleString('ko-KR')}</td>
				<td>${(e.baseSalary + e.allowanceAmount - e.deductionAmount).toLocaleString('ko-KR')}</td>
				<td>${e.paymentDate.substring(0, 10)}</td>
			`;

			tbody.appendChild(tr);
		});
	} catch(e) {
		console.log(e);
	}
}

getSalary();

function detail(e) {
	location.href = '/salary/detail/' + e;
}