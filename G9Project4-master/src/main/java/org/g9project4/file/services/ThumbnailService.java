package org.g9project4.file.services;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.g9project4.file.controllers.RequestThumb;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class ThumbnailService {

    private final FileProperties properties;
    private final FileInfoService infoService;
    private final RestTemplate restTemplate;

    public String create(RequestThumb form) {
        try {
            Long seq = form.getSeq();
            String url = form.getUrl();
            int width = form.getWidth() == null || form.getWidth() < 10 ? 10 : form.getWidth();
            int height = form.getHeight() == null || form.getHeight() < 10 ? 10 : form.getHeight();

            /**
             * Thumb이미지가 이미 존재하면 생성 X 경로만 반환
             */
            String thumbPath = getThumbPath(seq, url, width, height);
            File _thumbPath = new File(thumbPath);
            if (_thumbPath == null)
                return null;

            if (_thumbPath.exists()) {
                return thumbPath;
            }

            if (seq != null && seq > 0L) { // 파일 등록번호
                FileInfo fileInfo = infoService.get(seq);
                Thumbnails.of(fileInfo.getFilePath())
                        .size(width, height)
                        .toFile(thumbPath);
            } else { // 파일 URL
                String orgPath = String.format("%s_original", thumbPath);
                byte[] bytes = restTemplate.getForObject(URI.create(url), byte[].class);
                Files.write(Paths.get(orgPath), bytes);
                Thumbnails.of(orgPath)
                        .size(width, height)
                        .toFile(thumbPath);
            }

            return thumbPath;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getThumbPath(Long seq, String url, int width, int height) {
        if ((seq == null || seq < 1L) && !StringUtils.hasText(url)) {
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