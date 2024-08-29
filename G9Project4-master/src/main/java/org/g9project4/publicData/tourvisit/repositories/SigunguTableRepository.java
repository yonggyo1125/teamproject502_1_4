package org.g9project4.publicData.tourvisit.repositories;

import org.g9project4.publicData.tourvisit.entities.SigunguTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SigunguTableRepository  extends JpaRepository<SigunguTable, String>, QuerydslPredicateExecutor<SigunguTable> {
}
