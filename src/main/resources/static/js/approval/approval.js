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

/**
 *
 */
const url = new URL(window.location.href);
const path = url.pathname;
const pathSegments = path.split('/');
const approvalFormId = pathSegments[2];

switch (approvalFormId) {
  case '1':
    /**
     * 휴가 신청 폼 - 휴가 사용 일수 자동 계산
     */
    // 날짜 입력 필드 참조
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const usedDaysInput = document.getElementById('usedDays');

    // 평일(주말 제외) 계산 함수
    function calculateWeekdays(startDate, endDate) {
      if (!startDate || !endDate || startDate > endDate) {
        return 0;
      }

      let count = 0;
      let currentDate = new Date(startDate);

      while (currentDate <= endDate) {
        const dayOfWeek = currentDate.getDay();
        // 0: 일요일, 6: 토요일 제외
        if (dayOfWeek !== 0 && dayOfWeek !== 6) {
          count++;
        }
        currentDate.setDate(currentDate.getDate() + 1);
      }

      return count;
    }

    // 사용일수 업데이트 함수
    function updateUsedDays() {
      const startDate = startDateInput.value ? new Date(startDateInput.value) : null;
      const endDate = endDateInput.value ? new Date(endDateInput.value) : null;

      if (startDate && endDate) {
        if (startDate > endDate) {
          usedDaysInput.value = '종료일이 시작일보다 빨라요';
          usedDaysInput.style.color = '#dc3545';
        } else {
          const weekdays = calculateWeekdays(startDate, endDate);
          usedDaysInput.value = `${weekdays}`;
          usedDaysInput.style.color = '#198754';
        }
      } else {
        usedDaysInput.value = '';
        usedDaysInput.style.color = '#6c757d';
      }
    }

    // 이벤트 리스너 추가
    startDateInput.addEventListener('change', updateUsedDays);
    endDateInput.addEventListener('change', updateUsedDays);

    // 시작일 변경 시 종료일 최소값 설정
    startDateInput.addEventListener('change', function() {
      if (this.value) {
        endDateInput.min = this.value;
        // 종료일이 시작일보다 빠른 경우 초기화
        if (endDateInput.value && endDateInput.value < this.value) {
          endDateInput.value = '';
        }
      }
    });

    break;

  case '2':
    /**
     * 출장 신청 폼 - 금액 합계 자동 계산
     */
        // 금액 입력 필드들을 모두 선택
    const amountInputs = document.querySelectorAll('.amount-input');
    const totalDisplay = document.getElementById('total-amount');

    // 총합을 계산하는 함수
    function calculateTotal() {
      let total = 0;
      amountInputs.forEach(input => {
        const value = parseFloat(input.value) || 0;
        total += value;
      });
      totalDisplay.textContent = total.toLocaleString('ko-KR');
    }

    // 각 금액 입력 필드에 이벤트 리스너 추가
    amountInputs.forEach(input => {
      input.addEventListener('input', calculateTotal);
      input.addEventListener('blur', calculateTotal);
    });

    // 페이지 로드 시 초기 계산
    calculateTotal();

    // -------------------------------------------------------------

    /**
     * 출장 신청 폼 - 출장 기간 유효성 검사
     */
        // 날짜 입력 필드 참조 (ID는 HTML에서 재사용됨)
    const businessStartDateInput = document.getElementById('startDate');
    const businessEndDateInput = document.getElementById('endDate');

    // 날짜 유효성 검사 및 min 속성 설정 함수
    function validateBusinessTripDates() {
      const startDateValue = businessStartDateInput.value;
      const endDateValue = businessEndDateInput.value;

      if (startDateValue && businessEndDateInput) {
        // 1. 시작일 변경 시 종료일 최소값 설정
        businessEndDateInput.min = startDateValue;

        // 2. 종료일이 시작일보다 빠른 경우 초기화 및 알림
        if (endDateValue && endDateValue < startDateValue) {
          alert('출장 종료일은 시작일보다 빠를 수 없습니다.');
          businessEndDateInput.value = ''; // 값 초기화
        }
      }
    }

    // 이벤트 리스너 추가: 시작일 및 종료일 변경 시 유효성 검사 실행
    if (businessStartDateInput && businessEndDateInput) {
      businessStartDateInput.addEventListener('change', validateBusinessTripDates);
      businessEndDateInput.addEventListener('change', validateBusinessTripDates);
    }

    break;

  case '3':
    break;
  default:

}