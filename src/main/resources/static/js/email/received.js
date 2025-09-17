const table = document.getElementById('table');

async function getMail() {
	try {
		const response = await fetch("/emailApi/received", {
			method: 'POST'
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.json();

		console.log(data);

		data.forEach(function (e) {
			const tr = document.createElement('tr');
			// console.log(e);

			tr.innerHTML = `
				<td>${e.from}</td>
				<td>${e.subject}</td>
				<td>${e.sentDate}</td>
			`;
			table.appendChild(tr);
		});

	} catch (error) {
		console.log(error);
	}
}

getMail();