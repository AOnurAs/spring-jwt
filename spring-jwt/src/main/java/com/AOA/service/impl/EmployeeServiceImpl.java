package com.AOA.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AOA.dto.DtoDepartment;
import com.AOA.dto.DtoEmployee;
import com.AOA.model.Department;
import com.AOA.model.Employee;
import com.AOA.repository.EmployeeRepository;
import com.AOA.service.IEmployeeService;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public DtoEmployee findEmployeeById(Long id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		
		if(optional.isEmpty()) {
			//exception
			return null;
		}
		
		Employee dbEmployee = optional.get();
		Department dbDepartment = dbEmployee.getDepartment();
		DtoEmployee dtoEmployee = new DtoEmployee();
		DtoDepartment dtoDepartment = new DtoDepartment();
		
		BeanUtils.copyProperties(dbEmployee, dtoEmployee);
		BeanUtils.copyProperties(dbDepartment, dtoDepartment);
		
		dtoEmployee.setDepartment(dtoDepartment);
		return dtoEmployee;
	}
	
	
}
