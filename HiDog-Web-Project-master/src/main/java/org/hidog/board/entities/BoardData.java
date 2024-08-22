package org.hidog.board.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hidog.file.entities.FileInfo;
import org.hidog.global.entities.BaseEntity;
import org.hidog.member.entities.Member;

import java.util.List;


@Data
@Builder
@Entity
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
        @Index(name="idx_boardData_basic", columnList = "notice DESC, listOrder DESC, listOrder2 ASC, createdAt DESC")
}) // @Index : 빠르게 조회위해 사용
// notice DESC : 공지는 항상 앞에 나와야 하니 orderby desc, createdAt DESC : 최신글이 먼저 나오도록
public class BoardData extends BaseEntity { // extends BaseEntity : 날짜와 시간
    @Id @GeneratedValue
    private Long seq; // 게시글 번호

    @ManyToOne(fetch = FetchType.LAZY) // 게시판입장에서 게시글은 여러개 // many가 관계의 주인, 외래키도 있는 곳
    @JoinColumn(name="bid") // 게시판 별 게시글 구분
    //@Column(updatable=false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY) // 권한설정 // 한명의 회원이 여러개의 게시글 작성 -> 게시글 : many, 회원 : one
    @JoinColumn(name="memberSeq")
    //@Column(updatable=false)
    private Member member;

    @Column(length=65, nullable = false, updatable=false)
    private String gid; // 게시글하나에 여러개의 파일을 묶는 groupid

    @Column(length=60)
    private String category; // 게시글 분류

    @Column(length=40, nullable = false)
    private String poster; // 작성자

    @Column(length=65) // 나중에 비크립트해시화 할거라 length=65
    private String guestPw; // 비회원 비밀번호(수정, 삭제)

    private boolean notice;  // 공지글 여부 - true : 공지글 // 공지는 항상 앞에 나와야 하니 오더바이 desc // 1, 0 형태

    @Column(nullable = false)
    private String subject; // 게시글 제목

    @Lob
    @Column(nullable = false)
    private String content; // 게시글 내용

    private int viewCount; // 조회수 // 우리 사이트는 조회수가 그렇게 많진 않을 테니 자료형 int로,,,

    private int commentCount; // 댓글 수

    private boolean editorView; // true : 에디터를 통해서 작성한 경우
    // 에디터 작성글은 기본적으로 html형태로 데이터가 들어감
    // 에디터 사용하지 않은 글은 <\n>로 들어감 -> <br> 태그로 바꾸어 주어야 함
    // 에디터를 사용하다가 사용하지않은경우가 있을 수 있음
    // 에디터를 사용하는 경우 : 그냥 출력
    // 에디터를 사용하지 않은경우 : \n -> <br> 로 바꿔서 출력

    //private Long parentSeq; // 부모 게시글 번호 - 답글인 경우

    private Long listOrder; // 1차 정렬 순서 - 내림차순

    @Column(length=60)
    private String listOrder2 = "R"; // 답글 2차 정렬 -> 오름차순

    //private int depth; // 답글 들여쓰기 정도

    @Column(length=20, updatable=false)
    private String ip; // IP 주소
    // 커뮤니티 사이트의 경우 이상한 회원 차단시키는 용
    // updatable=false : 내가 차단시킨 회원의 ip가 내 ip로 수정되면 안됨, 그럼 내가 차단됨

    @Column(length=150, updatable=false)
    private String ua; // User-Agent : 브라우저 정보
    // 통계 : 요즘 사용자는 어떤 장비를 통해서 브라우저 접속하나, 사용자는 보통 모바일, pc 중에 어떤걸 통해 많이 접속하나

    private Long num1; // 추가 필드1 : 정수
    private Long num2; // 추가 필드2 : 정수
    private Long num3; // 추가 필드3 : 정수
    // 추가필드가 필요한 이유 : 게시글을 쓰는데 상품후기, 상품문의를 만들고 싶음
    // -> 상품번호가 필요함,
    // -> 근데 보드데이터엔티티에 상품번호 칼럼을 넣기엔 너무 짜침 + 다양하게 활용 힘듬
    // -> 추가필드를 활용하자
    // num1 = 상품번호로 활용

    @Column(length=100)
    private String text1; // 추가 필드1 : 한줄 텍스트

    @Column(length=100)
    private String text2; // 추가 필드2 : 한줄 텍스트

    @Column(length=100)
    private String text3; // 추가 필드3 : 한줄 텍스트

    // ex) 상품 후기?

    @Lob
    private String longText1; // 추가 필드1 : 여러줄 텍스트

    @Lob
    private String longText2; // 추가 필드2 : 여러줄 텍스트

    @Lob
    private String longText3; // 추가 필드3 : 여러줄 텍스트

    @Transient
    private List<FileInfo> editorImages; // 에디터 첨부 이미지 파일 목록

    @Transient
    private List<FileInfo> attachFiles; // 첨부 파일 목록

    @Transient
    private boolean editable; // 수정, 삭제 가능 여부

    @Transient
    private boolean commentable; // 댓글 수정, 삭제 가능 여부

    @Transient
    private boolean viewable; // 상세쪽 조회 가능 여부

    @Transient
    private boolean listable; // 목록쪽 조회 가능 여부

    @Transient
    private String formattedCreatedAt; // 게시글 조회용 날짜
}