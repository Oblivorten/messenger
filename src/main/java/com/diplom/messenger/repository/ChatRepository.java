package com.diplom.messenger.repository;

import com.diplom.messenger.entity.Chat;
import com.diplom.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c JOIN c.members m WHERE m.user = :user")
    List<Chat> findAllByMember(@Param("user") User user);
}