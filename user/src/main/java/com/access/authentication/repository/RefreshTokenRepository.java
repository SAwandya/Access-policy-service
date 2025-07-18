package com.access.authentication.repository;

import com.access.authentication.model.RefreshToken;
import com.access.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByUser(User user);

    @Modifying
    int deleteByUser(User user);
}
