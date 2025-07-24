package com.AOA.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AOA.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
	
	//@Query(value = "FROM refreshTokenFrom r WHERE r.refreshToken = :refreshToken")
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	
}
