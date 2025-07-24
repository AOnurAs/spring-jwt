package com.AOA.service.impl;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AOA.jwt.AuthResponse;
import com.AOA.jwt.JwtService;
import com.AOA.jwt.RefreshTokenRequest;
import com.AOA.model.RefreshToken;
import com.AOA.model.User;
import com.AOA.repository.RefreshTokenRepository;
import com.AOA.service.IRefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService {
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthServiceImpl authServiceImpl;
	
	public boolean isRefreshTokenExpired(Timestamp expireDate){
	    return System.currentTimeMillis() < expireDate.getTime();
	}
	

	public RefreshToken createRefreshToken(User user) {
		return authServiceImpl.createRefreshToken(user);
	}
	
	public AuthResponse refreshToken(RefreshTokenRequest request) {
		Optional<RefreshToken> optional = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
		
		if(optional.isEmpty()) {
			System.out.println("Refresh token not valid : " + request.getRefreshToken());
			return null;
		}
		
		RefreshToken refreshToken = optional.get();
		if(isRefreshTokenExpired(refreshToken.getExpireDate())) {
			System.out.println("Refresh token expired, (" + refreshToken.getExpireDate() + "):" + request.getRefreshToken());
		}
		
		
		String newAccessToken = jwtService.generateToken(refreshToken.getUser());
		RefreshToken newRefreshToken = createRefreshToken(refreshToken.getUser());
		refreshTokenRepository.save(newRefreshToken);
		
		
		return new AuthResponse(newAccessToken, newRefreshToken.getRefreshToken());
		
	}

}
