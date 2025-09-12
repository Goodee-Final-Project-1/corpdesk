package com.goodee.corpdesk.employee;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Employee> optional = employeeRepository.findByUsername(username);
		
		return optional.get();
	}
	
	public void join(Employee employee) {
		String encoded = passwordEncoder.encode(employee.getPassword());
		employee.setPassword(encoded);
		
		employeeRepository.save(employee);
	}

	public Employee detail(String username) {
		Optional<Employee> optional = employeeRepository.findByUsername(username);
		return optional.get();
	}
	
}
