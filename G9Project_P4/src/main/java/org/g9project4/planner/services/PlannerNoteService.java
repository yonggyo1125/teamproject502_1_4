package org.g9project4.planner.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.controllers.RequestBoard;
import org.g9project4.global.exceptions.script.AlertBackException;
import org.g9project4.planner.entities.Planner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class PlannerNoteService {
    private final PlannerInfoService infoService;
    /**
     * 여행 노트 게시판 공통 처리 부분
     *
     * @param model
     */
    public void commonProcess(RequestBoard form, Model model) {
        Long num1 = form.getNum1();
        if (num1 == null || num1 < 1L) {
            throw new AlertBackException("BadRequest", HttpStatus.BAD_REQUEST);
        }

        // 플래너 정보 조회
        Planner planner = infoService.get(num1);
        model.addAttribute("planner", planner);

        if (!StringUtils.hasText(form.getSubject())) {
            form.setSubject(planner.getTitle());
        }
    }

    public void commonProcess(RequestBoard form, Errors errors, Model model) {

        // 추가 검증 처리

        commonProcess(form, model);
    }
}
