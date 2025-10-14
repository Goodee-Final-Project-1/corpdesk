const params = new URLSearchParams(window.location.search);

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
  else location.href=`/approval-form/${formId}?departmentId=${departmentIdEl.value}`;
});

/**
 * 수정/삭제 혹은 승인/반려 버튼을 눌렀을 때
 */
const btnActions = document.querySelectorAll('.btn-action');
const approvalId = btnActions[0].parentElement.getAttribute('data-approval-id');
const approverId = document.querySelector('#btnBox').getAttribute('data-approver-id');
console.log(approverId);

btnActions.forEach((btn) => {
  btn.addEventListener('click', function () {

    switch (btn.id) {
      case 'btnDelete':
        const message = '정말 삭제하시겠습니까?';

        if(confirm(message)) {
          fetch(`/approval/${approvalId}`, {
            method: "DELETE"
          })
              .then(r => r.text())
              .then(r => {
                  alert('삭제되었습니다.');
                  history.back();
              });
        }

        break;
      case 'btnApproval':
        fetch(`/approval/${approvalId}`, {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ // JavaScript 객체를 JSON 문자열로 변환하여 본문에 담음
            approveYn: 'Y',
            approverId: `${approverId}`
          })
        })
            .then(r => r.text())
            .then(r => {
                console.log(r)

                alert("승인하였습니다.");
                location.href = `/approval/${approvalId}`;
            })
        ;

        break;
      case 'btnReject':
        fetch(`/approval/${approvalId}`, {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ // JavaScript 객체를 JSON 문자열로 변환하여 본문에 담음
            approveYn: 'N',
            approverId: `${approverId}`
          })
        })
            .then(r => r.text())
            .then(r => {
                console.log(r)

                alert("반려하였습니다.");
                location.href = `/approval/${approvalId}`;
            })
        ;

        break;
      default:
    }

  });
});

/**
 * json 형태의 결재내용 데이터를 폼에 뿌리기
 */

const form = document.querySelector('#approvalContentByType');


