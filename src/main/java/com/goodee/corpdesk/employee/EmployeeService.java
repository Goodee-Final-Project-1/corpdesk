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

    public void update(Employee employee) {
        String encoded = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encoded);
        employee.setEmployeeId(1);  // FIXME: save()로 업데이트 하려면 pk가 필요함

        employeeRepository.save(employee);
    }
}
