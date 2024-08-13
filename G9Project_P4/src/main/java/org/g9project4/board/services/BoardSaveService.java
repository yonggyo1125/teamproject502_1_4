package org.g9project4.board.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.controllers.RequestBoard;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.repositories.BoardDataRepository;
import org.g9project4.board.repositories.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BoardSaveService {

    private final BoardRepository boardRepository;
    private final BoardDataRepository boardDataRepository;

    public BoardData save(RequestBoard form) {

        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode.trim() : "write";

        Long seq = form.getSeq();
        if (seq != null && mode.equals("update")) { // 글 수정

        } else { // 글 작성

        }

        return null;
    }
}
