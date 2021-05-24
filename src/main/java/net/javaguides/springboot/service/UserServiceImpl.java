package net.javaguides.springboot.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.dto.UserRegistrationDto;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	@Override
	public User save(UserRegistrationDto registrationDto) {
		String firstName = registrationDto.getFirstName();
		String lastName = registrationDto.getLastName();
		String email = registrationDto.getEmail();
		User user = new User(firstName, 
				lastName, email,
				passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("ROLE_HR")));
		
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public User saveManager(UserRegistrationDto registrationDto) {
		String firstName = registrationDto.getFirstName();
		String lastName = registrationDto.getLastName();
		String email = registrationDto.getEmail();
		User user = new User(firstName, 
				lastName, email,
				passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("ROLE_MANAGER")));
		Employee employee = new Employee();
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setEmail(email);
		employeeService.saveEmployee(employee);
		
		return userRepository.save(user);
	}
}
