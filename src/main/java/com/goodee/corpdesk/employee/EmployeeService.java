package com.goodee.corpdesk.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
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

    public Employee updatePassword(Employee employee) {
        Optional<Employee> optional = employeeRepository.findById(employee.getUsername());
        Employee origin = optional.get();

        if (passwordEncoder.matches(origin.getPassword(), employee.getPasswordNew())) {
            throw new BadCredentialsException("비밀번호가 다릅니다.");
        }

        if (!Objects.equals(employee.getPasswordNew(), employee.getPasswordCheck())) {
            throw new RuntimeException("비밀번호 확인이 다릅니다.");
        }

        String encoded = passwordEncoder.encode(employee.getPasswordNew());

        origin.setPassword(encoded);
        return employeeRepository.save(origin);
    }

    public Employee updateEmail(Employee employee) {
        Optional<Employee> optional = employeeRepository.findById(employee.getUsername());
        Employee origin = optional.get();

        origin.setExternalEmail(employee.getExternalEmail());
        origin.setExternalEmailPassword(employee.getExternalEmailPassword());

        return employeeRepository.save(origin);
    }
}
