package org.g9project4.planner.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.planner.entities.Planner;
import org.g9project4.planner.repositories.PlannerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerInfoService {
    private final PlannerRepository plannerRepository;

    /**
     * 플래너 한개 조회
     *
     * @param seq
     * @return
     */
    public Planner get(Long seq) {
        Planner planner = plannerRepository.findById(seq).orElseThrow(PlannerNotFoundException::new);

        /**
         *   추가 처리
         *   1. 여행 노트 목록 ...
          */

        addInfo(planner);

        return planner;

    }

    private void addInfo(Planner item) {

    }
}
