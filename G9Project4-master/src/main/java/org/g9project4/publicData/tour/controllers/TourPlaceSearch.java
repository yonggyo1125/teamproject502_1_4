package org.g9project4.publicData.tour.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.global.CommonSearch;
import org.g9project4.publicData.tour.constants.OrderBy;
import lombok.*;
//import org.g9project4.global.RequestPage;
import org.g9project4.publicData.tour.constants.ContentType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourPlaceSearch extends CommonSearch {
    /**
     * 필터 옵션
     * 1. 태그들
     * 1-1 AreaCode
     *     1.구 소분류(시군구 코드)
     * 1-2 ContentType
     * 1-3 Category
     *      1. category1
     *      2. category2
     *      3. category3
     * 2. 검색 키워드
     * 3. 정렬 이름순, 최신순, 거리순, 인기순
     *
     */

    private String areaCode;
    private List<String> sigunguCode;
    private String contentType;
    private String category1;
    private String category2;
    private String category3;
    private Double latitude;
    private Double longitude;
    private Integer radius = 10000;
    private String orderBy;


    private String contentId; // 게시글 번호
}
