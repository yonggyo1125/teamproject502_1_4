package org.g9project4.publicData.tourvisit;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.g9project4.publicData.myvisit.services.TourplaceInterestsPointService;
import org.g9project4.publicData.myvisit.services.TourplacePointMemberService;
import org.g9project4.publicData.tour.controllers.TourPlaceSearch;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
import org.g9project4.publicData.tour.services.ApiUpdateService;
import org.g9project4.publicData.tourvisit.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
//@ActiveProfiles("test")
public class DataTransfer {

    @Autowired
    private TourPlaceRepository tourPlaceRepository;

    @Autowired
    private SidoVisitStatisticService service;

    @Autowired
    private SigunguVisitStatisticService service2;

    @Autowired
    private ApiUpdateService apiUpdateService;

    @Autowired
    private VisitUpdateService visitUpdateService;

    @Autowired
    private SigunguTableStatisticService tableService;


    @Autowired
    private TourplacePointService pointService;


    @PersistenceContext
    private EntityManager em;

    private String sKey = "7rVGv4M2LZhWVFhu97TYGa8Lltf6eOFPG99BKHny11wiv2TWbUle1fP3Foos+QcjBgTlHVDYcoG8RwfuspzfxA==";

    @Test
    void test1() {
        service.updateSidoVisit("1D");
        service.updateSidoVisit("1W");
        service.updateSidoVisit("1M");
        service.updateSidoVisit("3M");
        service.updateSidoVisit("6M");
        service.updateSidoVisit("1Y");
    }

    @Test
    void test2() {
        service2.updateVisit("1D");
        service2.updateVisit("1W");
        service2.updateVisit("1M");
        service2.updateVisit("3M");
        service2.updateVisit("6M");
        service2.updateVisit("1Y");
    }

    @Test // tourplace api 다운로드 km 1000개만
    void test3() {
       apiUpdateService.update(sKey);
    }

    @Test
    void test4() {
        tableService.update();

    }


    @Test
    void test5() {
        visitUpdateService.update();
    }




    @Test // placepointvalue 계산
    void test6() {

        pointService.update();

    }


}



