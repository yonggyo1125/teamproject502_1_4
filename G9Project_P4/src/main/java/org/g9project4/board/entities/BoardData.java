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
public class BoardData extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;

    private String gid;

    @JoinColumn(name="bid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String guestPw; // 비회원 비밀번호(수정, 삭제)

    private String category; // 게시글 분류

    private String poster;
    private String subject;
    private String content;

    private int viewCount; // 조회수
    private boolean editorView; // 에디터를 사용해서 글 작성했는지 여부

}
