/**
 *
 */
const btnActions = document.querySelectorAll('.btn-action');
const approvalId = btnActions[0].parentElement.getAttribute('data-approval-id');

btnActions.forEach((btn) => {
  btn.addEventListener('click', function () {
    if(btn.id === 'btnEdit') {

    } else if(btn.id === 'btnDelete') {
      fetch(`/approval/${approvalId}`, {
        method: "DELETE"
      })
          .then(r => r.text())
          .then(r => console.log(r))
      ;
    }
  });
});

