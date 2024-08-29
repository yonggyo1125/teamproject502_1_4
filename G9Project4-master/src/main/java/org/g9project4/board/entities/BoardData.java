package org.g9project4.board.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.global.entities.BaseEntity;
import org.g9project4.member.entities.Member;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = @Index(name="idx_board_data", columnList = "notice DESC, createdAt DESC"))
public class BoardData extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;

    @Column(length=65, nullable = false, updatable = false)
    private String gid;

    @JoinColumn(name="bid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Cascade 설정
    private Member member;

    @Column(length=65)
    private String guestPw; // 비회원 비밀번호(수정, 삭제)

    @Column(length=60)
    private String category; // 게시글 분류

    private boolean notice; // 공지글 여부

    @Column(length=40, nullable = false)
    private String poster;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    private int viewCount; // 조회수
    private int commentCount; // 댓글 수

    private boolean editorView; // 에디터를 사용해서 글 작성했는지 여부

    @Column(length=20, updatable = false)
    private String ip; // IP 주소

    @Column(updatable = false)
    private String ua; // User-Agent

    private Long num1; // 정수 추가 필드1
    private Long num2; // 정수 추가 필드2
    private Long num3; // 정수 추가 필드3

    @Column(length=100)
    private String text1; // 한줄 텍스트 추가 필드1

    @Column(length=100)
    private String text2; // 한줄 텍스트 추가 필드2

    @Column(length=100)
    private String text3; // 한줄 텍스트 추가 필드3

    @Lob
    private String longText1; // 여러줄 텍스트 추가 필드1

    @Lob
    private String longText2; // 여러줄 텍스트 추가 필드2

    @Transient // 에디터 첨부 이미지 파일 목록
    private List<FileInfo> editorImages;

    @Transient // 첨부 파일 목록
    private List<FileInfo> attachFiles;

    @Transient // 선택 이미지 목록
    private List<FileInfo> selectedImages;

    @Transient
    private boolean editable; // 수정, 삭제 가능 여부

    @Transient
    private boolean commentable; // 댓글 작성 가능 여부

    @Transient
    private boolean showWrite; // 글쓰기 버튼 노출 여부

    @Transient
    private boolean showEdit; // 수정 버튼 노출 여부

    @Transient
    private boolean showDelete; // 글삭제 버튼 노출 여부

    @Transient
    private boolean showList; // 글목록 버튼 노출 여부

    @Transient
    private boolean mine; // 게시글 소유자

    @Transient
    @JsonIgnore
    private List<CommentData> comments; // 댓글 목록
}