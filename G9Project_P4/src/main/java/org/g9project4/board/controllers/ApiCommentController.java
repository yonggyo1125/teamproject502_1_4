package org.g9project4.board.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.CommentData;
import org.g9project4.board.services.BoardAuthService;
import org.g9project4.board.services.comment.CommentInfoService;
import org.g9project4.board.services.comment.CommentSaveService;
import org.g9project4.global.exceptions.RestExceptionProcessor;
import org.g9project4.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@SessionAttributes("commentData")
public class ApiCommentController implements RestExceptionProcessor {

    private final CommentInfoService commentInfoService;
    private final CommentSaveService commentSaveService;
    private final BoardAuthService boardAuthService;
    private CommentData commentData;

    @GetMapping("/{seq}")
    public JSONData getComment(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, model);

        return new JSONData(commentData);
    }

    @PatchMapping
    public ResponseEntity<Void> editComment(RequestComment form, Model model) {
        commonProcess(form.getSeq(), model);

        boardAuthService.check("comment_update", form.getSeq());

        form.setMode("edit");
        commentSaveService.save(form);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/auth_check")
    public JSONData authCheck(@RequestParam("seq") Long seq, Model model) {
        commonProcess(seq, model);
        JSONData data = new JSONData();
        try {
            boardAuthService.check("comment_update", seq);
        } catch (Exception err) {
            data.setSuccess(false);
            data.setMessage(err.getMessage());
        }

        return data;
    }

    @GetMapping("/auth_validate")
    public JSONData authValidate(@RequestParam("password") String password) {
        JSONData data = new JSONData();
        try {
            boardAuthService.validate(password, commentData);
        } catch (Exception err) {
            data.setSuccess(false);
            data.setMessage(err.getMessage());
        }
        return data;
    }

    private void commonProcess(Long seq, Model model) {
        commentData = commentInfoService.get(seq);
        model.addAttribute("commentData", commentData);
    }
}
