package org.g9project4.publicData.tourvisit.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.g9project4.publicData.tour.entities.QTourPlace;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
import org.g9project4.publicData.tourvisit.entities.QSigunguVisit;
import org.g9project4.publicData.tourvisit.entities.SigunguVisit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VisitUpdateService {

    private final JPAQueryFactory queryFactory;
    private final TourPlaceRepository tourPlaceRepository;

    @Scheduled(cron = "0 0 5 * * *")
    public void update() {
        QSigunguVisit sigunguVisit = QSigunguVisit.sigunguVisit;
        QTourPlace tourPlace = QTourPlace.tourPlace;

        List<SigunguVisit> items = queryFactory.selectFrom(sigunguVisit)
                .leftJoin(sigunguVisit.sidoVisit)
                .fetchJoin()
                .fetch();

        for (SigunguVisit item : items) {
            if (item.getSidoVisit() == null) continue;

            String sido = item.getSidoVisit().getAreaName();
            String sigungu = item.getSigunguName();

            BooleanBuilder andBuilder = new BooleanBuilder();
            andBuilder.and(tourPlace.address.contains(sido))
                    .and(tourPlace.address.contains(sigungu));

            List<TourPlace> places = (List<TourPlace>)tourPlaceRepository.findAll(andBuilder);
            if (places == null || places.isEmpty()) continue;

            for (TourPlace place : places) {
                place.setType1D1(item.getType1D1());
                place.setType2D1(item.getType2D1());
                place.setType3D1(item.getType3D1());
                place.setType1W1(item.getType1W1());
                place.setType2W1(item.getType2W1());
                place.setType3W1(item.getType3W1());
                place.setType1M1(item.getType1M1());
                place.setType2M1(item.getType2M1());
                place.setType3M1(item.getType3M1());
                place.setType1M3(item.getType1M3());
                place.setType2M3(item.getType2M3());
                place.setType3M3(item.getType3M3());
                place.setType1M6(item.getType1M6());
                place.setType2M6(item.getType2M6());
                place.setType3M6(item.getType3M6());
                place.setType1Y1(item.getType1Y1());
                place.setType2Y1(item.getType2Y1());
                place.setType3Y1(item.getType3Y1());
            }

            tourPlaceRepository.saveAllAndFlush(places);
        }
}}