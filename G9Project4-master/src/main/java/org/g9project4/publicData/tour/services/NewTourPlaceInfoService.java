package org.g9project4.publicData.tour.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.g9project4.config.controllers.ApiConfig;
import org.g9project4.config.service.ConfigInfoService;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.g9project4.global.Utils;
import org.g9project4.publicData.tour.exceptions.TourPlaceNotFoundException;
import org.g9project4.global.rests.gov.api.ApiItem;
import org.g9project4.global.rests.gov.api.ApiResult;
import org.g9project4.publicData.tour.constants.ContentType;
import org.g9project4.publicData.tour.constants.OrderBy;
import org.g9project4.publicData.tour.controllers.TourPlaceSearch;
import org.g9project4.publicData.tour.entities.QTourPlace;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewTourPlaceInfoService {
    private final RestTemplate restTemplate;

    @PersistenceContext
    private EntityManager em;

    private final Utils utils;
    private final TourPlaceRepository tourPlaceRepository;
    private final HttpServletRequest request;
    private final ConfigInfoService configInfoService;

    /**
     * 필터 옵션
     * 1. 태그들
     * 1-1 AreaCode
     * 1.구 소분류(시군구 코드)
     * 1-2 ContentType
     * 1-3 Category
     * 1. category1
     * 2. category2
     * 3. category3
     * 2. 검색 키워드
     * 3. 정렬 이름순, 최신순, 거리순, 인기순
     *
     * @param search
     * @return
     */
    public ListData<TourPlace> getSearchedList(TourPlaceSearch search) {

        /* 검색 조건 처리 S */
        QTourPlace tourPlace = QTourPlace.tourPlace;
        BooleanBuilder andBuilder = new BooleanBuilder();
        BooleanBuilder orBuilder = new BooleanBuilder();
        //AreaCode
        if (StringUtils.hasText(search.getAreaCode())) {
            andBuilder.and(tourPlace.areaCode.eq(search.getAreaCode()));
            if (search.getSigunguCode() != null) {
                for (String sigunguCode : search.getSigunguCode()) {
                    orBuilder.or(tourPlace.sigunguCode.eq(sigunguCode));
                }
            }
        }
        // ContentType
        if (StringUtils.hasText(search.getContentType())) {
            andBuilder.and(tourPlace.contentTypeId.eq(utils.typeCode(search.getContentType()).getId()));
        }
        //카테고리
        if (StringUtils.hasText(search.getCategory1())) {
            andBuilder.and(tourPlace.category1.eq(search.getCategory1()));
            if (StringUtils.hasText(search.getCategory2())) {
                andBuilder.and(tourPlace.category2.eq(search.getCategory2()));
            }
            if (StringUtils.hasText(search.getCategory3())) {
                andBuilder.and(tourPlace.category3.eq(search.getCategory3()));
            }
        }

        //검색 키워드
        String sopt = search.getSopt();
        String skey = search.getSkey();
        sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL";

        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            BooleanExpression titleCond = tourPlace.title.contains(skey); // 제목 - subject LIKE '%skey%';
            BooleanExpression addressCond = tourPlace.address.contains(skey); // 내용 - content LIKE '%skey%';

            if (sopt.equals("TITLE")) { // 여행지 이름

                andBuilder.and(titleCond);

            } else if (sopt.equals("ADDRESS")) { // 주소

                andBuilder.and(addressCond);

            } else if (sopt.equals("TITLE_ADDRESS") || sopt.equals("ALL")) { // 제목 + 내용
                orBuilder.or(titleCond)
                        .or(addressCond);
            }

        }
        andBuilder.and(orBuilder);
        /* 검색 조건 처리 E */

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        int offset = (page - 1) * limit;

        /* 정렬 조건 처리 S */
        String _orderBy = search.getOrderBy();
        OrderSpecifier order = tourPlace.contentId.asc();
        if (StringUtils.hasText(_orderBy)) {
            if (_orderBy.equals(OrderBy.title.name())) {
                order = tourPlace.title.asc();
            } else if (_orderBy.equals(OrderBy.modifiedTime.name())) {
                order = tourPlace.modifiedTime.desc();
            } else if (_orderBy.equals(OrderBy.distance.name())) {
                return listOrderByDistance(search);
            }
        }

        /* 인기순 인기점수로 내림차순 정열 시작 */
        if (StringUtils.hasText(_orderBy)) {
            if (_orderBy.equals(OrderBy.popularity.name())) {
                order = tourPlace.placePointValue.desc();
            } else if (_orderBy.equals(OrderBy.modifiedTime.name())) {
                order = tourPlace.modifiedTime.desc();
            }
        }
        /* 인기순 인기점수로 내림차순 정열 끝 */

        /* 정렬 조건 처리 E */
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        JPAQuery<TourPlace> query = queryFactory.selectFrom(tourPlace)
                .offset(offset)
                .limit(limit)
                .where(andBuilder);

        query.orderBy(order);

        int count = queryFactory.selectFrom(tourPlace)
                .where(andBuilder).fetch().size();

        List<TourPlace> items = query.fetch();

        items.forEach(this::addInfo);
        Pagination pagination = new Pagination(page, count, 0, limit, request);
        return new ListData<>(items, pagination);

    }

    private void addInfo(TourPlace item) {
//        컨텐트 타입 정보
        Long contentTypeId = item.getContentTypeId();
        if (contentTypeId != null) {
            ContentType type = ContentType.getList().stream().filter(c -> c.getId() == contentTypeId.longValue()).findFirst().orElse(null);
            item.setContentType(type);
        }
    }

    private void addDistance(TourPlace item, TourPlaceSearch search) {
        // 거리 정보 주입
        Double latitude = item.getLatitude();
        Double longitude = item.getLongitude();
        Double curLat = search.getLatitude();
        Double curLon = search.getLongitude();

        Double distance = 0.0;
        if (latitude != null && longitude != null && curLat != null && curLon != null) {
            distance = Math.sqrt(Math.pow(latitude - curLat, 2) + Math.pow(longitude - curLon, 2));
        }
        item.setDistance(distance);
    }

    private ListData<TourPlace> listOrderByDistance(TourPlaceSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        int offset = (page - 1) * limit;
        Double curLon = search.getLongitude();
        Double curLat = search.getLatitude();
        Integer radius = search.getRadius();
        String contentType = "";
        if (search.getContentType() != null) {
            contentType = String.valueOf(utils.typeCode(search.getContentType()).getId());
        }
        ApiConfig apiKey = configInfoService.get("apiConfig", ApiConfig.class).orElseGet(ApiConfig::new);
        String url = String.format("https://apis.data.go.kr/B551011/KorService1/locationBasedList1?numOfRows=%d&pageNo=%d&MobileOS=and&MobileApp=test&_type=json&mapX=%f&mapY=%f&contentTypeId=%s&radius=%d&serviceKey=%s", limit, page, curLon, curLat, contentType, radius, apiKey.getPublicOpenApiKey());
        ResponseEntity<ApiResult> response = null;
        try {
            response = restTemplate.getForEntity(URI.create(url), ApiResult.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<ApiItem> items = response.getBody().getResponse().getBody().getItems().getItem();
                List<TourPlace> tourPlaces = new ArrayList<TourPlace>();
                try {
                    items.forEach(item -> {
                        String address = item.getAddr1();
                        address += StringUtils.hasText(item.getAddr2()) ? " " + item.getAddr2() : "";
                        TourPlace tourPlace = TourPlace.builder()
                                .contentId(item.getContentid())
                                .contentTypeId(item.getContenttypeid())
                                .category1(item.getCat1())
                                .category2(item.getCat2())
                                .category3(item.getCat3())
                                .modifiedTime(item.getModifiedtime())
                                .createdTime(item.getCreatedtime())
                                .title(item.getTitle())
                                .tel(item.getTel())
                                .address(address)
                                .areaCode(item.getAreacode())
                                .distance(item.getDist())
                                .bookTour(item.getBooktour().equals("1"))
                                .distance(item.getDist())
                                .firstImage(item.getFirstimage())
                                .firstImage2(item.getFirstimage2())
                                .cpyrhtDivCd(item.getCpyrhtDivCd())
                                .latitude(item.getMapy())
                                .longitude(item.getMapx())
                                .mapLevel(item.getMlevel())
                                .sigunguCode(item.getSigunguCode())
                                .build();
                        tourPlaces.add(tourPlace);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int count = response.getBody().getResponse().getBody().getTotalCount();
                Pagination pagination = new Pagination(page, count, 0, limit, request);
                return new ListData<>(tourPlaces, pagination);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public TourPlace get(Long contentId){
        return tourPlaceRepository.findById(contentId).orElseThrow(TourPlaceNotFoundException::new);
    }
}
