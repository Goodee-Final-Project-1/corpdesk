
const salary = document.getElementById('salary');
const allowance = document.getElementById('allowance');
const deduction = document.getElementById('deduction');

const pathArr = location.pathname.split('/');

async function getDetail() {
	const paymentId = parseInt(pathArr[pathArr.length - 1]);

	try {
		const response = await fetch(`/api/salary/detail/${paymentId}`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			}
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.json();

		console.log(data);

		const li = document.createElement('li');
		li.classList.add('row', 'mb-1');
		li.innerHTML += `<h6 class="col-sm-4 col-lg-4">기본급:</h6>`
		li.innerHTML += `<span class="col-sm-8 col-lg-8">${data.salaryPayment.baseSalary.toLocaleString('ko-KR')}</span>`;
		salary.appendChild(li);


		data.allowanceList.forEach(function (e) {
			const li = document.createElement('li');
			li.classList.add('row', 'mb-1');
			li.innerHTML += `<h6 class="col-sm-4 col-lg-4">${e.allowanceName}:</h6>`
			li.innerHTML += `<span class="col-sm-8 col-lg-8">${e.allowanceAmount.toLocaleString('ko-KR')}</span>`;
			allowance.appendChild(li);
		})

		data.deductionList.forEach(function (e) {
			const li = document.createElement('li');
			li.classList.add('row', 'mb-1');
			li.innerHTML += `<h6 class="col-sm-4 col-lg-4">${e.deductionName}:</h6>`
			li.innerHTML += `<span class="col-sm-8 col-lg-8">${e.deductionAmount.toLocaleString('ko-KR')}</span>`;
			deduction.appendChild(li);
		})



	} catch (e) {
		console.log(e);
	}
}

getDetail();