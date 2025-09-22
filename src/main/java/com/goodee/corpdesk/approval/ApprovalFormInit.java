package com.goodee.corpdesk.approval;

import com.goodee.corpdesk.approval.entity.ApprovalForm;
import com.goodee.corpdesk.approval.repository.ApprovalFormRepository;
import com.goodee.corpdesk.vacation.entity.VacationType;
import com.goodee.corpdesk.vacation.repository.VacationTypeRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApprovalFormInit implements InitializingBean {

	@Autowired
	private ApprovalFormRepository approvalFormRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	private void init() {
        ApprovalForm form1 = new ApprovalForm();
        form1.setFormTitle("휴가 신청");
        form1.setFormContent("""
            <br>
            <table class="table table-bordered">
                <tbody>
                    <tr>
                      <th class="col-2 table-light align-middle">휴가 종류</th>
                      <td>
                          <div class="form-group">
                              <select name="vacationTypeId" class="form-control" id="vacationTypeId">
                                  <c:forEach items="${vacationTypeList }" var="el">
                                      <option value="${el.vacationTypeId}">${el.vacationTypeName}</option>
                                  </c:forEach>
                              </select>
                          </div>
                      </td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">휴가 기간</th>
                        <td>
                            <div class="d-flex align-items-center">
                                <input name="startDate" type="date" class="form-control col-sm-5 mb-2" id="startDate"><p>&nbsp;&nbsp;&nbsp;&nbsp;~</p>
                            </div>
                            <input name="endDate" type="date" class="form-control col-sm-5" id="endDate">
                        </td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">사용일수</th>
                      <td><input name="usedDays" type="number" class="form-control" id="usedDays" readonly></td>
                    </tr>
                    <tr>
                      <th class="table-light align-middle">휴가 사유</th>
                      <td>
                        <textarea name="reason" class="form-control" rows="5"></textarea>
                      </td>
                    </tr>
                </tbody>
            </table>
        """);
        approvalFormRepository.save(form1);        
        
        ApprovalForm form2 = new ApprovalForm();
        form2.setFormTitle("출장 신청");
        form2.setFormContent("""
          <br>
          <h5 class="p-3">출장 내용</h5>
          <table class="table table-bordered">
            <tbody>
            <tr>
              <th class="col-2 table-light align-middle">출장 기간</th>
              <td class="col-4">
                  <div class="d-flex align-items-center">
                    <input name="startDate" type="date" class="form-control col-sm-10 mb-2" id="startDate"><p>&nbsp;&nbsp;&nbsp;&nbsp;~</p>
                  </div>
                  <input name="endDate" type="date" class="form-control col-sm-10" id="endDate">
              </td>
              <th class="col-2 table-light align-middle">출장지</th>
              <td class="col-4 align-middle"><input name="destination" type="text" class="form-control"></td>
            </tr>
            <tr>
              <th class="table-light align-middle">교통편</th>
              <td colspan="3"><input name="transportation" type="text" class="form-control"></td>
            </tr>
            <tr>
              <th class="table-light align-middle">출장목적</th>
              <td colspan="3"><input name="purpose" type="text" class="form-control"></td>
            </tr>
            <tr>
              <th class="table-light align-middle">비고</th>
              <td colspan="3"><input name="note" type="text" class="form-control"></td>
            </tr>
            </tbody>
          </table>

          <br>
          <h5 class="p-3">출장자 정보</h5>
          <table class="table table-bordered">
            <tbody>
            <tr>
              <th class="col-2 table-light align-middle">성명</th>
              <td><input name="travelerName" type="text" class="form-control"></td>
              <th class="col-2 table-light align-middle">직위</th>
              <td><input name="travelerPosition" type="text" class="form-control"></td>
            </tr>
            <tr>
              <th class="table-light align-middle">소속</th>
              <td><input name="travelerDepartment" type="text" class="form-control"></td>
              <th class="table-light align-middle">전화번호</th>
              <td><input name="travelerPhone" type="text" class="form-control"></td>
            </tr>
            </tbody>
          </table>

          <!-- 출장여비 -->
          <br>
          <h5 class="p-3">출장여비</h5>
          <table class="table table-bordered">
            <tbody>
            <tr>
              <th class="col-2 table-light">구분</th>
              <th class="table-light">산출내역</th>
              <th class="table-light">금액</th>
            </tr>
            <tr>
              <th class="table-light align-middle">교통비</th>
              <td><input name="trafficBreakdown" type="text" class="form-control"></td>
              <td><input name="trafficAmount" type="number" class="form-control amount-input"></td>
            </tr>
            <tr>
              <th class="table-light align-middle">일비</th>
                <td><input name="dailyBreakdown" type="text" class="form-control"></td>
                <td><input name="dailyAmount" type="number" class="form-control amount-input"></td>
            </tr>
            <tr>
              <th class="table-light align-middle">식비</th>
                <td><input name="foodBreakdown" type="text" class="form-control"></td>
                <td><input name="foodAmount" type="number" class="form-control amount-input"></td>
            </tr>
            <tr>
              <th class="table-light align-middle">숙박비</th>
                <td><input name="lodgingBreakdown" type="text" class="form-control"></td>
                <td><input name="lodgingAmount" type="number" class="form-control amount-input"></td>
            </tr>
            <tr>
              <th class="table-light align-middle">기타</th>
                <td><input name="otherBreakdown" type="text" class="form-control"></td>
                <td><input name="otherAmount" type="number" class="form-control amount-input"></td>
            </tr>
            <tr>
              <th class="table-light">계</th>
              <th></th>
              <th id="total-amount" class="text-right">0</th>
            </tr>
            </tbody>
          </table>
        """);
        approvalFormRepository.save(form2);
        
        ApprovalForm form3 = new ApprovalForm();
        form3.setFormTitle("업무 기안");
        form3.setFormContent("""
          <br>
          <table class="table table-bordered">
            <tbody>
            <tr>
              <th class="col-2 table-light align-middle">시행일자</th>
              <td><input name="executionDate" type="date" class="form-control"></td>
              <th class="col-2 table-light align-middle">협조부서</th>
              <td>
                  <div class="form-group">
                      <select name="coopDepartmentId" class="form-control" id="departmentId">
                          <c:forEach items="${departmentList }" var="el">
                              <option value="${el.departmentId}">${el.departmentName}</option>
                          </c:forEach>
                      </select>
                  </div>
              </td>
            </tr>
            <tr>
              <th class="table-light align-middle">합의</th>
              <td colspan="3">${userInfo.departmentName}</td>
            </tr>
            <tr>
              <th class="table-light align-middle">제목</th>
              <td colspan="3"><input name="draftTitle" type="text" class="form-control"></td>
            </tr>
            <tr>
              <th class="table-light align-middle">내용</th>
              <td colspan="3">
                  <textarea name="draftContent" class="form-control" rows="5"></textarea>
              </td>
            </tr>
            </tbody>
          </table>
        """);
        approvalFormRepository.save(form3);
	}
}
