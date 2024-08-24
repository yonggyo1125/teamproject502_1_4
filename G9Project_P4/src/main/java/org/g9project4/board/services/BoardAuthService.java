package org.g9project4.board.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.g9project4.board.entities.Board;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.exceptions.BoardNotFoundException;
import org.g9project4.member.MemberUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Setter
@RequiredArgsConstructor
public class BoardAuthService {
    private final MemberUtil memberUtil;
    private final BoardConfigInfoService configInfoService;
    private final BoardInfoService infoService;

    private Board board;
    private BoardData boardData;

    /**
     *  권한 체크
     * @param mode
     *          - list, write, update, view ..
     * @param seq : 게시글 번호
     */
    public void check(String mode, Long seq) {

        // 관리자는 권한 체크 X
        if (memberUtil.isAdmin()) {
            return;
        }

        if (boardData == null && seq != null && seq.longValue() != 0L) {
            boardData = infoService.get(seq);
        }

        if (board == null) {
            board = boardData.getBoard();
        }

        // 게시글 목록 접근 권한 체크
        if (mode.equals("list") && boardData.isShowListButton()) {

        }



    }

    /**
     *
     * @param bid - 게시판 ID
     * @param mode - write, list
     */
    public void check(String mode, String bid) {
        if (board == null && StringUtils.hasText(bid)) {
            board = configInfoService.get(bid).orElseThrow(BoardNotFoundException::new);
        }

        check(mode, 0L);
    }
}
