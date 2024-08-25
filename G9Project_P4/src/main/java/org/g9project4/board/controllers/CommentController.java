package org.g9project4.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.CommentData;
import org.g9project4.board.exceptions.GuestPasswordCheckException;
import org.g9project4.board.services.BoardAuthService;
import org.g9project4.board.services.comment.CommentDeleteService;
import org.g9project4.board.services.comment.CommentInfoService;
import org.g9project4.board.services.comment.CommentSaveService;
import org.g9project4.board.validators.CommentValidator;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.CommonException;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.global.exceptions.script.AlertException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
@SessionAttributes({"commentSeq"})
public class CommentController implements ExceptionProcessor {

    private final CommentValidator commentValidator;
    private final CommentInfoService commentInfoService;
    private final CommentSaveService commentSaveService;
    private final CommentDeleteService commentDeleteService;
    private final BoardAuthService boardAuthService;

    private final Utils utils;

    /**
     * 댓글 저장, 수정 처리
     *
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestComment form, Errors errors, Model model) {

        commentValidator.validate(form, errors);

        if (errors.hasErrors()) {
            FieldError error = errors.getFieldErrors().stream().findFirst().orElse(null);

            throw new AlertException(utils.getMessage(error == null || error.getCodes() == null ? "BadRequest" :  error.getCodes()[0]), HttpStatus.BAD_REQUEST);
        }

        CommentData commentData = commentSaveService.save(form); // 댓글 저장, 수정

        String script = String.format("parent.location.replace('/board/view/%d?comment_id=%d');", commentData.getBoardData().getSeq(), commentData.getSeq());

        model.addAttribute("script", script);

        return "common/_execute_script";
    }

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model) {


        model.addAttribute("commentSeq", seq);

        boardAuthService.check("comment_delete", seq);

        Long boardDataSeq = commentDeleteService.delete(seq);

        return "redirect:/board/view/" + boardDataSeq;
    }

    @ExceptionHandler(GuestPasswordCheckException.class)
    public ModelAndView errorHandler(GuestPasswordCheckException passwordCheckException, HttpServletRequest request, Model model) {

        ModelAndView mv = new ModelAndView();
        Long commentSeq = (Long)model.getAttribute("commentSeq");
        CommentData data = commentInfoService.get(commentSeq);

        mv.setStatus(passwordCheckException.getStatus());
        mv.addObject("commentData", data);
        mv.addObject("boardData", data.getBoardData());
        mv.addObject("board", data.getBoardData().getBoard());

        mv.setViewName(utils.tpl("board/password"));
        return mv;
    }
}