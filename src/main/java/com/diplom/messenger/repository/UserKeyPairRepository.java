package com.diplom.messenger.repository;

import com.diplom.messenger.entity.User;
import com.diplom.messenger.entity.UserKeyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserKeyPairRepository extends JpaRepository<UserKeyPair, Long> {
    Optional<UserKeyPair> findByUser(User user);
    boolean existsByUser(User user);
}