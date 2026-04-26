package com.diplom.messenger.repository;

import com.diplom.messenger.entity.RefreshToken;
import com.diplom.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findAllByUser(User user);
    @Transactional
    void deleteByToken(String token);
}