package org.hidog.board.services;

import lombok.RequiredArgsConstructor;
import org.hidog.board.entities.BoardData;
import org.hidog.board.repositories.BoardDataRepository;
import org.hidog.file.services.FileDeleteService;
import org.hidog.global.constants.DeleteStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardDeleteService {
    private final BoardDataRepository boardDataRepository;
    private final BoardInfoService boardInfoService;
    private final FileDeleteService fileDeleteService;

    /**
     * SOFT 삭제, 삭제 시간 업데이트
     * 게시글 완전삭제 아님 -> 그저 사용자에게 보이지 않게만 처리
     * 삭제한 게시글 복구가능하게 하기 위해서
     *
     * @param seq
     * @return
     */
    public BoardData delete(Long seq) {
        BoardData data = boardInfoService.get(seq);
        data.setDeletedAt(LocalDateTime.now());

        boardDataRepository.saveAndFlush(data);

        return data;
    }

    /**
     * 게시글 완전 삭제
     *
     * @param seq
     */
    @Transactional
    public BoardData completeDelete(Long seq) {
         BoardData data = boardInfoService.get(seq, DeleteStatus.ALL);

         String gid = data.getGid(); // 파일 조회

        // 업로드된 파일 삭제
        fileDeleteService.delete(gid);

        boardDataRepository.delete(data);
        boardDataRepository.flush();

        return data;
    }

    /**
     * 게시글 복구
     * - 삭제 일시 -> null
     *
     * @param seq
     * @return
     */
    public BoardData recover(Long seq) {
        BoardData item = boardInfoService.get(seq); // 게시글 가져오기
        item.setDeletedAt(null);

        boardDataRepository.saveAndFlush(item);

        return item;
    }
}
