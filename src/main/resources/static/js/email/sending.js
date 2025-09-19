
const formData = document.getElementById('form_data');
const textArea = document.getElementById('text');
const editor = document.getElementById('editor');
const submitBtn = document.getElementById('submit_btn');

submitBtn.addEventListener('click', function () {
	// console.log(editor.firstElementChild);
	const content = editor.firstElementChild;
	console.log(content.innerHTML);
	textArea.innerText = content.innerHTML;
	formData.submit();
});