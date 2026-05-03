package com.diplom.messenger.repository;

import com.diplom.messenger.entity.Attachment;
import com.diplom.messenger.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByMessage(Message message);
}