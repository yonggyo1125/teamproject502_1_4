package org.hidog.member;


import lombok.RequiredArgsConstructor;
import org.hidog.member.constants.Authority;
import org.hidog.member.entities.Authorities;
import org.hidog.member.entities.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberUtil {
   //  private final HttpSession session;
   //  private final MemberInfoService infoService;

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

            /*
            if (session.getAttribute("userInfoChanged") != null) { // 회원 정보를 변경한 경우
                memberInfo = (MemberInfo)infoService.loadUserByUsername(memberInfo.getEmail());
            }
            */
            return memberInfo.getMember();
        }

        return null;
    }
}
