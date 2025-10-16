package com.goodee.corpdesk.employee;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.goodee.corpdesk.attendance.DTO.AttendanceEditDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.department.service.DepartmentService;
import com.goodee.corpdesk.employee.Employee.CreateGroup;
import com.goodee.corpdesk.employee.Employee.UpdateGroup;
import com.goodee.corpdesk.employee.dto.EmployeeListDTO;
import com.goodee.corpdesk.employee.validation.UpdateEmail;
import com.goodee.corpdesk.employee.validation.UpdatePassword;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;
import com.goodee.corpdesk.position.service.PositionService;
import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Controller
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    private final PositionRepository positionRepository;

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    
    
    private final DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EmployeeController(EmployeeService employeeService,
            AttendanceService attendanceService,
            PasswordEncoder passwordEncoder,
            DepartmentService departmentService,
            PositionService positionService, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, PositionRepository positionRepository) {
		this.employeeService = employeeService;
		this.attendanceService = attendanceService;
		this.passwordEncoder = passwordEncoder;
		this.departmentService = departmentService;
		this.positionService = positionService;
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
		this.positionRepository = positionRepository;
		}

    // 직원 추가 화면
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", employeeService.getAllDepartments());
        model.addAttribute("positions", employeeService.getAllPositions());
        return "employee/add";
    }

    // 직원 등록
    @PostMapping("/add")
    public String addEmployee(@Validated(CreateGroup.class) @ModelAttribute Employee employee,
                              BindingResult bindingResult, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("positions", employeeService.getAllPositions());
            return "employee/add";
        }

        if (employeeService.isUsernameExists(employee.getUsername())) {
            bindingResult.rejectValue("username", "error.employee", "이미 사용 중인 아이디입니다.");
            return "employee/add";
        }

        if (employeeService.isMobilePhoneExists(employee.getMobilePhone())) {
            bindingResult.rejectValue("mobilePhone", "error.employee", "이미 등록된 휴대전화입니다.");
            return "employee/add";
        }
        
        employeeService.addEmployee(employee);
        return "redirect:/employee/list";
    }

    // 직원 목록
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("employees", employeeService.getActiveEmployeesForList());
        return "employee/list";
    }

    
    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");

        List<EmployeeListDTO> employees = employeeService.getActiveEmployeesForList();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Employees");

            // 헤더 작성
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "이름", "아이디", "부서", "직위", "휴대폰", "입사일", "퇴사일"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 데이터 작성
            int rowNum = 1;
            for (EmployeeListDTO employee : employees) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                    employee.getName() != null ? employee.getName() : ""
                );
                row.createCell(1).setCellValue(
                    employee.getUsername() != null ? employee.getUsername() : ""
                );
                row.createCell(2).setCellValue(
                    employee.getDepartmentName() != null ? employee.getDepartmentName() : ""
                );
                row.createCell(3).setCellValue(
                    employee.getPositionName() != null ? employee.getPositionName() : ""
                );
                row.createCell(4).setCellValue(
                    employee.getMobilePhone() != null ? employee.getMobilePhone() : ""
                );

                // hireDate (null-safe)
                if (employee.getHireDate() != null) {
                    row.createCell(5).setCellValue(employee.getHireDate().toString());
                } else {
                    row.createCell(5).setCellValue("");
                }

                // lastWorkingDay (null-safe)
                if (employee.getLastWorkingDay() != null) {
                    row.createCell(6).setCellValue(employee.getLastWorkingDay().toString());
                } else {
                    row.createCell(6).setCellValue("");
                }
            }

            workbook.write(response.getOutputStream());
        }
    }

    
 // importFromExcel 메서드 내부 로직 수정
    @PostMapping("/import")
    public String importFromExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        int successCount = 0;
        int failCount = 0;

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            int rowIndex = 0;
            int consecutiveEmpty = 0;
            final int EMPTY_BREAK = 50; // 연속 50개 빈 행이면 종료

            for (Row row : sheet) {
                if (rowIndex++ == 0) continue; // 헤더(0행) 스킵

                if (isRowEmpty(row)) {
                    if (++consecutiveEmpty >= EMPTY_BREAK) break;
                    continue;
                }
                consecutiveEmpty = 0;

                try {
                    EmployeeListDTO employee = new EmployeeListDTO();
                    employee.setName(getCellValue(row, 0));

                    String username = getCellValue(row, 1).trim();
                    if (username.isEmpty()) throw new IllegalArgumentException("아이디가 비어있음");
                    employee.setUsername(username);

                    String deptName = getCellValue(row, 2).trim();
                    if (!deptName.isEmpty() && !deptName.equals("-")) {
                    	    Department dept = departmentRepository.findByDepartmentName(deptName)
                    			        .orElseThrow(() -> new IllegalArgumentException(
                    			            String.format("행 %d: 존재하지 않는 부서명 '%s'", row.getRowNum()+1, deptName)));
                    			    employee.setDepartmentId(dept.getDepartmentId());
                    			    employee.setDepartmentName(dept.getDepartmentName());
                    }

                    String posName = getCellValue(row, 3).trim();
                    if (!posName.isEmpty() && !posName.equals("-")) {
                    	    Position pos = positionRepository.findByPositionName(posName)
                    			        .orElseThrow(() -> new IllegalArgumentException(
                    			            String.format("행 %d: 존재하지 않는 직위명 '%s'", row.getRowNum()+1, posName)));
                    		    	employee.setPositionId(pos.getPositionId());
                    			    employee.setPositionName(pos.getPositionName());
                    }


                    String mobilePhone = getCellValue(row, 4).replaceAll("[^0-9]", "");
                    if (!mobilePhone.isEmpty() && !mobilePhone.matches("^01[0-9]{8,9}$")) {
                    	    throw new IllegalArgumentException(
                    	        String.format("행 %d: 유효하지 않은 휴대폰 번호 형식 '%s'", row.getRowNum()+1, mobilePhone));
                    	}
                    employee.setMobilePhone(mobilePhone);

                    employee.setHireDate(readDate(row, 5));        // ▼ (2)에서 추가할 메서드
                    employee.setLastWorkingDay(readDate(row, 6));  // ▼ (2)에서 추가할 메서드

                    employee.setUseYn(true);

                 // (업서트) username이 있으면 업데이트, 없으면 신규 생성
                    boolean exists = employeeService.isUsernameExists(username);

                    if (exists) {
                        // 휴대폰 중복: 같은 사람은 허용, 다른 사람과 충돌이면 실패
                        if (!mobilePhone.isEmpty()) {
                            boolean phoneInUseByOther = employeeService.isMobilePhoneExists(mobilePhone)
                                    && employeeRepository.findByUsername(username)
                                                         .map(e -> !mobilePhone.equals(e.getMobilePhone()))
                                                         .orElse(true);
                            if (phoneInUseByOther) {
                                log.warn("행 {}: 휴대폰 '{}'가 다른 사용자와 중복", row.getRowNum()+1, mobilePhone);
                                failCount++;
                                continue;
                            }
                        }

                        // 업데이트(비밀번호/권한은 건드리지 않음)
                        employeeService.updateFromImport(employee);
                        successCount++;

                    } else {
                        // 신규 생성
                        if (!mobilePhone.isEmpty() && employeeService.isMobilePhoneExists(mobilePhone)) {
                            log.warn("행 {}: 휴대폰 '{}' 중복", row.getRowNum()+1, mobilePhone);
                            failCount++;
                            continue;
                        }

                        Employee entity = employee.toEntity();
                        if (entity.getPassword() == null || entity.getPassword().isBlank()) {
                            entity.setPassword("1234"); // raw, addEmployee에서 encode됨
                        }
                        employeeService.addEmployee(entity);
                        successCount++;
                    }


                } catch (Exception e) {
                    log.error("직원 데이터 처리 실패 ({}행): {}", row.getRowNum()+1, e.getMessage());
                    failCount++;
                }
            }
        } catch (IOException e) {
            log.error("엑셀 파일 처리 중 오류 발생: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("message", "엑셀 파일 처리 중 오류 발생: " + e.getMessage());
            return "redirect:/employee/list";
        }
        
        // 최종 메시지 반환 로직은 하나로 통합
        redirectAttributes.addFlashAttribute("message", successCount + "명 등록 완료, 실패: " + failCount + "명");
        return "redirect:/employee/list";
    }

 // 셀 값 가져오기 헬퍼 메서드 수정
    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return "";
        }
        
        // DataFormatter를 사용하여 셀의 실제 타입에 상관없이 값을 문자열로 가져옵니다.
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        int first = row.getFirstCellNum();
        int last = row.getLastCellNum();
        if (first < 0 || last < 0) return true;
        DataFormatter fmt = new DataFormatter();
        for (int c = first; c < last; c++) {
            Cell cell = row.getCell(c);
            if (cell == null) continue;
            if (cell.getCellType() != CellType.BLANK) {
                String v = fmt.formatCellValue(cell);
                if (v != null && !v.trim().isEmpty()) return false;
            }
        }
        return true;
    }

    private LocalDate readDate(Row row, int idx) {
        Cell cell = row.getCell(idx);
        if (cell == null) return null;

        // 1) 엑셀 날짜 셀(서식 포함)
        if (DateUtil.isCellDateFormatted(cell)) {
            try {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            } catch (Exception ignore) {
                return cell.getDateCellValue().toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
            }
        }

        // 2) 문자열/숫자 직렬값 파싱
        String s = getCellValue(row, idx);
        if (s == null || s.isBlank() || "-".equals(s.trim())) return null;

        // 숫자 직렬값(예: 45292)
        if (s.matches("\\d{3,6}")) {
            try { return DateUtil.getLocalDateTime(Double.parseDouble(s)).toLocalDate(); }
            catch (Exception ignore) {}
        }

        DateTimeFormatter[] fmts = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("yyyy.MM.dd"),
            DateTimeFormatter.ofPattern("yyyyMMdd"),
            DateTimeFormatter.ofPattern("M/d/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        };
        for (DateTimeFormatter f : fmts) {
            try { return LocalDate.parse(s.trim(), f); } catch (Exception ignored) {}
        }
        return null; // 못 읽으면 null (필요 시 예외로 바꿔도 됨)
    }





    

    // 직원 수정 페이지
    @GetMapping("/edit/{username}")
    public String editEmployeePage(@PathVariable("username") String username, Model model) {
        Employee employee = employeeService.getEmployeeOrThrow(username);
        model.addAttribute("employee", employee);

        Optional<EmployeeFile> employeeFileOpt = employeeService.getEmployeeFileByUsername(username);
        employeeFileOpt.ifPresent(file -> model.addAttribute("employeeFile", file));

        model.addAttribute("attendanceList", employeeService.getAttendanceByUsername(username));
        model.addAttribute("departments", employeeService.getAllDepartments());
        model.addAttribute("positions", positionService.getAllActive());

        return "employee/edit";
    }

    // 직원 수정 처리
    @PostMapping("/edit")
    public String editEmployee(@Validated(UpdateGroup.class) @ModelAttribute Employee employeeFromForm,
                               BindingResult bindingResult, Model model,
                               @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile)
                               throws Exception {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("positions", employeeService.getAllPositions());
            return "employee/edit";
        }

        employeeService.updateEmployee(employeeFromForm, profileImageFile);
        return "redirect:/employee/list";
    }

    // 프로필 이미지 삭제
    @PostMapping("/deleteProfileImage")
    @ResponseBody
    public String deleteProfileImage(@RequestParam("username") String username) {
        try {
            employeeService.deleteProfileImage(username);
            return "success";
        } catch (Exception e) {
            log.error("직원 {}의 프로필 이미지 삭제 실패: {}", username, e);
            return "fail";
        }
    }

    @PostMapping("/delete/{username}")
    @ResponseBody
    public Map<String, Object> deleteEmployee(
            @PathVariable("username") String username,
            @RequestParam(value = "lastWorkingDay", required = false)
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            LocalDate lastWorkingDay) {

        Map<String, Object> result = new HashMap<>();
        try {
            employeeService.deactivateEmployee(username, lastWorkingDay); // ▼ 변경된 서비스 호출
            result.put("success", true);
            result.put("message", "삭제(비활성화)되었습니다.");
        } catch (IllegalStateException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "삭제 중 오류가 발생했습니다.");
        }
        return result;
    }





	// FIXME: 배포 전에 삭제 해야됨
