package org.g9project4.member.repositories;


import org.g9project4.member.entities.Member;
import org.g9project4.member.entities.QMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {
    @EntityGraph(attributePaths = "authorities")//처음부터 조인(같이 로딩)
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserName(String userName);
    Optional<Member> findBySeq(Long seq);
    default boolean exists(String email) {
        QMember member = QMember.member;

        return exists(member.email.eq(email));
    }

    List<Member> findAll();
}
