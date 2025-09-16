package com.goodee.corpdesk.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService implements UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AesBytesEncryptor aesBytesEncryptor;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Employee> employeeOp = employeeRepository.findById(username);
		Employee employee = employeeOp.get();
		Optional<Role> roleOp = roleRepository.findById(employee.getRoleId());
		Role role = roleOp.get();
		employee.setRole(role);

		return employee;
	}

	public void join(Employee employee) {
		String encoded = passwordEncoder.encode(employee.getPassword());
		employee.setPassword(encoded);

		employeeRepository.save(employee);
	}

	public Employee detail(String username) {
		Optional<Employee> optional = employeeRepository.findById(username);
		Employee employee = optional.get();

		if (employee.getEncodedEmailPassword() != null) {
			byte[] decoded = aesBytesEncryptor.decrypt(employee.getEncodedEmailPassword());
			employee.setExternalEmailPassword(new String(decoded));
		}

		return employee;
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

		byte[] encoded = aesBytesEncryptor.encrypt(employee.getExternalEmailPassword().getBytes());

		origin.setExternalEmail(employee.getExternalEmail());
		origin.setEncodedEmailPassword(encoded);

		return employeeRepository.save(origin);
	}
}
