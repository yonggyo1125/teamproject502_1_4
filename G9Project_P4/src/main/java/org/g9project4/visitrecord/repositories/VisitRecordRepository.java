package org.g9project4.visitrecord.repositories;

import org.g9project4.visitrecord.entities.VisitRecord;
import org.g9project4.visitrecord.entities.VisitRecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VisitRecordRepository extends JpaRepository<VisitRecord, VisitRecordId>, QuerydslPredicateExecutor<VisitRecord> {
}
