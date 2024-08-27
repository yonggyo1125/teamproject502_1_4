package org.g9project4.visitrecord.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class VisitRecordId {
    private Long contentId; // 여행지 등록 번호
    private int uid; // 비회원(IP + User-Agent), 회원(회원번호)
    private LocalDate yearMonth;
}
