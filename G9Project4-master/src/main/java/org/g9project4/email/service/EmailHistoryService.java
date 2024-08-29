package org.g9project4.email.service;

import lombok.RequiredArgsConstructor;
import org.g9project4.email.entities.EmailHistory;
import org.g9project4.email.repositories.EmailHistoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailHistoryService {

    private final EmailHistoryRepository repository;

    public void save(EmailHistory history) {
        repository.saveAndFlush(history);
    }


}