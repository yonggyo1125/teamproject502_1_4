package org.g9project4.publicData.myvisit.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.constants.Interest;
import org.g9project4.member.entities.Member;
import org.g9project4.member.repositories.InterestsRepository;
import org.g9project4.member.repositories.MemberRepository;
import org.g9project4.member.entities.Interests;
import org.g9project4.publicData.myvisit.TourplaceDto;
import org.g9project4.publicData.tour.controllers.TourPlaceSearch;
import org.g9project4.publicData.tour.entities.QTourPlace;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TourplaceInterestsPointService {

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;
    private final TourPlaceRepository repository;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;
    private final InterestsRepository interestsRepository;

    // 여행 추천 - 관심사 기반 + 베이스 점수 계산
    @Transactional(readOnly = true)
    public ListData<TourplaceDto> getTopTourPlacesByInterests(TourPlaceSearch search) {

        if (!memberUtil.isLogin()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Member member = memberUtil.getMember();
        List<Interest> interests = getInterestsForMember(member);

        QTourPlace qTourPlace = QTourPlace.tourPlace;
        List<TourPlace> tourPlaces = queryFactory.selectFrom(qTourPlace).fetch();

        List<TourplaceDto> items = tourPlaces.stream()
                .filter(tourPlace -> filterByInterests(tourPlace, interests))
                .map(tourPlace -> {
                    int interestPoints = calculateInterestPoints(tourPlace, interests);
                    int placePointValue = tourPlace.getPlacePointValue() != null ? tourPlace.getPlacePointValue() : 0;
                    int finalPointValue = placePointValue + interestPoints;

                    return new TourplaceDto(
                            tourPlace.getContentId(),
                            tourPlace.getTitle(),
                            tourPlace.getAddress(),
                            tourPlace.getFirstImage(),
                            tourPlace.getTel(),
                            finalPointValue
                    );
                })
                .sorted((tp1, tp2) -> Integer.compare(tp2.getFinalPointValue(), tp1.getFinalPointValue()))
                .limit(20)
                .collect(Collectors.toList());

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit() < 1 ? 10 : search.getLimit();
        int offset = (page - 1) * limit;

        Pagination pagination = new Pagination(page, (int) repository.count(), 0, limit, request);

        return new ListData<>(items, pagination);
    }

    private List<Interest> getInterestsForMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }

        Member managedMember = memberRepository.findById(member.getSeq())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return interestsRepository.findByMember(managedMember)
                .stream()
                .map(Interests::getInterest)
                .collect(Collectors.toList());
    }

    private boolean filterByInterests(TourPlace tourPlace, List<Interest> interests) {
        if (interests == null || interests.isEmpty()) {
            return false;
        }

        return (interests.contains(Interest.MATJIB) && tourPlace.getContentTypeId() == 12) ||
                (interests.contains(Interest.HOCANCE) && tourPlace.getTitle().contains("호텔")) ||
                (interests.contains(Interest.MUSEUM) && (tourPlace.getTitle().contains("문학관") ||
                        tourPlace.getTitle().contains("박물관") ||
                        tourPlace.getTitle().contains("테마관") ||
                        tourPlace.getTitle().contains("문화관") ||
                        tourPlace.getTitle().contains("과학관"))) ||
                (interests.contains(Interest.CAMPING) && (tourPlace.getTitle().contains("캠핑장") ||
                        tourPlace.getTitle().contains("야영장") ||
                        tourPlace.getTitle().contains("글램핑") ||
                        tourPlace.getTitle().contains("카라반"))) ||
                (interests.contains(Interest.HIKING) && tourPlace.getTitle().contains("산")) ||
                (interests.contains(Interest.NATURE) && (tourPlace.getTitle().contains("공원") ||
                        tourPlace.getTitle().contains("산") ||
                        tourPlace.getTitle().contains("휴양림") ||
                        tourPlace.getTitle().contains("케이블카") ||
                        tourPlace.getTitle().contains("계곡") ||
                        tourPlace.getTitle().contains("펜션"))) ||
                (interests.contains(Interest.ART) && tourPlace.getTitle().contains("미술관")) ||
                (interests.contains(Interest.SEA) && (tourPlace.getTitle().contains("해수욕장") ||
                        tourPlace.getTitle().contains("강") ||
                        tourPlace.getTitle().contains("계곡") ||
                        tourPlace.getTitle().contains("해변") ||
                        tourPlace.getTitle().contains("선착장"))) ||
                (interests.contains(Interest.WITHCHILD) && (tourPlace.getTitle().contains("물놀이장") ||
                        tourPlace.getTitle().contains("목장") ||
                        tourPlace.getTitle().contains("농장") ||
                        tourPlace.getTitle().contains("체험마을") ||
                        tourPlace.getTitle().contains("테마관") ||
                        tourPlace.getTitle().contains("해변") ||
                        tourPlace.getTitle().contains("캠프") ||
                        tourPlace.getTitle().contains("과학관") ||
                        tourPlace.getTitle().contains("마을") ||
                        tourPlace.getTitle().contains("체험관") ||
                        tourPlace.getTitle().contains("축제") ||
                        tourPlace.getTitle().contains("수영장"))) ||
                (interests.contains(Interest.WITHFAMILY) && (tourPlace.getTitle().contains("리조트") ||
                        tourPlace.getTitle().contains("펜션") ||
                        tourPlace.getTitle().contains("농장") ||
                        tourPlace.getTitle().contains("휴양림"))) ||
                (interests.contains(Interest.WITHLOVER) && (tourPlace.getTitle().contains("카페") ||
                        tourPlace.getTitle().contains("해수욕장") ||
                        tourPlace.getTitle().contains("워터파크") ||
                        tourPlace.getTitle().contains("수상레저") ||
                        tourPlace.getTitle().contains("캠핑장") ||
                        tourPlace.getTitle().contains("글램핑"))) ||
                (interests.contains(Interest.FISHING) && tourPlace.getTitle().contains("낚시"));
    }

    private int calculateInterestPoints(TourPlace tourPlace, List<Interest> interests) {
        int interestCount = 0;

        if (filterByInterests(tourPlace, List.of(Interest.MATJIB))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.HOCANCE))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.MUSEUM))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.CAMPING))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.HIKING))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.NATURE))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.ART))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.SEA))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.WITHCHILD))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.WITHFAMILY))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.WITHLOVER))) interestCount++;
        if (filterByInterests(tourPlace, List.of(Interest.FISHING))) interestCount++;

        return switch (interestCount) {
            case 1 -> 100;
            case 2 -> 200;
            case 3 -> 300;
            default -> 0;
        };
    }
}
