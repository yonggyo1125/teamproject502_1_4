package org.g9project4.search.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.g9project4.member.MemberUtil;
import org.g9project4.search.constants.SearchType;
import org.g9project4.search.entities.QSearchHistory;
import org.g9project4.search.entities.SearchHistory;
import org.g9project4.search.entities.SearchHistoryId;
import org.g9project4.search.repositories.SearchHistoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {
    private final SearchHistoryRepository repository;
    private final MemberUtil memberUtil;

    public void save(String keyword, SearchType type) {
        if (!memberUtil.isLogin() || keyword == null || !StringUtils.hasText(keyword.trim())) {
            return;
        }

        SearchHistoryId searchHistoryId = new SearchHistoryId(keyword, memberUtil.getMember(), type);
        SearchHistory history = repository.findById(searchHistoryId).orElseGet(SearchHistory::new);

        history.setKeyword(keyword);
        history.setMember(memberUtil.getMember());
        history.setSearchType(type);
        history.setSearchCount(history.getSearchCount() + 1L);

        repository.saveAndFlush(history);
    }

    public void saveBoard(String keyword) {
        save(keyword, SearchType.BOARD);
    }

    public void saveTour(String keyword) {
        save(keyword, SearchType.TOUR);
    }

    public List<String> getKeywords(SearchType type) {
        if (!memberUtil.isLogin()) {
            return null;
        }

        QSearchHistory searchHistory = QSearchHistory.searchHistory;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(searchHistory.member.seq.eq(memberUtil.getMember().getSeq()))
                .and(searchHistory.searchType.eq(type));

        List<SearchHistory> items = (List<SearchHistory>)repository.findAll(builder, Sort.by(desc("searchCount")));

        return items.stream().map(SearchHistory::getKeyword).toList();
    }
}
