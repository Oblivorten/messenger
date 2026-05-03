package com.diplom.messenger.repository;

import com.diplom.messenger.entity.Chat;
import com.diplom.messenger.entity.ChatKey;
import com.diplom.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatKeyRepository extends JpaRepository<ChatKey, Long> {
    Optional<ChatKey> findByChatAndUser(Chat chat, User user);
}