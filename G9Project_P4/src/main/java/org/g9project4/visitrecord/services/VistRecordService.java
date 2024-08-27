package org.g9project4.visitrecord.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.Utils;
import org.g9project4.member.MemberUtil;
import org.g9project4.visitrecord.entities.VisitRecord;
import org.g9project4.visitrecord.entities.VisitRecordId;
import org.g9project4.visitrecord.repositories.VisitRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VistRecordService {
    private final VisitRecordRepository repository;
    private final MemberUtil memberUtil;
    private final Utils utils;

    public void record(Long contentId) {
        int uid = memberUtil.isLogin() ? memberUtil.getMember().getSeq().intValue() : utils.guestUid();

        LocalDate yearMonth = thisMonth();

        VisitRecordId recordId = new VisitRecordId(contentId, uid, yearMonth);
        VisitRecord record = repository.findById(recordId).orElseGet(VisitRecord::new);

        record.setContentId(contentId);
        record.setUid(uid);
        record.setYearMonth(yearMonth);
        record.setVisitCount(record.getVisitCount() + 1);

        repository.saveAndFlush(record);
    }

    public List<Long> getMonthlyRecommend() {

    }

    public LocalDate thisMonth() {
        LocalDate today = LocalDate.now();
        // 년, 월 기준의 통계
        return LocalDate.of(today.getYear(), today.getMonth(), 1);
    }
}
