package org.g9project4.member;


import lombok.RequiredArgsConstructor;
import org.g9project4.member.constants.Authority;
import org.g9project4.member.entities.Authorities;
import org.g9project4.member.entities.Member;
import org.g9project4.member.repositories.MemberRepository;
import org.g9project4.member.services.MemberInfoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final MemberRepository repository;
    private final MemberInfoService infoService;

    public boolean isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo;
    }

    public boolean isAdmin() {
        if (isLogin()) {
            List<Authorities> authorities = getMember().getAuthorities();
            return authorities.stream().anyMatch(s -> s.getAuthority().equals(Authority.ADMIN));
        }
        return false;
    }


    //km  오류로 인하여 수정
     public Member getMember() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof MemberInfo memberInfo)) {
             return null;
         }

         Member member = memberInfo.getMember();
         if (member == null) {
             member = repository.findByEmail(memberInfo.getEmail()).orElse(null);
             if (member != null) {
                 infoService.addMemberInfo(member);
                 memberInfo.setMember(member);
             }
         }

         return member;
     }

    // 원래 코드
//    public Member getMember() {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        Member member = null;
//        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo memberInfo) {
//
//
//            member = memberInfo.getMember();
//            if (member == null) {
//                member = repository.findByEmail(memberInfo.getEmail()).orElse(null);
//                infoService.addMemberInfo(member);
//
//                memberInfo.setMember(member);
//            }
//        }
//
//        return member;
//    }


    public List<Member> getAllMembers() {
        return repository.findAll();
    }

}