//	@GetMapping
//	public String link() {
//		return "employee/link";
//	}
//
//    @GetMapping("sample_page")
//    public void sample() {
//    }
//
//    @GetMapping("sign_in")
//    public String signIn() {
//        return "sample/sign_in";
//    }
//
//    @GetMapping("reset")
//    public String reset() {
//        return "sample/reset_password";
//    }

    @GetMapping("join")
    public void join() {
    }

    @PostMapping("join")
    public String join(Employee employee) {
        employeeService.join(employee);
        return "employee/link";
    }

    @GetMapping("detail")
    public void detail(Authentication authentication, Model model) {
        Map<String, Object> map = employeeService.detail(authentication.getName());
        model.addAttribute("employee", map.get("employee"));
        model.addAttribute("department", map.get("department"));
        model.addAttribute("position", map.get("position"));
    }

	@GetMapping("salary")
	public String salary(@PageableDefault(size = 5) Pageable pageable, Authentication authentication, Model model) {
		int number = pageable.getPageNumber();
		pageable = pageable.withPage(number == 0 ? 0 : number - 1);
		Page<EmployeeSalaryDTO> page = employeeService.getSalaryList(authentication.getName(), pageable);

		int currentPage = page.getNumber();

		int startPage = (int) Math.floor(currentPage / 5) * 5;
		int endPage = (int) Math.min(startPage + 4, page.getTotalPages() - 1);
		endPage = endPage < 0 ? 0 : endPage;

		boolean isFirst = startPage < 1;
		boolean isLast = endPage == page.getTotalPages() - 1;

//		model.addAttribute("page", page);
		model.addAttribute("salaryList", page.getContent());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("isFirst", isFirst);
		model.addAttribute("isLast", isLast);


		return  "employee/salary";
	}

	@GetMapping("salary/{paymentId}")
	public String salaryDetail(@PathVariable("paymentId") Long paymentId,
			Authentication authentication, Model model) throws JsonProcessingException {

		return "employee/salaryDetail";
	}

	@GetMapping("salary/detail")
	@ResponseBody
	public Map<String, Object> salaryDetail(Long paymentId, Authentication authentication) throws JsonProcessingException {

		Map<String, Object> map = employeeService.getSalaryDetail(authentication.getName(), paymentId);

		return map;
	}

    @GetMapping("update/email")
    public String updateEmail(Authentication authentication, Model model) {
        Employee employee = employeeService.detailSecret(authentication.getName());
        model.addAttribute("employee", employee);
        return "employee/update_email";
    }

	@PostMapping("update/email")
	public String updateEmail(Authentication authentication, @Validated(UpdateEmail.class) Employee employee,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "employee/update_email";
		}

        employee.setUsername(authentication.getName());
        Employee result = employeeService.updateEmail(employee);

		if (result == null) {
			return "employee/update_email";
		}

        return "redirect:/employee/link";
    }

    @GetMapping("update/password")
    public String updatePassword(Authentication authentication, Model model) {
        Employee employee = employeeService.detailSecret(authentication.getName());
        model.addAttribute("employee", employee);
        return "employee/update_password";
    }

    @PostMapping("update/password")
	public String updatePassword(Authentication authentication, @Validated(UpdatePassword.class) Employee employee,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "employee/update_password";
		}

		employee.setUsername(authentication.getName());
		Employee result = employeeService.updatePassword(employee, bindingResult);

		if (result == null) {
			return "employee/update_password";
		}

        return "redirect:/logout";
    }

    @PostMapping("{username}/attendance/delete")
    @ResponseBody
    public Map<String, Object> deleteAttendance(@PathVariable("username") String username,
                                                @RequestBody Map<String, List<Long>> payload) {
        List<Long> attendanceIds = payload.get("attendanceIds");
        Map<String, Object> result = new HashMap<>();
        try {
            attendanceService.deleteAttendances(username, attendanceIds);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }

    @PostMapping("{username}/attendance/add")
    @ResponseBody
    public Map<String, Object> addAttendance(
        @PathVariable(name = "username") String username,   // 명시적으로 name 지정
        @RequestBody AttendanceEditDTO dto) {

        Map<String, Object> result = new HashMap<>();
        try {
            Attendance att = new Attendance();
            att.setUsername(username);
            att.setWorkStatus(dto.getWorkStatus());
            att.setUseYn(true);

            if (dto.getDateTime() != null && !dto.getDateTime().isEmpty()) {
                LocalDateTime dt = LocalDateTime.parse(dto.getDateTime(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                if ("출근".equals(dto.getWorkStatus())) {
                    att.setCheckInDateTime(dt);
                } else if ("퇴근".equals(dto.getWorkStatus())) {
                    att.setCheckOutDateTime(dt);
                }
            }

            Attendance saved = attendanceService.saveOrUpdateAttendance(att);
            result.put("success", true);
            result.put("attendanceId", saved.getAttendanceId());
            result.put("workStatus", saved.getWorkStatus());
            result.put("dateTime", saved.getCheckInDateTime() != null ?
                saved.getCheckInDateTime().format(formatterOutput) :
                (saved.getCheckOutDateTime() != null ?
                    saved.getCheckOutDateTime().format(formatterOutput) : null));

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;

    }

    @PostMapping("{username}/attendance/edit")
    @ResponseBody
    public Map<String, Object> editAttendance(@PathVariable("username") String username,
                                              @RequestBody AttendanceEditDTO dto) {

        Map<String, Object> result = new java.util.HashMap<>();
        try {
            Attendance attendance = attendanceService.getAttendanceById(dto.getAttendanceId());

            if (!org.springframework.util.StringUtils.hasText(dto.getWorkStatus())) {
                attendance.setWorkStatus("-");
            } else {
                attendance.setWorkStatus(dto.getWorkStatus());
            }

            if (org.springframework.util.StringUtils.hasText(dto.getDateTime())) {
                LocalDateTime dateTime = LocalDateTime.parse(dto.getDateTime(), formatterInput);
                if ("출근".equals(attendance.getWorkStatus())) {
                    attendance.setCheckInDateTime(dateTime);
                } else if ("퇴근".equals(attendance.getWorkStatus())) {
                    attendance.setCheckOutDateTime(dateTime);
                }
            }

            attendanceService.saveOrUpdateAttendance(attendance);

            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;

    }
}
