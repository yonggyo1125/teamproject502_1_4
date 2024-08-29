package org.g9project4.global.rests.gov.detailpetapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailPetItem {
    private String acmpyPsblCpam;//동반 가능 동물
    private String relaRntlPrdlst;//관련 렌탈 품목
    private String acmpyNeedMtr;//동반시 필요사항
    private String relaFrnshPrdlst;// 관련 비치 품목
    private String etcAcmpyInfo;// 기타 동반 정보
    private String relaPurcPrdlst;//관련_구매_품목
    private String relaAcdntRiskMtr;// 관련_사고_대비사항
    private String acmpyTypeCd;// 동반_유형_코드
    private String relaPosesFclty;//관련_구비_시설
    private String contentid;//콘텐트 ID
    private String petTursmInfo;//반려견_관광정보
}
