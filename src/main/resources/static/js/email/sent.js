const table = document.getElementById('table');

async function getMail() {
	try {
		const response = await fetch("/api/email/sent", {
			method: 'POST'
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.json();

		console.log(data);

		data.forEach(function (e) {
			const tr = document.createElement('tr');
			console.log(e);

			tr.innerHTML = `
				<td>${e.from}</td>
				<td><a href="/email/sent/${e.no}">${e.subject}</a></td>
				<td>${e.sentDate}</td>
			`;
			table.appendChild(tr);
		});

	} catch (error) {
		console.log(error);
	}
}

getMail();