package org.g9project4.board.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.g9project4.board.controllers.RequestBoard;
import org.g9project4.board.entities.Board;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.exceptions.BoardDataNotFoundException;
import org.g9project4.board.exceptions.BoardNotFoundException;
import org.g9project4.board.repositories.BoardDataRepository;
import org.g9project4.board.repositories.BoardRepository;
import org.g9project4.member.MemberUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardSaveService {

    private final HttpServletRequest request;
    private final BoardRepository boardRepository;
    private final BoardDataRepository boardDataRepository;
    private final MemberUtil memberUtil;

    public BoardData save(RequestBoard form) {

        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode.trim() : "write";

        String gid = form.getGid();

        BoardData data = null;
        Long seq = form.getSeq();
        if (seq != null && mode.equals("update")) { // 글 수정
            data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);
        } else { // 글 작성
            String bid = form.getBid();
            Board board = boardRepository.findById(bid).orElseThrow(BoardNotFoundException::new);

            data = BoardData.builder()
                    .gid(gid)
                    .board(board)
                    .member(memberUtil.getMember())
                    .ip(request.getRemoteAddr())
                    .ua(request.getHeader("User-Agent"))
                    .build();
        }

        /* 글작성, 글 수정 공통 S */
        data.setPoster(form.getPoster());
        data.setSubject(form.getSubject());
        data.setContent(form.getContent());
        data.setCategory(form.getCategory());
        data.setEditorView(data.getBoard().isUseEditor());

        /* 글작성, 글 수정 공통 E */

        return null;
    }
}
