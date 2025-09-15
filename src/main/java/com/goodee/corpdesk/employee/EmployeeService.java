package com.goodee.corpdesk.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService implements UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Employee> optional = employeeRepository.findById(username);
		
		return optional.get();
	}
	
	public void join(Employee employee) {
		String encoded = passwordEncoder.encode(employee.getPassword());
		employee.setPassword(encoded);
		
		employeeRepository.save(employee);
	}

	public Employee detail(String username) {
		Optional<Employee> optional = employeeRepository.findById(username);
		return optional.get();
	}

    public void updatePassword(Employee param) {
        Optional<Employee> optional = employeeRepository.findById(param.getUsername());
        Employee  employee = optional.get();
        String encoded = passwordEncoder.encode(param.getPassword());
        employee.setPassword(encoded);

        employeeRepository.save(employee);
    }

    public void updateEmail(Employee param) {
        Optional<Employee> optional = employeeRepository.findById(param.getUsername());
        Employee  employee = optional.get();

        employee.setExternalEmail(param.getExternalEmail());

        employeeRepository.save(employee);
    }
}
