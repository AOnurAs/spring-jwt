package com.AOA.service.impl;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.AOA.dto.DtoUser;
import com.AOA.jwt.AuthRequest;
import com.AOA.jwt.AuthResponse;
import com.AOA.jwt.JwtService;
import com.AOA.model.RefreshToken;
import com.AOA.model.User;
import com.AOA.repository.RefreshTokenRepository;
import com.AOA.repository.UserRepository;
import com.AOA.service.IAuthService;

@Service
public class AuthServiceImpl implements IAuthService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	public RefreshToken createRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setRefreshToken(UUID.randomUUID().toString());
		refreshToken.setExpireDate(new Timestamp(System.currentTimeMillis() + 1000 * 60 * 60 * 4));
		refreshToken.setUser(user);
		
		return refreshToken;
	}

	@Override
	public AuthResponse authenticate(AuthRequest request) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
			authenticationProvider.authenticate(authenticationToken);
			
			Optional<User> optional = userRepository.findByUsername(request.getUsername());
			
			if(optional.isEmpty()) {
				System.out.println("Username couldnt be found");		
				return null;
			}
			String accessToken = jwtService.generateToken(optional.get());
			RefreshToken refreshToken = createRefreshToken(optional.get());
			refreshTokenRepository.save(refreshToken);
					;
			return new AuthResponse(accessToken, refreshToken.getRefreshToken());
					
		} catch (Exception e) {
			System.out.println("Wrong password - username combination (e : " + e.getMessage() + ")");
		}
		return null;
	}
	
	@Override
	public DtoUser register(AuthRequest request) {
		DtoUser dtoUser = new DtoUser();
		User user = new User();
		
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		User savedUser = userRepository.save(user);
		BeanUtils.copyProperties(savedUser, dtoUser);
		
		return dtoUser;
	}

}
