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

let formType = "1";

approvalFormNames.forEach((name) => {
  name.addEventListener('click', function () {
    approvalTitle.textContent = name.textContent;

    formType = name.getAttribute('data-approval-form-id');
  });
});

const formCheck = document.querySelector('#formCheck');
const departmentIdEl = document.querySelector('#departmentId');

formCheck.addEventListener('click', function () {
  location.href=`../${formType}/${departmentIdEl.value}`; // "/approval/{formType}/{departmentId}"로 이동
});