package org.hidog.board.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hidog.file.entities.FileInfo;

import java.util.List;
import java.util.UUID;

@Data
public class RequestBoard {
    private Long seq; // 게시글 번호 - 게시글 수정 시 필요(선별적인 필수) // 선별적인 필수 -> 검증이 필요함 -> 밸리데이터 정의해야 함(BoardValidator)
    private String mode = "write"; // write : 글작성, update : 글 수정, reply : 답글(부모게시글, 자식게시글) // 글 작성을 많이 하니 write로 기본값 설정

    @NotBlank
    private String bid; // 게시판 ID

    private String gid = UUID.randomUUID().toString();// UUID.randomUUID().toString(); : 글을 최초 작성할때는 랜덤으로 gid부여 // 수정때는 db에 있는 gid 가져오기

    private boolean notice; // 공지사항 여부 // true이면 공지사항이 먼저 나오게 할거임(관리자가 아닐때는 true이면 안됨)(= 선별적인 필수)

    private String category; // 게시판 분류

    @NotBlank
    private String poster; // 게시글 작성자

    private boolean guest; // 비회원

    private String guestPw; // 비회원 비밀번호(수정, 삭제 시 필요) // 미로그인 상태일때는 필수, 로그인 상태일때는 필요x(선별적인 필수)


    @NotBlank
    private String subject; // 게시글 제목

    @NotBlank
    private String content; // 게시글 내용

    //private Long parentSeq; // 부모 게시글 번호 - 답글

    // 추가필드 - 정수
    @NotNull
    private Long num1 = 0L; //가격
    @NotNull
    private Long num2 = 0L; //수량
    private Long num3;

    // 추가필드 - 한줄 텍스트
    @NotNull
    private String text1 = "default"; //상품명
    private String text2;
    private String text3;

    // 추가필드 - 여러줄 텍스트
    private String longText1;
    private String longText2;
    private String longText3;

    private List<FileInfo> editorImages; // 에디터 이미지 목록
    private List<FileInfo> attachFiles; // 첨부 파일 목록
}
