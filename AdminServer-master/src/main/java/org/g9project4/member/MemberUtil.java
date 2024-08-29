package org.g9project4.member;


import lombok.RequiredArgsConstructor;
import org.g9project4.member.constants.Authority;
import org.g9project4.member.entities.Authorities;
import org.g9project4.member.entities.Member;
import org.g9project4.member.repositories.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberUtil {
    private final MemberRepository memberRepository;

    public boolean isLogin() {
        return getMember() != null;
    }

    public boolean isAdmin() {
        if (isLogin()) {
            List<Authorities> authorities = getMember().getAuthorities();
            return authorities.stream().anyMatch(s -> s.getAuthority().equals(Authority.ADMIN));
        }
        return false;
    }

    public Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo memberInfo) {
            return memberRepository.findByEmail(memberInfo.getEmail()).orElse(null);
            //return memberInfo.getMember();
        }

        return null;
    }
}
