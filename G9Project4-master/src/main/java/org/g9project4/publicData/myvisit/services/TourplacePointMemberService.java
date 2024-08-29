package org.g9project4.publicData.myvisit.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.entities.Member;
import org.g9project4.member.repositories.MemberRepository;
import org.g9project4.publicData.tour.controllers.TourPlaceSearch;
import org.g9project4.publicData.tour.entities.QTourPlace;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.myvisit.TourplaceDto;
import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@RequiredArgsConstructor
public class TourplacePointMemberService {

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;
    private final TourPlaceRepository repository;
    private final EntityManager em;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;


    @Transactional(readOnly = true)
    public ListData<TourplaceDto> getTopTourPlacesByMember(TourPlaceSearch search) {
        if (!memberUtil.isLogin()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        Member member = memberUtil.getMember();
        LocalDate birth = member.getBirth();

        LocalDate currentDate = LocalDate.now();
        int age = calculateAge(birth, currentDate);
        String currentSeason = getCurrentSeason(currentDate);
        QTourPlace qTourPlace = QTourPlace.tourPlace;

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 10 : limit;
        int offset = (page - 1) * limit;

        // 모든 TourPlace 항목을 가져오고, 각 TourPlace에 대해 최종 점수를 계산하여 리스트를 생성합니다.
        List<TourplaceDto> items = queryFactory.selectFrom(qTourPlace)
                .fetch()
                .stream()
                .map(tourPlace -> {
                    // 각 Member별로 mRecordPoint를 계산
                    int mRecordPoint = calculateMRecordPoint(tourPlace, member, age, currentSeason);
                    // getPlacePointValue()가 null일 경우 0으로 대체
                    int placePointValue = tourPlace.getPlacePointValue() != null ? tourPlace.getPlacePointValue() : 0;

                    // 최종 점수 계산 (placePointValue + mRecordPoint)
                    int finalPointValue = placePointValue + mRecordPoint;

                    // DTO로 변환
                    return new TourplaceDto(
                            tourPlace.getContentId(),
                            tourPlace.getTitle(),
                            tourPlace.getAddress(),
                            tourPlace.getFirstImage(),
                            tourPlace.getTel(),
                            finalPointValue);
                })
                .sorted((tp1, tp2) -> Integer.compare(tp2.getFinalPointValue(), tp1.getFinalPointValue())) // 내림차순 정렬
                .skip(offset) // 페이징을 위해 offset 적용
                .limit(limit) // 페이징을 위해 limit 적용
                .collect(Collectors.toList());

        Pagination pagination = new Pagination(page, (int) repository.count(), 0, limit, request);

        return new ListData<>(items, pagination);
    }

    private LocalDate getBirthForMember(Member member) {
        // Member 객체에서 생일 정보를 가져옵니다.
        return member.getBirth();
    }

    private int calculateAge(LocalDate birth, LocalDate currentDate) {
        // 현재 날짜 기준으로 나이를 계산합니다.
        return Period.between(birth, currentDate).getYears();
    }

    private int calculateMRecordPoint(TourPlace tourPlace, Member loggedMember, int age, String currentSeason) {
        int mRecordPoint = 0;

        // 연령대 및 현재 계절 기준으로 추가 점수 계산
        if (age >= 10 && age < 30) {
            if (currentSeason.equals("summer") && tourPlace.getTitle().matches(".*(해수욕장|수상레저).*")) {
                mRecordPoint += 200;
            } else if (currentSeason.equals("winter") && tourPlace.getTitle().matches(".*(스키장).*")) {
                mRecordPoint += 200;
            } else if (currentSeason.equals("spring") || currentSeason.equals("fall")) {
                if (tourPlace.getTitle().matches(".*(산|공원).*")) {
                    mRecordPoint += 200;
                }
            }
        } else if (age >= 30 && age < 50) {
            if (currentSeason.equals("summer") && tourPlace.getTitle().matches(".*(박물관|글램핑|공원).*")) {
                mRecordPoint += 200;
            } else if (currentSeason.equals("winter") && tourPlace.getTitle().matches(".*(스키장|캠핑장|공원).*")) {
                mRecordPoint += 200;
            } else if (currentSeason.equals("spring") || currentSeason.equals("fall")) {
                if (tourPlace.getTitle().matches(".*(산|공원|박물관).*")) {
                    mRecordPoint += 200;
                }
            }
        } else if (age >= 50) {
            if (currentSeason.equals("summer") && tourPlace.getTitle().matches(".*(휴양림|캠핑장).*")) {
                mRecordPoint += 200;
            } else if (currentSeason.equals("winter") && tourPlace.getTitle().matches(".*(휴양림|스파).*")) {
                mRecordPoint += 200;
            } else if (currentSeason.equals("spring") || currentSeason.equals("fall")) {
                if (tourPlace.getTitle().matches(".*(산|공원|스파).*")) {
                    mRecordPoint += 200;
                }
            }
        }

        return mRecordPoint;
    }

    private String getCurrentSeason(LocalDate currentDate) {
        int month = currentDate.getMonthValue();
        if (month >= 3 && month <= 5) {
            return "spring";
        } else if (month >= 6 && month <= 8) {
            return "summer";
        } else if (month >= 9 && month <= 11) {
            return "fall";
        } else {
            return "winter";
        }
    }
}
