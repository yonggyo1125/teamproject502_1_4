package org.g9project4.member.controllers;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.g9project4.global.validators.ValidBirthDate;
import org.g9project4.member.constants.Gender;
import org.g9project4.member.constants.Interest;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class RequestJoin implements Serializable {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank
    private String userName;
    @NotBlank
    private String mobile;
    @AssertTrue
    private boolean agree;

    private String gid = UUID.randomUUID().toString();
    @NotNull
    @Past // 출생일은 과거 날짜여야 함
    @ValidBirthDate(minAge = 14, message = "회원가입은 14세 이상이어야 합니다.")
    private LocalDate birth;  // 출생일
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gende;  // 성별 (MALE, FEMALE)
    @NotNull
    private Boolean isForeigner;  // 외국인 여부 (외국인 true, 내국인 false)

    @NotNull
    @Enumerated(EnumType.STRING)
    private List<Interest> interests; // 관심사 (맛집 | 호캉스 | 박물관 | 캠핑 | 등산 | 자연 | 예술 | 강/바다 | 아이와 함께 | 온가족 함께 | 연인과 함께 | 낚시)
    //MATJIB, HOCANCE, MUSEUM, CAMPING, HIKING, NATURE, ART, SEA, WITHCHILD, WITHFAMILY, WITHLOVER, FISHING

}
