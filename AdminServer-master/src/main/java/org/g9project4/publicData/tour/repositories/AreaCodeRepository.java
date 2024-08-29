package org.g9project4.publicData.tour.repositories;


import org.g9project4.publicData.tour.entities.AreaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AreaCodeRepository extends JpaRepository<AreaCode,String>, QuerydslPredicateExecutor<AreaCode> {
}
