/**
 * 
 */

const approvalRows = document.querySelectorAll('.approval-row');

approvalRows.forEach((row) => {
  row.addEventListener('click', function() {
    const approvalId = row.getAttribute('data-approval-id');
    
    location.href=`../${approvalId}`; // "/approval/{approvalId}"로 이동
  });
});

/**
 * 
 */

const approvalFormNames = document.querySelectorAll('.approval-form-name');
const approvalTitle = document.querySelector('#approvalTitle');

let formId = 0;

approvalFormNames.forEach((name) => {
  name.addEventListener('click', function () {
    approvalTitle.textContent = name.textContent;

    formId = name.getAttribute('data-approval-form-id');
  });
});

const formCheckBtn = document.querySelector('#formCheck');
const departmentIdEl = document.querySelector('#departmentId');

formCheckBtn.addEventListener('click', function () {
    if(formId === 0) alert('결재 양식을 선택해 주세요.')
    else location.href=`/approval-form/${formId}?departmentId=${departmentIdEl.value}&username=jung_frontend`; // TODO username 정보는 사용자의 인증 정보를 사용하도록 수정
});