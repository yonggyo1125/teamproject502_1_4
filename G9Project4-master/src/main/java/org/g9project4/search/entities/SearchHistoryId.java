package org.g9project4.search.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g9project4.member.entities.Member;
import org.g9project4.search.constatnts.SearchType;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryId {
    private String keyword;
    private Member member;
    private SearchType searchType;
}
