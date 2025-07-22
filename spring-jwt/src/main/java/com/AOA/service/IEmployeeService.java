package com.AOA.service;

import com.AOA.dto.DtoEmployee;

public interface IEmployeeService {
	
	DtoEmployee  findEmployeeById(Long id);
}
