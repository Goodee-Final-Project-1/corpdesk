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
				<th>ID</th>
				<th>사원명</th>
				<th>부서</th>
				<th>직위</th>
				<th>직책</th>
				<th>기본급</th>
				<th>수당</th>
				<th>공제</th>
				<th>실지급액</th>
				<th>지급일</th>
			</tr>
			`;

			tbody.appendChild(tr);
		});
	} catch(e) {
		console.log(e);
	}
}

getSalary();