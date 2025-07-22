package com.AOA.controller;

import com.AOA.dto.DtoEmployee;

public interface IRestEmployeeController {
	
	public DtoEmployee findEmployeeById(Long id);

}
