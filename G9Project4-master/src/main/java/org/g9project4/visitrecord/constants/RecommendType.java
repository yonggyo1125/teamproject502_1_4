package org.g9project4.visitrecord.constants;

public enum RecommendType {
    VIEW, //여행지 상세 보기
    KEYWORD; // 키워드 추천

    public static RecommendType fromString(String value) {
        try {
            return RecommendType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            // 기본 값이나 예외 처리를 수행할 수 있습니다.
            return null; // 또는 예외를 던질 수 있습니다.
        }
    }


}
