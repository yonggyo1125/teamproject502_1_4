package org.g9project4.visitrecord.entities;


import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class VisitRecordId {


    private Long contentId; // 여행지 등록 번호
    private int uid; //비회원 (ip + User-Agent) 회원(회원번호
    private LocalDate yearMonth;


}
