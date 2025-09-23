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

			tr.innerHTML = `
			<tr>
				<th>${e.paymentId}</th>
				<th>${e.name}</th>
				<th>${e.departmentName}</th>
				<th>${e.positionName}</th>
				<th>${e.responsibility}</th>
				<th>${e.baseSalary.toLocaleString('ko-KR')}</th>
				<th>${e.allowanceAmount.toLocaleString('ko-KR')}</th>
				<th>${e.deductionAmount.toLocaleString('ko-KR')}</th>
				<th>${(e.baseSalary + e.allowanceAmount - e.deductionAmount).toLocaleString('ko-KR')}</th>
				<th>${e.paymentDate.substring(0, 10)}</th>
			</tr>
			`;

			tbody.appendChild(tr);
		});
	} catch(e) {
		console.log(e);
	}
}

getSalary();