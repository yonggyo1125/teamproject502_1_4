package org.g9project4.visitrecord.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor @NoArgsConstructor
@IdClass(VisitRecordId.class)
public class VisitRecord {

    @Id
    private Long contentId; // 여행지 등록 번호
    @Id
    @Column(name = "_uid")
    private int uid; // 비회원(IP + User-Agent), 회원(회원번호)
    @Id
    private LocalDate yearMonth;

    private Long visitCount; // 방문횟수



}
