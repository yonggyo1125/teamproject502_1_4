package org.hidog.board.services;

import lombok.RequiredArgsConstructor;
import org.hidog.board.entities.Board;
import org.hidog.board.repositories.BoardRepository;
import org.hidog.file.services.FileDeleteService;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.script.AlertException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardConfigDeleteService {

    private final BoardConfigInfoService infoService;
    private final FileDeleteService fileDeleteService;
    private final BoardRepository boardRepository;
    private final Utils utils;

    public void delete(String bid) {
        Board board = infoService.get(bid);
        String gid = board.getGid();

        boardRepository.delete(board); //게시판삭제
        boardRepository.flush();
        fileDeleteService.delete(gid); //게시판 관련 파일삭제
    }

    public void deleteList(List<Integer> chks) {
        if(chks == null || chks.isEmpty()) {
            throw new AlertException("삭제할 게시판을 선택하세요.", HttpStatus.BAD_REQUEST);
        }

        for(int chk : chks){
            String bid = utils.getParam("bid_" + chk);
            Board board = boardRepository.findById(bid).orElse(null);
            if(board == null) continue;
            delete(bid);
        }
    }
}
