package com.AOA.service.impl;

import java.util.Optional;

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
import com.AOA.model.User;
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
			String token = jwtService.generateToken(optional.get());
			return new AuthResponse(token);
			
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
