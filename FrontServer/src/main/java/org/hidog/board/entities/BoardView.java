package org.hidog.board.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(BoardViewId.class)
public class BoardView {
    @Id
    private Long seq; // 게시글 번호

    @Id
    @Column(name="_uid")
    private int uid;
}
