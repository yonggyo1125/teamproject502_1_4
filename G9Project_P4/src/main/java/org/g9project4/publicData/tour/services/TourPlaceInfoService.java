package org.g9project4.publicData.tour.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.g9project4.publicData.tour.constants.ContentType;
import org.g9project4.publicData.tour.controllers.TourPlaceSearch;
import org.g9project4.publicData.tour.entities.QTourPlace;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
import org.g9project4.visitrecord.constants.RecommendType;
import org.g9project4.visitrecord.services.VistRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourPlaceInfoService {
    @PersistenceContext
    private EntityManager em;

    private final RestTemplate restTemplate;
    private final TourPlaceRepository repository;
    private String serviceKey = "n5fRXDesflWpLyBNdcngUqy1VluCJc1uhJ0dNo4sNZJ3lkkaYkkzSSY9SMoZbZmY7/O8PURKNOFmsHrqUp2glA==";
    private final HttpServletRequest request;
    private final VistRecordService recordService;

    /**
     * 좌표, 거리 기반으로 검색
     *
     * @param search
     * @return
     */
    /*
    public ListData<TourPlace> getLocBasedList(TourPlaceSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 10 : limit;
        int offset = (page - 1) * limit;

        double lat = search.getLatitude();
        double lon = search.getLongitude();
        int radius = search.getRadius();
        String url = String.format("https://apis.data.go.kr/B551011/KorService1/locationBasedList1?MobileOS=AND&MobileApp=test&mapX=%f&mapY=%f&radius=%d&numOfRows=5000&serviceKey=%s&_type=json", lon, lat, radius, serviceKey);
        try {
            ResponseEntity<ApiResult> response = restTemplate.getForEntity(URI.create(url), ApiResult.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody().getResponse().getHeader().getResultCode().equals("0000")) {

                List<Long> ids = response.getBody().getResponse().getBody().getItems().getItem().stream().map(ApiItem::getContentid).toList();
                if (!ids.isEmpty()) {
                    QTourPlace tourPlace = QTourPlace.tourPlace;
                    List<TourPlace> items = (List<TourPlace>) repository.findAll(tourPlace.contentId.in(ids), Sort.by(asc("contentId")));
                    int count = items.size();
                    Pagination pagination = new Pagination(page, count, 0, limit, request);
                    return new ListData<>(items, pagination);
                } // endif
            } // endif
        } catch (Exception e) {
            e.printStackTrace();
            throw new TourPlaceNotFoundException();
        }

        return null;
    }
    */

    public ListData<TourPlace> getTotalList(TourPlaceSearch search) {

        return getTotalList(search, null);
    }

    public ListData<TourPlace> getTotalList(TourPlaceSearch search, RecommendType recommendType) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 10 : limit;
        int offset = (page - 1) * limit;

        QTourPlace tourPlace = QTourPlace.tourPlace;
        BooleanBuilder andBuilder = new BooleanBuilder();

        // 추천 검색 S
        if (recommendType != null) {
            if (recommendType == RecommendType.VIEW) {
                List<Long> contentIds = recordService.getMonthlyRecommend();
                andBuilder.and(tourPlace.contentId.in(contentIds));
            } else if (recommendType == RecommendType.KEYWORD) {

            }
        }
        // 추천 검색 E

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<TourPlace> items = queryFactory.selectFrom(tourPlace)
                .orderBy(tourPlace.contentId.asc())
                .offset(offset)
                .limit(limit)
                .fetch();

        Pagination pagination = new Pagination(page, (int) repository.count(andBuilder), 0, limit, request);

        return new ListData<>(items, pagination);
    }
    /*
    public ListData<GreenPlace> getGreenList(TourPlaceSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int offset = page * limit + 1;
        /* 검색 조건 처리 S */
        /*
        QGreenPlace greenPlace = QGreenPlace.greenPlace;
        BooleanBuilder andBuilder = new BooleanBuilder();

        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL";

        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            BooleanExpression titleCond = greenPlace.title.contains(skey); // 제목 - subject LIKE '%skey%';
            BooleanExpression addressCond = greenPlace.address.contains(skey); // 내용 - content LIKE '%skey%';

            if (sopt.equals("TITLE")) { // 여행지 이름

                andBuilder.and(titleCond);

            } else if (sopt.equals("ADDRESS")) { // 주소

                andBuilder.and(addressCond);

            } else if (sopt.equals("TITLE_ADDRESS")) { // 제목 + 내용

                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(titleCond)
                        .or(addressCond);

                andBuilder.and(orBuilder);

            }

        }

        /* 검색 조건 처리 E */
    /*
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        int count = queryFactory.selectFrom(greenPlace)
                .where(andBuilder).fetch().size();
        JPAQuery<GreenPlace> query = queryFactory.selectFrom(greenPlace)
                .orderBy(greenPlace.contentId.asc())
                .offset(offset)
                .limit(limit)
                .where(andBuilder);
        List<GreenPlace> items = query.fetch();
        Pagination pagination = new Pagination(page, count, 0, limit, request);
        return new ListData<>(items, pagination);
    }
    */

    public ListData<TourPlace> getSearchedList(TourPlaceSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        int offset = (page - 1) * limit;

        /* 검색 조건 처리 S */

        QTourPlace tourPlace = QTourPlace.tourPlace;
        BooleanBuilder andBuilder = new BooleanBuilder();
       // if (search.getContentType() != null) {
        //     andBuilder.and(tourPlace.contentTypeId.eq(search.getContentType().getId()));
        //}
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
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(titleCond)
                        .or(addressCond);
                andBuilder.and(orBuilder);
            }

        }

        /* 검색 조건 처리 E */

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        int count = queryFactory.selectFrom(tourPlace)
                .where(andBuilder).fetch().size();
        JPAQuery<TourPlace> query = queryFactory.selectFrom(tourPlace)
                .orderBy(tourPlace.contentId.asc())
                .offset(offset)
                .limit(limit)
                .where(andBuilder);
        List<TourPlace> items = query.fetch();

        items.forEach(this::addInfo);

        Pagination pagination = new Pagination(page, count, 0, limit, request);
        return new ListData<>(items, pagination);
    }

    private void addInfo(TourPlace item) {
        Long contentTypeId = item.getContentTypeId();
        if (contentTypeId != null) {
            ContentType type = ContentType.getList().stream().filter(c -> c.getId() == contentTypeId.longValue()).findFirst().orElse(null);
            item.setContentType(type);
        }
    }
}
