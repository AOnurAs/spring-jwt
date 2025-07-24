package com.AOA.service;

import com.AOA.jwt.AuthResponse;
import com.AOA.jwt.RefreshTokenRequest;

public interface IRefreshTokenService {
	
	public AuthResponse refreshToken(RefreshTokenRequest request);

}
