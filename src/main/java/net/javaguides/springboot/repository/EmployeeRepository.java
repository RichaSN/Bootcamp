package net.javaguides.springboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.model.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Page<Employee> findByManagerUserName(String managerUserName, Pageable pageable);

}
