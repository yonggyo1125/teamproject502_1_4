package org.g9project4.publicData.tour.repositories;

import org.g9project4.publicData.tour.entities.SigunguCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface SigunguCodeRepository extends JpaRepository<SigunguCode, Long>, QuerydslPredicateExecutor<SigunguCode> {
     List<SigunguCode> findAllByAreaCode(String areaCode);
}
