package org.hidog.member.services;

import lombok.RequiredArgsConstructor;
import org.hidog.member.entities.Member;
import org.hidog.member.repositories.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean existsByUserName(String userName) {
        return memberRepository.existsByUserName(userName);
    }

    public void updateMember(Member member) {
        memberRepository.save(member);
    }
}