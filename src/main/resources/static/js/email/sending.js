const form = document.getElementById('form_data');
const textArea = document.getElementById('text');
const editor = document.getElementById('editor');
const submitBtn = document.getElementById('submit_btn');

submitBtn.onclick = async function () {
	try {
		// console.log(editor.firstElementChild);
		const content = editor.firstElementChild;
		// console.log(content.innerHTML);
		textArea.innerText = content.innerHTML;

		const formData = new FormData(form);

		const response = await fetch('/api/email/sending', {
			method: 'POST',
			body: formData
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.text();

		if (data == 'success') {
			location.href = '/email/sent';
		}

		console.log(data);
	} catch (e) {
		console.log(e)
	}
};