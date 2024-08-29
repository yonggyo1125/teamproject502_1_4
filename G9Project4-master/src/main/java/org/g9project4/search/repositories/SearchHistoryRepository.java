package org.g9project4.search.repositories;

import io.lettuce.core.dynamic.annotation.Param;
import org.g9project4.member.entities.Member;
import org.g9project4.search.constatnts.SearchType;
import org.g9project4.search.entities.SearchHistory;
import org.g9project4.search.entities.SearchHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, SearchHistoryId>, QuerydslPredicateExecutor<SearchHistory> {
    List<SearchHistory> findByMember(Member member);

    // 새로운 메서드: 특정 회원, 키워드, SearchType이 TOUR인 경우의 SearchHistory를 가져오는 메서드
    @Query("SELECT sh FROM SearchHistory sh WHERE sh.member = :member AND sh.keyword = :keyword AND sh.searchType = :searchType")
    Optional<SearchHistory> findByMKeySType(@Param("member") Member member, @Param("keyword") String keyword, @Param("searchType") SearchType searchType);

}
