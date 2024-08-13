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
import org.g9project4.file.services.FileUploadDoneService;
import org.g9project4.member.MemberUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardSaveService {

    private final HttpServletRequest request;
    private final PasswordEncoder encoder;
    private final BoardRepository boardRepository;
    private final BoardDataRepository boardDataRepository;
    private final MemberUtil memberUtil;
    private final FileUploadDoneService doneService;

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

        data.setNum1(form.getNum1());
        data.setNum2(form.getNum2());
        data.setNum3(form.getNum3());

        data.setText1(form.getText1());
        data.setText2(form.getText2());
        data.setText3(form.getText3());

        data.setLongText1(form.getLongText1());
        data.setLongText2(form.getLongText2());

        // 비회원 비밀번호 처리
        String guestPw = form.getGuestPw();
        if (StringUtils.hasText(guestPw)) {
            data.setGuestPw(encoder.encode(guestPw));
        }

        if (memberUtil.isAdmin()) {
            data.setNotice(form.isNotice());
        }
        /* 글작성, 글 수정 공통 E */

        // 게시글 저장 처리
        boardDataRepository.saveAndFlush(data);

        // 파일 업로드 완료 처리
        doneService.process(gid);

        return data;
    }
}
