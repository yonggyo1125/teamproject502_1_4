package org.g9project4.publicData.tour.constants;

import lombok.Getter;

import java.util.List;

@Getter
public enum OrderBy {
    //정렬 기본 ContentId, 이름순, 최신순, 거리순, 인기순
    title("이름순","title"),
    modifiedTime("최신순","updateDate"),
    popularity("인기순","popularity"),
    distance("10km 이내 보기","distance"),;
    private final String type;
    private final String name;

    OrderBy(String type, String name) {
        this.type = type;
        this.name = name;
    }
    public static List<OrderBy> getOrders() {
        return List.of(OrderBy.values());
    }
}
