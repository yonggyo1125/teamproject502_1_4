package org.g9project4.planner.repositories;

import org.g9project4.planner.entities.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PlannerRepository extends JpaRepository<Planner, Long>, QuerydslPredicateExecutor<Planner> {
}
