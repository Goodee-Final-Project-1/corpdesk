package com.goodee.corpdesk.home;

import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.employee.ResEmployeeDTO;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HomeService {

    @Autowired
    private EmployeeService employeeService;

    public ResEmployeeDTO getEmployeeInfo (String username) {

        ResEmployeeDTO employee = employeeService.getFulldetail(username);

        Optional<EmployeeFile> optionalFile = employeeService.getEmployeeFileByUsername(username);
        if (optionalFile.isPresent()) {
            EmployeeFile file = optionalFile.get();
            employee.setSaveName(file.getSaveName());
            employee.setExtension(file.getExtension());
        }

        return employee;

    }

}
