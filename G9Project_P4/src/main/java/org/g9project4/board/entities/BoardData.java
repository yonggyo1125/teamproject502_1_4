package org.g9project4.board.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.global.entities.BaseEntity;
import org.g9project4.member.entities.Member;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = @Index(name="idx_board_data", columnList = "notice DESC, createdAt DESC"))
public class BoardData extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;

    @Column(length=65, nullable = false)
    private String gid;

    @JoinColumn(name="bid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
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
    private boolean editorView; // 에디터를 사용해서 글 작성했는지 여부

    @Column(length=20)
    private String ip; // IP 주소

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
}
