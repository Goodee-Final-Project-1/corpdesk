const params = new URLSearchParams(window.location.search);
const username = params.get("username");

/**
 * 수정/삭제 혹은 승인/반려 버튼을 눌렀을 때 혹은 취소 버튼을 눌렀을 때
 */
const btnActions = document.querySelectorAll('.btn-action');
const btnCancel = document.querySelector('#btnCancel');
const approvalId = btnActions[0].parentElement.getAttribute('data-approval-id');
const approverId = document.querySelector('#btnBox').getAttribute('data-approver-id');
console.log(approverId);

btnActions.forEach((btn) => {
  btn.addEventListener('click', function () {

    switch (btn.id) {
      case 'btnEdit':
        break;
      case 'btnDelete':
        fetch(`/approval/${approvalId}`, {
          method: "DELETE"
        })
            .then(r => r.text())
            .then(r => {
                console.log(r)

                alert('삭제되었습니다.');
                location.href = `/approval/list?username=${username}`;
            })
        ;

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
                location.href = `/approval/${approvalId}?username=${username}`;
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
                location.href = `/approval/${approvalId}?username=${username}`;
            })
        ;

        break;
      default:
    }

  });
});

btnCancel.addEventListener('click', function () {
    location.href=`/approval/list?username=${username}`;
});

/**
 * json 형태의 결재내용 데이터를 폼에 뿌리기
 */

const form = document.querySelector('#approvalContentByType');


