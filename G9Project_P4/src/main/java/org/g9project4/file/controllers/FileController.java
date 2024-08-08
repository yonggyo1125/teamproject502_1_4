package org.g9project4.file.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.file.services.FileDeleteService;
import org.g9project4.file.services.FileDownloadService;
import org.g9project4.file.services.FileInfoService;
import org.g9project4.file.services.FileUploadService;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.RestExceptionProcessor;
import org.g9project4.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController implements RestExceptionProcessor {

    private final FileUploadService uploadService;
    private final FileDownloadService downloadService;
    private final FileInfoService infoService;
    private final FileDeleteService deleteService;
    private final Utils utils;

    @PostMapping("/upload")
    public ResponseEntity<JSONData> upload(@RequestPart("file") MultipartFile[] files,
                                           @Valid RequestUpload form, Errors errors) {

        if (errors.hasErrors()) {

        }

        List<FileInfo> items = uploadService.upload(files, form.getGid(), form.getLocation());
        HttpStatus status = HttpStatus.CREATED;
        JSONData data = new JSONData(items);
        data.setStatus(status);

        return ResponseEntity.status(status).body(data);
    }

    @GetMapping("/download/{seq}")
    public void download(@PathVariable("seq") Long seq) {
        downloadService.download(seq);
    }

    @DeleteMapping("/delete/{seq}")
    public JSONData delete(@PathVariable("seq") Long seq) {
        FileInfo data = deleteService.delete(seq);

        return new JSONData(data);
    }

    @DeleteMapping("/deletes/{gid}")
    public JSONData deletes(@PathVariable("gid") String gid, @RequestParam(name="location", required = false) String location) {
        List<FileInfo> items = deleteService.delete(gid, location);

        return new JSONData(items);
    }

    @GetMapping("/info/{seq}")
    public JSONData get(@PathVariable("seq") Long seq) {
        FileInfo data = infoService.get(seq);

        return new JSONData(data);
    }

    @GetMapping("/list/{gid}")
    public JSONData getList(@PathVariable("gid") String gid, @RequestParam(name="location", required = false) String location) {
        List<FileInfo> items = infoService.getList(gid, location);

        return new JSONData(items);
    }
}
