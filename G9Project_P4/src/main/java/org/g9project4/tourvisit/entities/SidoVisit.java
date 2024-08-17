package org.g9project4.tourvisit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.global.entities.BaseEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SidoVisit extends BaseEntity {
    @Id
    @Column(length=5)
    private String areaCode;

    @Column(length=50)
    private String areaName;

    private Double type1D1; // 현지인(a) 일별 통계
    private Double type2D1; // 외지인(b) 일별 통계
    private Double type3D1; // 외국인(c) 일별 통계

    private Double type1W1; // 현지인(a) 1주 통계
    private Double type2W1; // 외지인(b) 1주 통계
    private Double type3W1; // 외국인(c) 1주 통계

    private Double type1M1; // 현지인(a) 1달 통계
    private Double type2M1; // 외지인(b) 1달 통계
    private Double type3M1; // 외국인(c) 1달 통계

    private Double type1M3; // 현지인(a) 3달 통계
    private Double type2M3; // 외지인(b) 3달 통계
    private Double type3M3; // 외국인(c) 3달 통계

    private Double type1M6; // 현지인(a) 6달 통계
    private Double type2M6; // 외지인(b) 6달 통계
    private Double type3M6; // 외국인(c) 6달 통계

    private Double type1Y1; // 현지인(a) 1년 통계
    private Double type2Y1; // 외지인(b) 1년 통계
    private Double type3Y1; // 외국인(c) 1년 통계
}
