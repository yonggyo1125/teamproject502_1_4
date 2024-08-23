package org.hidog.file.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hidog.file.entities.FileInfo;
import org.hidog.file.services.*;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.BadRequestException;
import org.hidog.global.exceptions.RestExceptionProcessor;
import org.hidog.global.rests.JSONData;
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
    private final BeforeFileUploadProcess beforeProcess;
    private final AfterFileUploadProcess afterProcess;
    private final Utils utils;

    @PostMapping("/upload")
    public ResponseEntity<JSONData> upload(@RequestPart("file") MultipartFile[] files,
                                           @Valid org.hidog.file.controllers.RequestUpload form, Errors errors) {

        form.setFiles(files);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        beforeProcess.process(form); // 파일 업로드 전처리

        List<FileInfo> items = uploadService.upload(files, form.getGid(), form.getLocation());

        afterProcess.process(form); // 파일 업로드 후처리

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
