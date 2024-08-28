package org.g9project4.planner.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.ListData;
import org.g9project4.global.exceptions.UnAuthorizedException;
import org.g9project4.member.MemberUtil;
import org.g9project4.planner.controllers.PlannerSearch;
import org.g9project4.planner.entities.Planner;
import org.g9project4.planner.entities.QPlanner;
import org.g9project4.planner.exceptions.PlannerNotFoundException;
import org.g9project4.planner.repositories.PlannerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerInfoService {
    private final PlannerRepository plannerRepository;
    private final MemberUtil memberUtil;
    /**
     * 플래너 한개 조회
     *
     * @param seq
     * @return
     */
    public Planner get(Long seq) {
        if (!memberUtil.isLogin()) {
            throw new UnAuthorizedException();
        }

        QPlanner planner = QPlanner.planner;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(planner.seq.eq(seq));

        if (!memberUtil.isAdmin()) {
            builder.and(planner.member.seq.eq(memberUtil.getMember().getSeq()));
        }

        Planner item = plannerRepository.findOne(builder).orElseThrow(PlannerNotFoundException::new);

        /**
         *   추가 처리
         *   1. 여행 노트 목록 ...
          */

        addInfo(item);

        return item;
    }

    public ListData<Planner> getList(PlannerSearch search) {
        return null;
    }

    private void addInfo(Planner item) {

    }
}
