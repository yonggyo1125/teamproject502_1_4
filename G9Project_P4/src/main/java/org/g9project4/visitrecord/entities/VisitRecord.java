package org.g9project4.visitrecord.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor @AllArgsConstructor
public class VisitRecord {
    @Id
    private Long contentId; // 여행지 등록 번호

    @Id
    private int uid; // 비회원(IP + User-Agent), 회원(회원번호)

    @Id
    private LocalDate yearMonth;
    private long visitCount; // 방문 횟수
}
