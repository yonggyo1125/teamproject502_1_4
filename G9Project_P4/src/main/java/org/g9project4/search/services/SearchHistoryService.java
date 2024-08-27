package org.g9project4.search.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.member.MemberUtil;
import org.g9project4.search.constants.SearchType;
import org.g9project4.search.entities.QSearchHistory;
import org.g9project4.search.entities.SearchHistory;
import org.g9project4.search.repositories.SearchHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {
    private final SearchHistoryRepository repository;
    private final MemberUtil memberUtil;

    public void save(String keyword, SearchType type) {
        if (!memberUtil.isLogin() || keyword == null || !StringUtils.hasText(keyword.trim())) {
            return;
        }

        SearchHistory history = SearchHistory.builder()
                .keyword(keyword)
                .member(memberUtil.getMember())
                .searchType(type)
                .build();

        repository.saveAndFlush(history);
    }

    public void saveBoard(String keyword) {
        save(keyword, SearchType.BOARD);
    }

    public void saveTour(String keyword) {
        save(keyword, SearchType.TOUR);
    }

    public List<String> getKeywords(SearchType type) {
        QSearchHistory searchHistory = QSearchHistory.searchHistory;

        List<SearchHistory> items = (List<SearchHistory>)repository.findAll(searchHistory.searchType.eq(type));

        return items.stream().map(SearchHistory::getKeyword).toList();
    }
}
