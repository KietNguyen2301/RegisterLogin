package com.example.ResgisterLogin.Repo;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.example.ResgisterLogin.Entity.Employee;

@EnableJpaRepositories
@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer>{

	Employee findByEmail(String email);
	Optional<Employee> findOneByEmailAndPassword(String email, String encodedPassword);
	// Thêm phương thức để tìm kiếm nhân viên theo ID
    Optional<Employee> findById(int employeeId);
}
