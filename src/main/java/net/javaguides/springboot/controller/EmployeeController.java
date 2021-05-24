package net.javaguides.springboot.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@PostConstruct
    public void init() {
		populateTestData();
    }

	private void populateTestData() {
		Employee employee1=new Employee();
		employee1.setFirstName("fn1");
		employee1.setLastName("ln1");
		employee1.setEmail("fn1@app.com");
		employee1.setBootcampLastAttended("2020-05-09");
		employee1.setManagerUserName("mgr1@test.com");
		employeeService.saveEmployee(employee1);
		
		Employee employee2=new Employee();
		employee2.setFirstName("fn2");
		employee2.setLastName("ln2");
		employee2.setEmail("fn2@app.com");
		employee2.setBootcampLastAttended("2018-05-09");
		employee2.setManagerUserName("mgr1@test.com");
		employeeService.saveEmployee(employee2);
		
		Employee employee3=new Employee();
		employee3.setFirstName("fn3");
		employee3.setLastName("ln3");
		employee3.setEmail("fn3@app.com");
		employee3.setManagerUserName("mgr1@test.com");
		employeeService.saveEmployee(employee3);
		
		Employee employee4=new Employee();
		employee4.setFirstName("fn4");
		employee4.setLastName("ln4");
		employee4.setEmail("fn4@app.com");
		employee4.setManagerUserName("mgr2@test.com");
		employee4.setBootcampLastAttended("2020-03-09");
		employeeService.saveEmployee(employee4);
		
		Employee employee5=new Employee();
		employee5.setFirstName("fn5");
		employee5.setLastName("ln5");
		employee5.setEmail("fn5@app.com");
		employee5.setBootcampLastAttended("2020-05-02");
		employee5.setManagerUserName("mgr2@test.com");
		employeeService.saveEmployee(employee5);
	}
	
	// display list of employees
	@GetMapping("/")
	public String viewHomePage(Model model) {
		return findPaginated(1, "firstName", "asc", model);		
	}
	
	@GetMapping("/showNewEmployeeForm")
	public String showNewEmployeeForm(Model model) {
		// create model attribute to bind form data
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "new_employee";
	}
	
	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee) {
		// save employee to database
		employeeService.saveEmployee(employee);
		return "redirect:/";
	}
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {
		
		// get employee from the service
		Employee employee = employeeService.getEmployeeById(id);
		
		// set employee as a model attribute to pre-populate the form
		model.addAttribute("employee", employee);
		return "update_employee";
	}
	
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable (value = "id") long id) {
		
		// call delete employee method 
		this.employeeService.deleteEmployeeById(id);
		return "redirect:/";
	}
	
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 5;
		
		Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Employee> listEmployees = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listEmployees", listEmployees);
		return "index";
	}
}
