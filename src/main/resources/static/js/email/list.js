const table = document.getElementById('table');
const pathArr = location.pathname.split('/');

async function getMail() {
	try {
		const response = await fetch(`/api/email/${pathArr[2]}`, {
			method: 'POST'
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.json();

		console.log(data);

		data.forEach(function (e) {
			const tr = document.createElement('tr');
			console.log(e);

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
		console.log(error);
	}
}

getMail();