package org.g9project4.file.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.file.controllers.RequestThumb;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Objects;

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

            if (seq != null && seq > 0L) { // 파일 등록번호

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getThumbPath(Long seq, String url, int width, int height) {
        if (seq == null && !StringUtils.hasText(url)) {
            return null;
        }

        String dir = properties.getPath() + "/thumb/";
        dir += seq != null ? seq % 10L : Objects.hash(url);
        File _dir = new File(dir);
        if (!_dir.exists()) {
            _dir.mkdirs();
        }


        if (seq != null) { // seq : 파일 등록번호
            FileInfo fileInfo = infoService.get(seq);
            dir += String.format("/%d_%d_%d%s", width, height, seq, fileInfo.getExtension());
        } else { // URL
            String extension = url.substring(url.lastIndexOf("."));
            dir += String.format("/%d_%d_%d%s", width, height, Objects.hash(url), extension);
        }

        return dir;
    }
}
