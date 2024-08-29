package org.g9project4.visitrecord.repositories;

import org.g9project4.member.entities.Member;
import org.g9project4.search.entities.SearchHistory;
import org.g9project4.visitrecord.entities.VisitRecord;
import org.g9project4.visitrecord.entities.VisitRecordId;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;



public interface VisitRecordRepository extends JpaRepository<VisitRecord, VisitRecordId>, QuerydslPredicateExecutor<VisitRecord> {
    // 특정 uid와 contentId에 대한 방문 기록을 조회하는 메서드
    List<VisitRecord> findByUidAndContentId(int uid, Long contentId);

    // 특정 uid에 대한 방문 기록을 조회하는 메서드
    List<VisitRecord> findByUid(int uid);
}