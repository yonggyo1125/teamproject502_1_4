package org.hidog.global.services;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.hidog.config.services.ConfigInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApiConfigService {
    private final ConfigInfoService infoService;
    private Map<String, String> configs;

    public String get(String code) {

        Map<String, String> configs = getAll();
        return configs == null ? "" : Objects.requireNonNullElse(configs.get(code), "");
    }

    private Map<String, String> getAll() {
        if (configs == null) {
            configs = infoService.get("apiConfig", new TypeReference<Map<String, String>>() {}).orElseGet(null);
        }

        return configs;
    }
}
