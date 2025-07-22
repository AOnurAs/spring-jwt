package com.AOA.controller;

import com.AOA.config.dto.DtoUser;
import com.AOA.jwt.AuthRequest;

public interface IRestAuthController {
	
	public DtoUser register(AuthRequest request);

}
