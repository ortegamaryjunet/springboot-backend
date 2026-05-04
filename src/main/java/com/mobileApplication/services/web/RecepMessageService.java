package com.mobileApplication.services.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mobileApplication.dto.web.MessageDTO;
import com.mobileApplication.models.web.RecepMessage;
import com.mobileApplication.repositories.web.RecepMessageRepository;

@Service
public class RecepMessageService {
	
	private final RecepMessageRepository messageRepository;

    public RecepMessageService(RecepMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // 🔥 GET conversation
    public List<MessageDTO> getConversation(Long patientId) {
        return messageRepository.findByPatientIdOrderByCreatedAtAsc(patientId)
                .stream()
                .map(m -> new MessageDTO(
                        m.getId(),
                        m.getSender(),
                        m.getContent(),
                        m.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // 🔥 SEND message (from receptionist)
    public void sendFromReceptionist(Long patientId, String message) {
        RecepMessage msg = new RecepMessage();
        msg.setPatientId(patientId);
        msg.setSender("RECEPTIONIST");
        msg.setContent(message);
        msg.setCreatedAt(LocalDateTime.now());
        msg.setRead(false);

        messageRepository.save(msg);
    }

    // 🔥 mark messages as read
    public void markAsRead(Long patientId) {
        List<RecepMessage> messages = messageRepository.findByPatientIdOrderByCreatedAtAsc(patientId);

        for (RecepMessage m : messages) {
            if (!m.isRead() && "PATIENT".equals(m.getSender())) {
                m.setRead(true);
            }
        }

        messageRepository.saveAll(messages);
    }
	

}
