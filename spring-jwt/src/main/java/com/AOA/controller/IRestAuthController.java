package com.AOA.controller;

import com.AOA.dto.DtoUser;
import com.AOA.jwt.AuthRequest;
import com.AOA.jwt.AuthResponse;

public interface IRestAuthController {
	
	public DtoUser register(AuthRequest request);
	
	public AuthResponse authenticate(AuthRequest request);

}
