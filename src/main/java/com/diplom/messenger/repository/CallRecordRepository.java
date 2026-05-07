package com.diplom.messenger.repository;

import com.diplom.messenger.entity.CallRecord;
import com.diplom.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CallRecordRepository extends JpaRepository<CallRecord, Long> {

    @Query("SELECT c FROM CallRecord c WHERE c.initiator = :user OR c.chat.id IN " +
            "(SELECT cm.chat.id FROM ChatMember cm WHERE cm.user = :user) " +
            "ORDER BY c.startedAt DESC")
    List<CallRecord> findAllByUser(@Param("user") User user);
}