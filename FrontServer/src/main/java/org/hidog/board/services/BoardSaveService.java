package org.hidog.board.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hidog.board.controllers.RequestBoard;
import org.hidog.board.entities.Board;
import org.hidog.board.entities.BoardData;
import org.hidog.board.exceptions.BoardDataNotFoundException;
import org.hidog.board.exceptions.BoardNotFoundException;
import org.hidog.board.repositories.BoardDataRepository;
import org.hidog.board.repositories.BoardRepository;
import org.hidog.file.services.FileUploadDoneService;
import org.hidog.file.services.FileUploadService;
import org.hidog.member.MemberUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional // 게시판 설정 // 엔티티 영속성상태를 계속 보장하기 위해 넣어줌
@RequiredArgsConstructor
public class BoardSaveService {

    //private final BoardAuthService boardAuthService;
    private final BoardRepository boardRepository;
    private final BoardDataRepository boardDataRepository;
    private final FileUploadService fileUploadService;
    private final MemberUtil memberUtil;
    private final HttpServletRequest request;
    private final PasswordEncoder encoder;
    private final FileUploadDoneService doneService;

    public BoardData save(RequestBoard form) {
        // 반환값 BoardData : 게시글 작성 이후 이동하려면 게시글 정보가 필요함 (게시글번호 or 게시판아이디)

        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode.trim() : "write"; // requestBoard 에서 이미 write 기본값 했는데 왜 해? // 커맨드객체에서 값을 넘겨줄 때 빈값으로 넘겨줄 때도 있다...?

        /*
        // 수정 권한 체크
        if (mode.equals("update")) {
            boardAuthService.check(mode, seq);
        }
         */

        String gid = form.getGid();
        BoardData data = null;
        Long seq = form.getSeq();
        if (seq != null && mode.equals("update")) { // 글 수정
            data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new); // 게시글 조회
        } else { // 글 작성
           String bid = form.getBid(); // 게시판아이디 가져오기
           Board board = boardRepository.findById(bid).orElseThrow(BoardNotFoundException::new); // 게시판 조회

           data= BoardData.builder() // 게시글 수정일때는 바뀌면 안되는 값들
                   .gid(gid) // 파일그룹아이디
                   .board(board) // 게시판 설정
                   .member(memberUtil.getMember()) // 회원정보
                   .ip(request.getRemoteAddr()) // 브라우저에서 ip정보 가져오기
                   .ua(request.getHeader("User-Agent")) // 브라우저에서 헤더정보 가져오기
                   .build();
        }


        /* 글 작성, 글 수정 공통 S */

        data.setPoster(form.getPoster()); // 작성자
        data.setSubject(form.getSubject()); // 게시글 제목
        data.setContent(form.getContent()); // 게시글 내용
        data.setCategory(form.getCategory()); // 게시판 분류
        data.setEditorView(data.getBoard().isUseEditor()); // 에디터 사용유무

        data.setNum1(form.getNum1());
        data.setNum2(form.getNum2());
        data.setNum3(form.getNum3());

        data.setText1(form.getText1());
        data.setText2(form.getText2());
        data.setText3(form.getText3());

        data.setLongText1(form.getLongText1());
        data.setLongText2(form.getLongText2());
        data.setLongText3(form.getLongText3());

        // 비회원 비밀번호 처리(해시화)
        String guestPw = form.getGuestPw();
        if (StringUtils.hasText(guestPw)) {
            data.setGuestPw(encoder.encode(guestPw));
        }

        // 관리자 일때만 공지글 수정 가능
        if (memberUtil.isAdmin()) {
            data.setNotice(form.isNotice());
        }
        /* 글 작성, 글 수정 공통 E */


        // 게시글 저장 처리
        data = boardDataRepository.saveAndFlush(data);

        // 파일 업로드 완료 처리
        doneService.process(gid);

        return data;
    }
}
