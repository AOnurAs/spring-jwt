package com.AOA.service;

import com.AOA.config.dto.DtoUser;
import com.AOA.jwt.AuthRequest;

public interface IAuthService {
	
	public DtoUser register(AuthRequest request);

}
