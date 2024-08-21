package org.hidog.member.repositories;


import org.hidog.member.entities.Authorities;
import org.hidog.member.entities.AuthoritiesId;
import org.hidog.member.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities, AuthoritiesId>, QuerydslPredicateExecutor<Authorities> {
    List<Authorities> findByMember(Member member);
}
