package org.g9project4.publicData.tourvisit.entities;

import jakarta.persistence.*;
import lombok.*;
import org.g9project4.global.entities.BaseEntity;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SigunguVisit extends BaseEntity {
//Locgovisit

    @Id
    @Column(length=10)
    private String sigunguCode;

    @Column(length=50)
    private String sigunguName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="areaCode")
    private SidoVisit sidoVisit;

    private Double type1D1; // 현지인 일별통계 현지인관광객구분명
    private Double type2D1; // 외지인 일별통계
    private Double type3D1; // 외국인 일별통계

    private Double type1W1; // 현지인 일별통계 현지인관광객구분명
    private Double type2W1; // 외지인 일별통계
    private Double type3W1; //외국인 일별통계

    private Double type1M1;  // 현지인 일별통계 현지인관광객구분명
    private Double type2M1; // 외지인 일별통계
    private Double type3M1; //외국인 일별통계

    private Double type1M3;  // 현지인 일별통계 현지인관광객구분명
    private Double type2M3; // 외지인 일별통계
    private Double type3M3; //외국인 일별통계

    private Double type1M6;  // 현지인 일별통계 현지인관광객구분명
    private Double type2M6; // 외지인 일별통계
    private Double type3M6; //외국인 일별통계

    private Double type1Y1;  // 현지인 일별통계 현지인관광객구분명
    private Double type2Y1; // 외지인 일별통계
    private Double type3Y1; //외국인 일별통계


}
