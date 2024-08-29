package org.g9project4.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.g9project4.file.entities.FileInfo;

import java.util.List;
import java.util.UUID;

@Data
public class RequestBoard {
    private Long seq; // 게시글 번호 - 글 수정시 필요.
    private String mode = "write"; // write : 글 작성, update - 글 수정  기본값으로 write

    @NotBlank
    private String bid; // 게시판 ID

    private String gid = UUID.randomUUID().toString(); // 중복 되지 않는

    private boolean notice; // 공지글 여부

    private String category;

    @NotBlank
    private String poster; //작성자

    private boolean guest; //비회원
    private String guestPw; //비회원 비밀번호(수정, 삭제)

    @NotBlank
    private String subject;

    @NotBlank
    private String content;


    private Long num1; // 정수 추가 필드1
    private Long num2; // 정수 추가 필드2
    private Long num3; // 정수 추가 필드3

    private String text1; // 한줄 텍스트 추가 필드1
    private String text2;
    private String text3;

    private String longText1;
    private String longText2;

    private List<FileInfo> editorImages;
    private List<FileInfo> attachFiles;

}
