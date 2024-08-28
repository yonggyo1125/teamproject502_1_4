package org.g9project4.file.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.file.controllers.RequestThumb;
import org.g9project4.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class ThumbnailService {

    private final FileProperties properties;
    private final FileInfoService infoService;

    public String create(RequestThumb form) {
        try {
            Long seq = form.getSeq();
            String url = form.getUrl();
            int width = form.getWidth() == null || form.getWidth() < 10 ? 10 : form.getWidth();
            int height = form.getHeight() == null || form.getHeight() < 10 ? 10 : form.getHeight();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
