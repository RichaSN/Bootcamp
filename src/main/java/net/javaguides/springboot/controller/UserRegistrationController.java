package net.javaguides.springboot.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.javaguides.springboot.dto.UserRegistrationDto;
import net.javaguides.springboot.service.UserService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
	
	@Autowired
	private UserService userService;


	@PostConstruct
    public void init() {
		populateTestManagerData();
    }

	private void populateTestManagerData() {
		// TODO Auto-generated method stub
		UserRegistrationDto dtoAdmin = new UserRegistrationDto("admFn1", "admLn1", "test@test.com", "test");
		userService.save(dtoAdmin);
		UserRegistrationDto dto1 = new UserRegistrationDto("mgrFn1", "mgrLn1", "mgr1@test.com", "test");
		userService.saveManager(dto1);
		UserRegistrationDto dto2 = new UserRegistrationDto("mgrFn2", "mgrLn2", "mgr2@test.com", "test");
		userService.saveManager(dto2);
		UserRegistrationDto dto3 = new UserRegistrationDto("mgrFn3", "mgrLn3", "mgr3@test.com", "test");
		userService.saveManager(dto3);
		UserRegistrationDto dto4 = new UserRegistrationDto("mgrFn4", "mgrLn4", "mgr4@test.com", "test");
		userService.saveManager(dto4);
	}
	
	@ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
	
	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
		userService.save(registrationDto);
		return "redirect:/registration?success";
	}
}
