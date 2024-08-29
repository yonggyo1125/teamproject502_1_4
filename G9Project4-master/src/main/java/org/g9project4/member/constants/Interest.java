package org.g9project4.member.constants;

import lombok.Data;
import lombok.Getter;
import org.g9project4.member.entities.Interests;

import java.util.List;

@Getter
public enum Interest {

    //맛집 | 호캉스 | 박물관 | 캠핑 | 등산 | 자연 | 예술 | 강/바다 | 아이와 함께 | 온가족 함께 | 연인과 함께 | 낚시
    MATJIB("맛집"),
    HOCANCE("호캉스"),
    MUSEUM("박물관"),
    CAMPING("캠핑"),
    HIKING("등산"),
    NATURE("자연"),
    ART("예술"),
    SEA("강/바다"),
    WITHCHILD("아이와 함께"),
    WITHFAMILY("온가족 함께"),
    WITHLOVER("연인과 함께"),
    FISHING("낚시");
    private final String type;

    Interest(String type) {
        this.type = type;
    }

    public static List<Interest> getInterests() {
        return List.of(MATJIB, HOCANCE, MUSEUM, CAMPING, HIKING, NATURE, ART, SEA, WITHCHILD, WITHFAMILY, WITHLOVER, FISHING);
    }
}
