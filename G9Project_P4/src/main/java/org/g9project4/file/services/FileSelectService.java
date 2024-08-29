package org.g9project4.file.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.file.constants.FileStatus;
import org.g9project4.file.controllers.RequestSelect;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.file.repositories.FileInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileSelectService {
    private final FileInfoService infoService;
    private final FileInfoRepository repository;

    public void process(List<Long> seqs, String gid, String location) {
        List<FileInfo> items = infoService.getList(gid, location, FileStatus.ALL);
        items.forEach(item -> {
            item.setSelected(seqs != null && !seqs.isEmpty() && seqs.contains(item.getSeq()));
        });

        repository.saveAllAndFlush(items);
    }

    public void process(List<Long> seqs, String gid) {
        process(seqs, gid, null);
    }

    public void process(RequestSelect form) {
        process(form.getSeq(), form.getGid(), form.getLocation());
    }
}
