package com.diplom.messenger.repository;

import com.diplom.messenger.entity.Chat;
import com.diplom.messenger.entity.ChatMember;
import com.diplom.messenger.entity.ChatMemberRole;
import com.diplom.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    Optional<ChatMember> findByChatAndUser(Chat chat, User user);
    List<ChatMember> findAllByChat(Chat chat);
    boolean existsByChatAndUser(Chat chat, User user);
    void deleteByChatAndUser(Chat chat, User user);
}