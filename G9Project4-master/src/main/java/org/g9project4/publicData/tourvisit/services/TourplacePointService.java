package org.g9project4.publicData.tourvisit.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.g9project4.publicData.tour.entities.QTourPlace;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class TourplacePointService {
    private final JPAQueryFactory queryFactory;
    private final TourPlaceRepository tourPlaceRepository;

    // 여행추천-베이스점수계산( api 기간별집계데이터+현재계절가중치)
    @Scheduled(cron = "0 20 5 * * *") // 매일 5:20 AM에 실행
    public void update() {
        QTourPlace tourPlace = QTourPlace.tourPlace;

        // 모든 TourPlace 항목을 가져옵니다.
        List<TourPlace> items = queryFactory.selectFrom(tourPlace)
                .fetch();

//        /* 10개만 테스트 시작  */
//        // 모든 TourPlace 항목을 가져옵니다.
//        List<TourPlace> items = queryFactory.selectFrom(tourPlace)
//                .limit(10) // 처음 10개 항목만 가져옵니다.
//                .fetch();
//        /* 10개만 테스트 끝 */


        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();

        for (TourPlace item : items) {
            // 기존의 placePointValue를 0으로 초기화합니다.
            item.setPlacePointValue(0);

            // 18가지 필드의 값을 가져옵니다.
            Double type1D1 = item.getType1D1();
            Double type2D1 = item.getType2D1();
            Double type3D1 = item.getType3D1();
            Double type1W1 = item.getType1W1();
            Double type2W1 = item.getType2W1();
            Double type3W1 = item.getType3W1();
            Double type1M1 = item.getType1M1();
            Double type2M1 = item.getType2M1();
            Double type3M1 = item.getType3M1();
            Double type1M3 = item.getType1M3();
            Double type2M3 = item.getType2M3();
            Double type3M3 = item.getType3M3();
            Double type1M6 = item.getType1M6();
            Double type2M6 = item.getType2M6();
            Double type3M6 = item.getType3M6();
            Double type1Y1 = item.getType1Y1();
            Double type2Y1 = item.getType2Y1();
            Double type3Y1 = item.getType3Y1();

            // 18가지 필드의 값을 합산합니다. null 값은 0.0으로 처리합니다.
            Double totalValue = (type1D1 != null ? type1D1 : 0.0)
                    + (type2D1 != null ? type2D1 : 0.0)
                    + (type3D1 != null ? type3D1 : 0.0)
                    + (type1W1 != null ? type1W1 : 0.0)
                    + (type2W1 != null ? type2W1 : 0.0)
                    + (type3W1 != null ? type3W1 : 0.0)
                    + (type1M1 != null ? type1M1 : 0.0)
                    + (type2M1 != null ? type2M1 : 0.0)
                    + (type3M1 != null ? type3M1 : 0.0)
                    + (type1M3 != null ? type1M3 : 0.0)
                    + (type2M3 != null ? type2M3 : 0.0)
                    + (type3M3 != null ? type3M3 : 0.0)
                    + (type1M6 != null ? type1M6 : 0.0)
                    + (type2M6 != null ? type2M6 : 0.0)
                    + (type3M6 != null ? type3M6 : 0.0)
                    + (type1Y1 != null ? type1Y1 : 0.0)
                    + (type2Y1 != null ? type2Y1 : 0.0)
                    + (type3Y1 != null ? type3Y1 : 0.0);

            // totalValue를 396000.0으로 나누고 소수점 반올림하여 정수로 변환
            Integer calculatedPlacePointValue = (int) Math.round(totalValue / 396000.0);

            // 계절별 가중치 적용
            String title = item.getTitle();

            if (currentMonth == 6 || currentMonth == 7 || currentMonth == 8) { // 여름
                if (title.contains("해수욕장") || title.contains("수상레저") || title.contains("휴양림") || title.contains("캠핑장")) {
                    calculatedPlacePointValue += 500;
                } else if (title.contains("스키장")) {
                    calculatedPlacePointValue -= 100;
                }
            } else if (currentMonth == 12 || currentMonth == 1 || currentMonth == 2) { // 겨울
                if (title.contains("스키장")) {
                    calculatedPlacePointValue += 500;
                } else if (title.contains("해수욕장") || title.contains("수상레저")) {
                    calculatedPlacePointValue -= 100;
                }
            } else if ((currentMonth >= 3 && currentMonth <= 5) || (currentMonth >= 9 && currentMonth <= 11)) { // 봄, 가을
                if (title.contains("스키장") || title.contains("해수욕장") || title.contains("수상레저")) {
                    calculatedPlacePointValue -= 300;
                }
            }

            // 디버깅을 위한 로깅
            System.out.println("Title: " + title);
            System.out.println("Total Value: " + totalValue);
            System.out.println("Calculated Place Point Value Before Adjustment: " + calculatedPlacePointValue);

            // 점수 범위 검토
            if (calculatedPlacePointValue < 0) {
                calculatedPlacePointValue = 0; // 최소값을 0으로 설정
            }

            // placePointValue를 업데이트
            item.setPlacePointValue(calculatedPlacePointValue);

            // 변경된 엔티티를 저장합니다.
            tourPlaceRepository.saveAndFlush(item);
        }
    }
}
