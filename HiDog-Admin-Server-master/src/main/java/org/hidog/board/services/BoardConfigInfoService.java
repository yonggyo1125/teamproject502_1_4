package org.hidog.board.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hidog.board.controllers.BoardSearch;
import org.hidog.board.controllers.RequestBoardConfig;
import org.hidog.board.entities.Board;
import org.hidog.board.entities.QBoard;
import org.hidog.board.exceptions.BoardNotFoundException;
import org.hidog.board.repositories.BoardRepository;
import org.hidog.file.entities.FileInfo;
import org.hidog.file.services.FileInfoService;
import org.hidog.global.ListData;
import org.hidog.global.Pagination;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {
    private final BoardRepository boardRepository;
    private final FileInfoService fileInfoService;
    private final HttpServletRequest request;

    //게시판 bid로 찾기
    public Board get(String bid){
        Board board = boardRepository.findById(bid).orElseThrow(BoardNotFoundException::new);

        addBoardInfo(board);

        //추가 데이터 처리

        return board;
    }

    //게시판 리스트 출력
    public List<String[]> getBoardList(){

        return boardRepository.findAll().stream().map(item -> new String[] { item.getBid(), item.getBName()}).toList();
    }


    //게시판 설정 정보 불러오기
    public RequestBoardConfig getForm(String bid){
  
        Board board = get(bid);

        RequestBoardConfig form = new ModelMapper().map(board, RequestBoardConfig.class);
        form.setListAccessType(board.getListAccessType().name());
        form.setViewAccessType(board.getViewAccessType().name());
        form.setWriteAccessType(board.getWriteAccessType().name());
        form.setReplyAccessType(board.getReplyAccessType().name());
        form.setCommentAccessType(board.getCommentAccessType().name());

        form.setMode("edit");

        return form;
    }

    public ListData<Board> getList(BoardSearch search, boolean isAll){
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;

        QBoard board = QBoard.board;
        BooleanBuilder andBuilder = new BooleanBuilder();

        /* 검색처리 S */
        String sopt = search.getSopt();
        String skey = search.getSkey();
        String bid = search.getBid();
        String bName = search.getBName();
        List<String> bids = search.getBids();

        sopt = StringUtils.hasText(sopt) ? sopt : "ALL"; //통합검색이 기본

        //키워드가 존재 할 때, 조건별 검색
        if(StringUtils.hasText(skey) && StringUtils.hasText(skey.trim())){
            /**
             * sopt 검색옵션
             * ALL - (통합검색) - bid, bName
             * bid - 게시판아이디 검색
             * bName - 게시판이름 검색
             */
            sopt = sopt.trim();
            skey = skey.trim();
            BooleanExpression condition = null;

            if(sopt.equals("ALL")){ //통합검색
                condition = board.bid.concat(board.bName).contains(skey);

            }else if(sopt.equals("bid")){ //게시판 아이디 검색
                //skey = skey.replaceAll("\\D", ""); //숫자만 남기고 검색하기
                condition = board.bid.contains(skey);

            }else if(sopt.equals("bName")){ //게시판 이름 검색
                condition = board.bName.contains(skey);

            }

            if(condition != null) andBuilder.and(condition);
        }

        if (StringUtils.hasText(bid)) { // 게시판 ID
            andBuilder.and(board.bid.contains(bid.trim()));

            if(bName != null && StringUtils.hasText(bName.trim())){//게시판 이름
                andBuilder.and(board.bName.eq(bName.trim()));
            }
        }
        /* 검색처리 E */

        //페이징 및 정렬 처리
        Pageable pageable = PageRequest.of(page-1, limit, Sort.by(desc("createdAt")));

        //데이터 조회
        Page<Board> data = boardRepository.findAll(andBuilder, pageable);

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);

        List<Board> items = data.getContent();

        return new ListData<>(items, pagination);
    }

    /**
     * 게시판 설정 추가 정보
     *      - 에디터 첨부 파일 목록
     * @param board
     */
    public void addBoardInfo(Board board) {
        String gid = board.getGid();

        List<FileInfo> htmlTopImages = fileInfoService.getList(gid, "html_top");

        List<FileInfo> htmlBottomImages = fileInfoService.getList(gid, "html_bottom");

        board.setHtmlTopImages(htmlTopImages);
        board.setHtmlBottomImages(htmlBottomImages);
    }

    /**
     * 노출 상태인 게시판 목록
     *
     * @param search
     * @return
     */
    public ListData<Board> getList(BoardSearch search) {
        return getList(search, false);
    }

    /**
     * 노출 가능한 모든 게시판 목록
     *
     * @return
     */
    public List<Board> getList() {
        QBoard board = QBoard.board;
        List<Board> items = (List<Board>)boardRepository.findAll(board.active.eq(true), Sort.by(desc("listOrder"), desc("createdAt")));

        return items;
    }
}
