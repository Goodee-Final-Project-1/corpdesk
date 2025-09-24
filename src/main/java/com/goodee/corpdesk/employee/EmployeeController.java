package com.goodee.corpdesk.employee;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.goodee.corpdesk.attendance.AttendanceService;
import com.goodee.corpdesk.employee.Employee.CreateGroup;
import com.goodee.corpdesk.employee.Employee.UpdateGroup;
import com.goodee.corpdesk.employee.validation.UpdateEmail;
import com.goodee.corpdesk.employee.validation.UpdatePassword;
import com.goodee.corpdesk.file.entity.EmployeeFile;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Controller
@RequestMapping("/employee/**")
@Slf4j
public class EmployeeController {

    private final PasswordEncoder passwordEncoder;

    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;

    public EmployeeController(EmployeeService employeeService, AttendanceService attendanceService, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
        this.passwordEncoder = passwordEncoder;
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
                              BindingResult bindingResult, Model model) {
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
        model.addAttribute("employees", employeeService.getActiveEmployees());
        return "employee/list";
    }
    
    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");

        List<Employee> employees = employeeService.getActiveEmployees();

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
            for (Employee employee : employees) {
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

    
    @PostMapping("/employee/import")
    public String importFromExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        int successCount = 0;
        int skipUsernameCount = 0;
        int skipMobileCount = 0;

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 첫 행은 헤더
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String username = getStringCellValue(row.getCell(1));
                String mobilePhone = getStringCellValue(row.getCell(4));

                // username 중복 체크
                if (employeeService.isUsernameExists(username)) {
                    log.warn("중복된 username {}. 해당 직원은 건너뜀.", username);
                    skipUsernameCount++;
                    continue; // 다음 행으로
                }

                // 휴대폰 중복 체크
                if (!mobilePhone.isEmpty() && employeeService.isMobilePhoneExists(mobilePhone)) {
                    log.warn("중복된 휴대폰 {}. 해당 직원은 건너뜀.", mobilePhone);
                    skipMobileCount++;
                    continue; // 다음 행으로
                }

                Employee employee = new Employee();
                employee.setName(getStringCellValue(row.getCell(0)));
                employee.setUsername(username);
                employee.setDepartmentName(getStringCellValue(row.getCell(2)));
                employee.setPositionName(getStringCellValue(row.getCell(3)));
                employee.setMobilePhone(mobilePhone);
                employee.setHireDate(getLocalDateFromCell(row.getCell(5)));
                employee.setLastWorkingDay(getLocalDateFromCell(row.getCell(6)));
                employee.setEnabled(true);

                // 기본 비밀번호 설정
                String defaultPassword = "1234"; 
                employee.setPassword(passwordEncoder.encode(defaultPassword));

                employeeService.addEmployee(employee);
                successCount++;
            }

            redirectAttributes.addFlashAttribute("message",
                "Excel 가져오기 완료! 성공: " + successCount 
                + "명, username 중복 건너뜀: " + skipUsernameCount 
                + "명, 휴대폰 중복 건너뜀: " + skipMobileCount + "명");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Excel 가져오기 실패: " + e.getMessage());
        }

        return "redirect:/employee/list";
    }

    // Cell에서 String 값 가져오는 유틸
    private String getStringCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue().trim();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long) cell.getNumericCellValue());
        return cell.toString().trim();
    }

    // Cell에서 LocalDate 가져오는 메소드
    private LocalDate getLocalDateFromCell(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            } else {
                String text = cell.getStringCellValue().trim();
                if (text.isEmpty()) return null;

                DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("yyyy.M.d")
                };

                for (DateTimeFormatter formatter : formatters) {
                    try {
                        return LocalDate.parse(text, formatter);
                    } catch (Exception ignored) {}
                }

                log.warn("지원하지 않는 날짜 형식: {}", text);
            }
        } catch (Exception e) {
            log.error("날짜 변환 실패: {}", e.getMessage());
        }
        return null;
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
        model.addAttribute("positions", employeeService.getAllPositions());

        return "employee/edit";
    }

    // 직원 수정 처리
    @PostMapping("/edit")
    public String editEmployee(@Validated(UpdateGroup.class) @ModelAttribute Employee employeeFromForm,
                               BindingResult bindingResult, Model model,
                               @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile) {

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

    // 직원 삭제(비활성화)
    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") String id) {
        employeeService.deactivateEmployee(id);
        return "redirect:/employee/list";
    }

    // 단순 링크
    @GetMapping
    public String link() {
        return "employee/link";
    }

    @GetMapping("sample_page")
    public void sample() {
    }

    @GetMapping("sign_in")
    public String signIn() {
        return "sample/sign_in";
    }

    @GetMapping("reset")
    public String reset() {
        return "sample/reset_password";
    }

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
            return "employee/detail";
        }

        employee.setUsername(authentication.getName());
        Employee result = employeeService.updateEmail(employee);

        if (result == null) {
            return "employee/detail";
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
            return "employee/update";
        }

        employee.setUsername(authentication.getName());
        Employee result = employeeService.updatePassword(employee);

        if (result == null) {
            return "employee/update";
        }

        return "redirect:/logout";
    }
}
