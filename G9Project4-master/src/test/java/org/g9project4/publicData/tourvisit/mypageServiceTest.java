//package org.g9project4.publicData.tourvisit;
//
//import org.g9project4.global.ListData;
//import org.g9project4.member.MemberUtil;
//import org.g9project4.member.entities.Member;
//import org.g9project4.member.repositories.MemberRepository;
//import org.g9project4.publicData.tour.controllers.TourPlaceSearch;
//import org.g9project4.publicData.tour.entities.TourPlace;
//import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
//import org.g9project4.publicData.myvisit.services.TourplaceInterestsPointService;
//import org.g9project4.publicData.myvisit.services.TourplaceMRecordPointService;
//import org.g9project4.publicData.myvisit.services.TourplacePointMemberService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@ActiveProfiles("test")
//public class mypageServiceTest {
//
//    @Autowired
//    private TourplacePointMemberService pointMemberService;
//
//    @Autowired
//    private TourplaceMRecordPointService mRecordPointService;
//
//    @Autowired
//    private TourplaceInterestsPointService interestsPointService;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private TourPlaceRepository tourPlaceRepository;
//
//    @Autowired
//    private MemberUtil memberUtil;
//
//    private Member testMember;
//    private LocalDate testDate;
//
//
//    @BeforeEach
//    void setUp() {
//        // Set up a test date
//        testDate = LocalDate.now();
//
//        // Create a test member and save to repository
//        testMember = new Member();
//        testMember.setUserName("testUser");
//        testMember.setBirth(LocalDate.of(1990, 1, 1)); // Example birthdate
//        memberRepository.save(testMember);
//
//        // Create and save a test TourPlace
//        TourPlace testTourPlace = new TourPlace();
//        testTourPlace.setTitle("대치유수지체육공원");
//        testTourPlace.setAddress("서울특별시 강남구 역삼로90길 43 (대치동)");
//        testTourPlace.setPlacePointValue(100);
//        testTourPlace.setContentId(12L);
//        tourPlaceRepository.save(testTourPlace);
//
//    }
//
//    @Test
//    void testService1() {
//        TourPlaceSearch search = new TourPlaceSearch();
//        search.setPage(1);
//        search.setLimit(10);
//
//        ListData<TourPlace> result = pointMemberService.getTopTourPlacesByMember(search, testDate);
//        System.out.println("Test Service 1 Result: " + result);
//
//        assertNotNull(result, "Result should not be null");
//        assertFalse(result.getItems().isEmpty(), "Result list should not be empty");
//    }
//
//    @Test
//    void testService2() {
//        TourPlaceSearch search = new TourPlaceSearch();
//        search.setPage(1);
//        search.setLimit(10);
//
//        ListData<TourPlace> result = mRecordPointService.getTopTourPlacesByRecord(search, testDate);
//        System.out.println("Test Service 2 Result: " + result);
//
//        assertNotNull(result, "Result should not be null");
//        assertFalse(result.getItems().isEmpty(), "Result list should not be empty");
//    }
//
//
//    @Test
//    void testService3() {
//        // Create an instance of TourPlaceSearch
//        TourPlaceSearch search = new TourPlaceSearch();
//        // Set appropriate values if needed
//        search.setPage(1); // Example page number
//        search.setLimit(10); // Example limit
//
//        // Fetch the userName from memberUtil
//        Long seq = testMember.getSeq();
//        assertNotNull(seq, "UserName should not be null");
//
//        // Call the method under test
//        ListData<TourPlace> result = interestsPointService.getTopTourPlacesByInterests(seq, search);
//
//        // Verify the result
//        assertNotNull(result, "The result should not be null");
//        assertTrue(result.getItems().size() <= 20, "The result list size should be at most 20");
//
//        // Additional assertions based on expected behavior
//        // Example: Check if pagination is correct
//        assertEquals(search.getPage(), result.getPagination().getPage(), "Page should match");
//        assertEquals(search.getLimit(), result.getPagination().getLimit(), "Limit should match");
//    }
//
//
//
//}
