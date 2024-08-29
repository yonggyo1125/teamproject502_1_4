package org.g9project4.publicData.tour.services;

import com.netflix.discovery.converters.Auto;
import org.g9project4.publicData.tour.entities.Category;
import org.g9project4.publicData.tour.entities.SigunguCode;
import org.g9project4.publicData.tour.repositories.AreaCodeRepository;
import org.g9project4.publicData.tour.repositories.CategoryRepository;
import org.g9project4.publicData.tour.repositories.SigunguCodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AreaCodeRepository areaCodeRepository;
    @Autowired
    private SigunguCodeRepository sigunguCodeRepository;

    @Test
    void test1() {
        categoryRepository.findDistinctName1().forEach(c -> {
            String category1 = (String) c[0];
            String name1 = (String) c[1];
            System.out.println("Category1: " + category1 + ", Name1: " + name1);
        });
    }

    @Test
    void test2() {
        areaCodeRepository.findAll().forEach(areaCode -> {
            sigunguCodeRepository.findAllByAreaCode("4").forEach(sigunguCode -> {
                System.out.println("sigunguCode : " + sigunguCode);
                System.out.println("areaCode : " + sigunguCode.getAreaCode());
                System.out.println("sigunguCode : " + sigunguCode.getSigunguCode());
                System.out.println("name : " + sigunguCode.getName());
            });
        });
    }
}
