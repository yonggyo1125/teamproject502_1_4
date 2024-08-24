package org.g9project4.board.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.g9project4.board.entities.Board;
import org.g9project4.board.entities.BoardData;
import org.g9project4.member.MemberUtil;
import org.springframework.stereotype.Service;

@Service
@Setter
@RequiredArgsConstructor
public class BoardAuthService {
    private final MemberUtil memberUtil;

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



    }

    /**
     *
     * @param bid - 게시판 ID
     * @param mode - write, list
     */
    public void check(String mode, String bid) {
        check(mode, 0L);
    }
}
