package org.g9project4.publicData.tourvisit.repositories;

import org.g9project4.publicData.tourvisit.entities.SigunguVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SigunguVisitRepository extends JpaRepository<SigunguVisit, String>, QuerydslPredicateExecutor<SigunguVisit> {
}
