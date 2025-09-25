
const subject = document.getElementById('subject');
const from = document.getElementById('from');
const recipients = document.getElementById('recipients');
const sentDate = document.getElementById('sentDate');
const content = document.getElementById('content');
const pathArr = location.pathname.split('/');
const emailNo = parseInt(pathArr[pathArr.length - 1]);

// console.log(emailNo);

async function getDetail() {
	try {
		const response = await fetch(`/api/email/${pathArr[2]}/detail`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				'emailNo': emailNo
			})
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.json();

		// console.log(data);

		subject.append(data.subject);
		from.append(data.from);
		recipients.append(data.recipients);
		const date = new Date(data.sentDate);
		sentDate.append(date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate());
		// sentDate.setAttribute('datetime', data.sentDate);
		// content.append(data.text);
		content.innerHTML = data.text;

	} catch (e) {
		console.log(e);
	}
}

getDetail();