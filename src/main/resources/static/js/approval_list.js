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

let formId = "1";

approvalFormNames.forEach((name) => {
  name.addEventListener('click', function () {
    approvalTitle.textContent = name.textContent;

    formId = name.getAttribute('data-approval-form-id');
  });
});

const formCheck = document.querySelector('#formCheck');
const departmentIdEl = document.querySelector('#departmentId');

formCheck.addEventListener('click', function () {
    location.href=`/approval-form/${formId}?departmentId=${departmentIdEl.value}`; // "/approval-form/{formId}?departmentId={departmentId}"로 이동
});