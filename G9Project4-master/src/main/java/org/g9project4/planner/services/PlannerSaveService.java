package org.g9project4.planner.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.entities.Member;
import org.g9project4.member.exceptions.MemberNotFoundException;
import org.g9project4.member.repositories.MemberRepository;
import org.g9project4.planner.controllers.RequestPlanner;
import org.g9project4.planner.entities.Planner;
import org.g9project4.planner.exceptions.PlannerNotFoundException;
import org.g9project4.planner.repositories.PlannerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerSaveService {
    private final PlannerRepository repository;
    private final MemberRepository memberRepository;
    private final MemberUtil memberUtil;

    public Planner save(RequestPlanner form) {
        String mode = form.getMode();
        Long seq = form.getSeq();
        Planner planner = null;
        if (mode.equals("update")) {//수정
            planner = repository.findById(seq).orElseThrow(PlannerNotFoundException::new);

        } else {//추가
            Member member = memberUtil.isLogin() ? memberRepository.findByEmail(memberUtil.getMember().getEmail()).orElse(null) :
                    null;
            planner = Planner.builder()
                    .member(member)
                    .build();
        }
        planner.setTitle(form.getTitle());
        planner.setSDate(form.getSDate());
        planner.setEDate(form.getEDate());
        planner.setItinerary(form.getItinerary());

        repository.saveAndFlush(planner);
        return planner;
    }
}
