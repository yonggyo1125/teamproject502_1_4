package org.g9project4.planner.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.global.entities.BaseEntity;
import org.g9project4.member.entities.Member;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Planner extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(length=100, nullable = false)
    private String title; // 플래너명

    @Column(nullable = false)
    private LocalDate sDate; // 여행 시작일

    @Column(nullable = false)
    private LocalDate eDate; // 여행 종료일

    @Lob
    @Column(nullable = false)
    private String itinerary; // 여행일정
}
