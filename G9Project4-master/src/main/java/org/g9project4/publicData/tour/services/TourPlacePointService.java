package org.g9project4.publicData.tour.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.CommonSearch;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.entities.Member;
import org.g9project4.publicData.tour.entities.QTourPlacePoint;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.entities.TourPlacePoint;
import org.g9project4.publicData.tour.repositories.TourPlacePointRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class TourPlacePointService {
    private final MemberUtil memberUtil;
    private final TourPlacePointRepository repository;
    private final HttpServletRequest request;
    /**
     *
     * @param mode - visit : 방문 추천, age : 연령 추천, interest: 취향 추천
     * @param search
     * @return
     */
    public ListData<TourPlace> getList(String mode, CommonSearch search) {
        if (!memberUtil.isLogin()) {
            return new ListData<>();
        }

        mode = StringUtils.hasText(mode) ? mode : "visit";

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;

        /* 검색 처리 S */
        String sopt = search.getSopt();
        String skey = search.getSkey();

        Member member = memberUtil.getMember();

        QTourPlacePoint tourPlacePoint = QTourPlacePoint.tourPlacePoint;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(tourPlacePoint.member.seq.eq(member.getSeq())); // 현재 로그인한 회원으로 검색 한정
        /* 검색 처리 E */

        Sort sort = Sort.by(desc("finalPointValueVisit")); // 방문 추천이 기본
        if (mode.equals("age")) { // 연령 추천
            sort = Sort.by(desc("finalPointValueAge")); // 연령별 추천
        } else if (mode.equals("interest")) { // 취향별 추천
            sort = Sort.by(desc("finalPointValueInterest"));
        }


        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<TourPlacePoint> data = repository.findAll(andBuilder, pageable);
        List<TourPlace> items = data.getContent().stream().map(d -> d.getTourPlace()).toList();

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);


        return new ListData<>(items, pagination);
    }

     public void updatePoint() {
        Member member = memberUtil.getMember();
     }
}
