package org.g9project4.search.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.global.entities.BaseEntity;
import org.g9project4.member.entities.Member;
import org.g9project4.search.constatnts.SearchType;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SearchHistoryId.class)
public class SearchHistory extends BaseEntity {
    @Id
    private String keyword;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SearchType searchType;


    private long searchCount;
}
