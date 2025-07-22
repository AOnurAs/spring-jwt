package com.AOA.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AOA.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
