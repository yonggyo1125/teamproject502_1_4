package org.g9project4.member.repositories;

import org.g9project4.member.entities.Interests;
import org.g9project4.member.entities.InterestsId;
import org.g9project4.member.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface InterestsRepository extends JpaRepository<Interests, InterestsId> {
    List<Interests> findByMember(Member member);
    void deleteAllByMember(Member member);
}
