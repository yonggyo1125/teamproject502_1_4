package org.g9project4.board.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.Board;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.services.BoardInfoService;
import org.g9project4.board.services.admin.BoardAdminService;
import org.g9project4.global.ListData;
import org.g9project4.global.constants.DeleteStatus;
import org.g9project4.global.exceptions.RestExceptionProcessor;
import org.g9project4.global.rests.JSONData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board/admin")
@RequiredArgsConstructor
public class BoardAdminController implements RestExceptionProcessor {

    private final BoardInfoService boardInfoService;
    private final BoardAdminService boardAdminService;

    @GetMapping //목록 조회
    public JSONData getList(BoardDataSearch search){
        ListData<BoardData> data = boardInfoService.getList(search, DeleteStatus.ALL);

        return new JSONData(data);
    }

    @PatchMapping ("/{mode}")//목록 수정, 삭제
    public JSONData updatelist(@PathVariable("mode") String mode, @RequestBody RequestAdminList form){
        List<BoardData> items = boardAdminService.update(mode, form.getItems());

        return new JSONData(items);
    }

    @PatchMapping("/{mode}/{seq}") // 게시글 하나, 수정 삭제
    public JSONData update(@PathVariable("mode") String mode, @PathVariable("seq") Long seq, RequestBoard form){
        form.setSeq(seq);

        BoardData item = boardAdminService.update(mode, form);

        return new JSONData(item);
    }

    @GetMapping("/info/{seq}")
    public JSONData getInfo(Long seq){
        BoardData item = boardInfoService.get(seq, DeleteStatus.ALL);

        return new JSONData(item);
    }
}
