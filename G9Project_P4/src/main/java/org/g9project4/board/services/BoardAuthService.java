package org.g9project4.board.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.member.MemberUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardAuthService {
    private final MemberUtil memberUtil;

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
}
