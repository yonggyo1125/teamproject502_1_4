package org.g9project4.search.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.global.entities.BaseEntity;
import org.g9project4.member.entities.Member;
import org.g9project4.search.constants.SearchType;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SearchHistoryId.class)
@Table(indexes=@Index(name="idx_search_history_viewcount", columnList = "viewCount DESC"))
public class SearchHistory extends BaseEntity {
    @Id
    private String keyword;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Id
    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private SearchType searchType;

    private long searchCount;
}
