package com.diplom.messenger.repository;

import com.diplom.messenger.entity.Message;
import com.diplom.messenger.entity.MessageRead;
import com.diplom.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MessageReadRepository extends JpaRepository<MessageRead, Long> {
    boolean existsByMessageAndUser(Message message, User user);
}