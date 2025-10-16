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
 * 인쇄 버튼 클릭 이벤트 (html2canvas -> addImage 방식으로 수정)
 */
document.getElementById('pdf-btn').addEventListener('click', async function () {
  const elementToCapture = document.getElementById('pdf-area');
  const { jsPDF } = window.jspdf;

  // 1. 링크 정보 미리 추출 (html2canvas 캡처 전)
  const links = [];
  const linkElements = elementToCapture.querySelectorAll('a');

  // PDF 출력 폭과 HTML 가상 폭 정의
  const pdfWidth = 190; // PDF 용지 내 폭 (mm)
  const windowWidth = 800; // HTML 캡처 시 가상 폭 (px)
  const margin = 10; // 좌우 상하 여백 (mm)
  const ratio = pdfWidth / windowWidth;

  linkElements.forEach(link => {
    const rect = link.getBoundingClientRect();
    const parentRect = elementToCapture.getBoundingClientRect();
    links.push({
      x: rect.left - parentRect.left,
      y: rect.top - parentRect.top,
      width: rect.width,
      height: rect.height,
      url: link.href
    });
  });

  // 2. html2canvas로 HTML 영역을 캔버스(이미지)로 캡처
  const canvas = await html2canvas(elementToCapture, {
    scale: 2, // 고해상도 캡처를 위해 스케일을 2로 높입니다. (한글 깨짐 방지)
    width: windowWidth, // 캡처할 영역의 가상 폭을 800px로 설정
    useCORS: true,
    allowTaint: true,
    logging: false,
    letterRendering: true,
  });

  // 3. 캡처된 캔버스를 이미지 데이터로 변환 (JPEG 또는 PNG)
  const imgData = canvas.toDataURL('image/jpeg', 1.0); // JPEG 포맷으로 변환 (품질 1.0)

  // 4. jsPDF 객체 생성
  const pdf = new jsPDF({
    orientation: 'portrait',
    unit: 'mm',
    format: 'a4',
    compress: true
  });

  // ================== 문서 사이즈 계산 및 페이지 분할 후 jsPDF 객체에 추가
  // A4 사이즈 (210mm x 297mm)
  const pageHeight = 297; // A4 세로 길이
  const contentHeight = pageHeight - (margin * 2); // 여백을 제외한 실제 컨텐츠 높이
  const pdfHeight = (canvas.height * pdfWidth) / canvas.width;

  // 5. PDF에 캡처된 이미지를 페이지별로 분할하여 추가
  let position = 0;
  let pageNumber = 0;

  while (position < pdfHeight) {
    if (pageNumber > 0) {
      pdf.addPage();
    }

    // 현재 페이지에 들어갈 이미지 영역 계산
    const remainingHeight = pdfHeight - position;
    const currentPageHeight = Math.min(contentHeight, remainingHeight);

    // canvas에서 현재 페이지에 해당하는 부분만 잘라내기
    const sourceY = (position / pdfHeight) * canvas.height;
    const sourceHeight = (currentPageHeight / pdfHeight) * canvas.height;

    // 임시 캔버스 생성하여 해당 영역만 복사
    const pageCanvas = document.createElement('canvas');
    pageCanvas.width = canvas.width;
    pageCanvas.height = sourceHeight;
    const pageContext = pageCanvas.getContext('2d');

    pageContext.drawImage(
        canvas,
        0, sourceY, canvas.width, sourceHeight,
        0, 0, canvas.width, sourceHeight
    );

    const pageImgData = pageCanvas.toDataURL('image/jpeg', 1.0);

    // PDF에 이미지 추가
    pdf.addImage(pageImgData, 'JPEG', margin, margin, pdfWidth, currentPageHeight);

    // 6. 현재 페이지의 링크 오버레이 추가
    links.forEach(link => {
      const linkY = (link.y * ratio) + margin;

      // 링크가 현재 페이지 범위에 있는지 확인
      if (linkY >= position + margin && linkY < position + margin + contentHeight) {
        const pdfX = (link.x * ratio) + margin;
        const adjustedY = linkY - position; // 페이지 내 상대 위치로 조정
        const linkPdfWidth = link.width * ratio;
        const linkPdfHeight = link.height * ratio;

        pdf.link(pdfX, adjustedY, linkPdfWidth, linkPdfHeight, { url: link.url });
      }
    });

    position += contentHeight;
    pageNumber++;
  }
  // ==================


  // 6. PDF 생성 후 링크 오버레이 추가
  links.forEach(link => {
    // HTML 좌표를 PDF 좌표(mm)로 변환하고 여백을 더합니다.
    const pdfX = (link.x * ratio) + margin;
    const pdfY = (link.y * ratio) + margin;
    const linkPdfWidth = link.width * ratio;
    const linkPdfHeight = link.height * ratio;

    // PDF에 링크 영역을 추가합니다.
    pdf.link(pdfX, pdfY, linkPdfWidth, linkPdfHeight, { url: link.url });
  });

  // 7. 미리보기 기능
  const blob = pdf.output('blob');
  const blobUrl = URL.createObjectURL(blob);
  window.open(blobUrl, 'pdfPreview', 'width=800,height=700,resizable=yes,scrollbars=yes');

});