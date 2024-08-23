package org.hidog.config.services;

import lombok.RequiredArgsConstructor;
import org.hidog.config.entities.Configs;
import org.hidog.config.repositories.ConfigsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigDeleteService {
    private final ConfigsRepository repository;

    public void delete(String code) {
        Configs config = repository.findById(code).orElse(null);
        if (config == null) {
            return;
        }

        repository.delete(config);
        repository.flush();
    }
}
