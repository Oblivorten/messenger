package com.diplom.messenger.repository;

import com.diplom.messenger.entity.Message;
import com.diplom.messenger.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.chat = :chat AND m.id < :cursorId ORDER BY m.id DESC")
    List<Message> findByChatWithCursor(@Param("chat") Chat chat,
                                       @Param("cursorId") Long cursorId,
                                       Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.chat = :chat ORDER BY m.id DESC")
    List<Message> findByChatNoCursor(@Param("chat") Chat chat, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.chat = :chat AND m.deletedAt IS NULL " +
            "AND m.id NOT IN (SELECT mr.message.id FROM MessageRead mr WHERE mr.user.id = :userId)")
    long countUnread(@Param("chat") Chat chat, @Param("userId") Long userId);
}