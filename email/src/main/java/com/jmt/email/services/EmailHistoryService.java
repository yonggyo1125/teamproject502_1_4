package com.jmt.email.services;

import com.jmt.email.entities.EmailHistory;
import com.jmt.email.repositories.EmailHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailHistoryService {

    private final EmailHistoryRepository repository;

    public void save(EmailHistory history) {
        repository.saveAndFlush(history);
    }
}
