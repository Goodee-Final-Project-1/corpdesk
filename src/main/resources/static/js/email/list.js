const table = document.getElementById('table');
const pathArr = location.pathname.split('/');
if (pathArr.length <= 3) pathArr.push(1);

function paging() {
	const card = document.getElementById('card');
	const cardBody = document.createElement('div');

	cardBody.innerHTML =`
	<div class="card-body">
		<nav aria-label="Page navigation example">
			<ul class="pagination pagination-flat pagination-flat-rounded">
				<li class="page-item">
					<a class="page-link" href="#" aria-label="Previous">
						<span aria-hidden="true" class="mdi mdi-chevron-left"></span>
						<span class="sr-only">Previous</span>
					</a>
				</li>
				<li class="page-item">
					<a class="page-link" href="#">1</a>
				</li>
				<li class="page-item">
					<a class="page-link" href="#">2</a>
				</li>
				<li class="page-item">
					<a class="page-link" href="#">3</a>
				</li>
				<li class="page-item">
					<a class="page-link" href="#" aria-label="Next">
						<span aria-hidden="true" class="mdi mdi-chevron-right"></span>
						<span class="sr-only">Next</span>
					</a>
				</li>
			</ul>
		</nav>
	</div>
	`
	card.appendChild(cardBody);
}
paging();

// 메일 목록 요청
async function getMail() {
	try {
		const response = await fetch(`/api/email/${pathArr[2]}/${pathArr[3]}`, {
			method: 'POST'
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.json();

		data.forEach(function (e) {
			const tr = document.createElement('tr');

			tr.innerHTML = `
				<td class="sender-name text-dark">${e.from}</td>
				<td><a href="/email/${pathArr[2]}/detail/${e.emailNo}" class="text-default d-inline-block text-smoke">
					<span class="subject text-dark">${e.subject}</span>
				</a></td>
				<td class="date">${e.sentDate}</td>
			`;
			// table.appendChild(tr);
			table.prepend(tr);
		});

	} catch (e) {
		console.log(e);
	}
}

getMail();