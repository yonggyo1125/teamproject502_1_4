package org.hidog.board.services;

import lombok.RequiredArgsConstructor;
import org.hidog.board.controllers.RequestBoardConfig;
import org.hidog.board.entities.Board;
import org.hidog.board.repositories.BoardRepository;
import org.hidog.file.services.FileUploadDoneService;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.script.AlertException;
import org.hidog.member.constants.Authority;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardConfigSaveService {

    private final BoardRepository boardRepository;
    private final FileUploadDoneService fileUploadDoneService;
    private final Utils utils;

    public void save(RequestBoardConfig form) {
        String bid = form.getBid();
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "add";

        Board board = boardRepository.findById(bid).orElseGet(Board::new);

        if(mode.equals("add")){ //게시판 등록시 gid, bid 등록 -> 수정시에는 변경 X
            board.setBid(bid);
            board.setGid(form.getGid());
        }

        board.setBName(form.getBName());
        board.setActive(form.isActive());
        board.setRowsPerPage(form.getRowsPerPage());
        board.setPageCountPc(form.getPageCountPc());
        board.setPageCountMobile(form.getPageCountMobile());
        board.setUseReply(form.isUseReply());
        board.setUseComment(form.isUseComment());
        board.setUseEditor(form.isUseEditor());
        board.setUseUploadImage(form.isUseUploadImage());
        board.setUseUploadFile(form.isUseUploadFile());
        board.setLocationAfterWriting(form.getLocationAfterWriting());
        board.setShowListBelowView(form.isShowListBelowView());
        board.setSkin(form.getSkin());
        board.setCategory(form.getCategory());

        board.setListAccessType(Authority.valueOf(form.getListAccessType()));
        board.setViewAccessType(Authority.valueOf(form.getViewAccessType()));
        board.setWriteAccessType(Authority.valueOf(form.getWriteAccessType()));
        board.setReplyAccessType(Authority.valueOf(form.getReplyAccessType()));
        board.setCommentAccessType(Authority.valueOf(form.getCommentAccessType()));

        board.setHtmlTop(form.getHtmlTop());
        board.setHtmlBottom(form.getHtmlBottom());

        board.setListOrder(form.getListOrder());

        boardRepository.saveAndFlush(board);

        // 파일 업로드 완료 처리
        fileUploadDoneService.process(board.getGid());
    }

    //게시글 다중 수정
    public void saveList(List<Integer> chks){
        if(chks == chks || chks.isEmpty()){
            throw new AlertException("수정할 게시판을 선택하세요.", HttpStatus.BAD_REQUEST);
        }

        for (int chk : chks){
            String bid = utils.getParam("bid_" + chk);
            Board board = boardRepository.findById(bid).orElse(null);
            if(board == null) continue;

            boolean active = Boolean.parseBoolean(utils.getParam("active_" + chk)); //사용자가 입력한 사용여부
            board.setActive(active); //사용여부 변경

            int listOrder = Integer.parseInt(utils.getParam("listOrder_" + chk)); //사용자가 입력한 진열가중치
            board.setListOrder(listOrder); //진열가중치 변경
        }

        boardRepository.flush();
    }
}
