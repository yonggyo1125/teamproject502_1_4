package org.hidog.member.repositories;

import com.querydsl.core.BooleanBuilder;
import org.hidog.member.entities.Member;
import org.hidog.member.entities.QMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.redis.core.RedisHash;

import java.util.Optional;

@RedisHash
public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {


    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findByEmail(String email);

    @EntityGraph(attributePaths = "authorities")
   Optional<Member> findByUserName(String userName);  // 닉네임으로 조회

    default boolean existsByEmail(String email) {
        QMember member = QMember.member;

        return exists(member.email.eq(email));
    }


    default boolean existsByEmailAndName(String email, String name) {
        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(member.email.eq(email))
                .and(member.userName.eq(member.userName));

        return exists(builder);
    }
    boolean existsByUserName(String userName);
}

