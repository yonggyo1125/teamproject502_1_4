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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static org.springframework.data.domain.Sort.Order.desc;

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

        if (!memberUtil.isLogin()) {
            return new ListData<>();
        }

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;

        /* 검색 처리 S */

        BooleanBuilder andBuilder = new BooleanBuilder();
        QPlanner planner = QPlanner.planner;
        if (!memberUtil.isAdmin()) {
            andBuilder.and(planner.member.seq.eq(memberUtil.getMember().getSeq()));
        }

        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";
        if (skey != null && !skey.isBlank()) {
            skey = skey.trim();
            if (sopt.equals("ALL")) {
                andBuilder.and(planner.title.contains(skey));
            }
        }
        /* 검색 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Planner> data = plannerRepository.findAll(andBuilder, pageable);


        return null;
    }

    private void addInfo(Planner item) {

    }
}
