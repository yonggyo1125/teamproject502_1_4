package org.g9project4.member.services;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.g9project4.file.services.FileUploadDoneService;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.constants.Authority;
import org.g9project4.member.constants.Gender;
import org.g9project4.member.constants.Interest;
import org.g9project4.member.controllers.RequestJoin;
import org.g9project4.member.entities.Authorities;

import org.g9project4.member.entities.Interests;
import org.g9project4.member.entities.Member;
import org.g9project4.member.exceptions.MemberNotFoundException;
import org.g9project4.member.repositories.AuthoritiesRepository;
import org.g9project4.member.repositories.InterestsRepository;
import org.g9project4.member.repositories.MemberRepository;
import org.g9project4.mypage.controllers.RequestProfile;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberSaveService {
    private final FileUploadDoneService uploadDoneService;
    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberUtil memberUtil;
    private final InterestsRepository interestsRepository;

    /**
     * 회원 가입 처리
     *
     * @param form
     */
    public void save(RequestJoin form) {

        Member member = new ModelMapper().map(form, Member.class);
        String hash = passwordEncoder.encode(member.getPassword());
        member.setPassword(hash);

        save(member, List.of(Authority.USER));

        List<Interest> interests = form.getInterests();
        if (interests != null && !interests.isEmpty()) {
            saveInterests(member, interests);
        }
    }

    /**
     * 회원정보 수정
     *
     * @param form
     */
    public void save(RequestProfile form) {
        // Get the current member
        Member member = memberUtil.getMember();
        //km
        if (member == null) {
            throw new MemberNotFoundException("Member not found in the current session.");
        }

        String email = member.getEmail();
        member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        // Update member details
        String password = form.getPassword();
        String mobile = form.getMobile();
        LocalDate birth = form.getBirth();
        Gender gende = form.getGende();
        Boolean isForeigner = form.getIsForeigner();

        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
        }

        member.setUserName(form.getUserName());
        member.setMobile(mobile);

        if (StringUtils.hasText(password)) {
            String hash = passwordEncoder.encode(password);
            member.setPassword(hash);
        }

        member.setBirth(birth);
        member.setGende(gende);
        member.setIsForeigner(isForeigner);

        // Save the updated member
        save(member, null);

        // Save interests if they are provided
        List<Interest> interests = form.getInterests();
        if (interests != null && !interests.isEmpty()) {
            saveInterests(member, interests);
        }
    }


    public void saveInterests(Member member, List<Interest> interests) {

        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        // Check if the member exists
        if (!memberRepository.existsById(member.getSeq())) {
            throw new IllegalArgumentException("Member does not exist");
        }

        interestsRepository.deleteAllByMember(member);

        // Convert the List<Interest> to List<Interests>
        List<Interests> newInterests = interests.stream()
                .map(interest -> new Interests(member, interest))
                .collect(Collectors.toList());

        interestsRepository.saveAllAndFlush(newInterests);
    }


    public void save(Member member, List<Authority> authorities) {
        //휴대폰 번호 숫자만 기록
        String mobile = member.getMobile();
        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
            member.setMobile(mobile);
        }

        memberRepository.saveAndFlush(member);
        if (authorities != null) {
            List<Authorities> items = authoritiesRepository.findByMember(member);
            authoritiesRepository.deleteAll(items);
            authoritiesRepository.flush();

            items = authorities.stream().map(authority -> Authorities.builder()
                    .member(member)
                    .authority(authority)
                    .build()).toList();
            authoritiesRepository.saveAllAndFlush(items);
        }

        //파일 업로드 완료 처리
        System.out.println("여기???");
        uploadDoneService.process(member.getGid());
    }


}

