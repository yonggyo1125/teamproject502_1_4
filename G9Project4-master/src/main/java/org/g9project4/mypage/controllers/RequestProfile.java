package org.g9project4.mypage.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.g9project4.member.constants.Gender;
import org.g9project4.member.constants.Interest;
import org.g9project4.member.entities.Interests;

import java.time.LocalDate;
import java.util.List;

@Data
//@NoArgsConstructor @AllArgsConstructor
public class RequestProfile {

    private String gid;

    @NotBlank
    private String userName;

    private String password;

    private String confirmPassword;

    private String mobile;

    private LocalDate birth;  // 출생일

    private Gender gende;  // 성별 (MALE, FEMALE)

    private Boolean isForeigner;  // 외국인 여부 (외국인 true, 내국인 false)

    private List<Interest> interests;

    // Other fields and methods



//    데이터 전송: 클라이언트와의 데이터 전송에서 단순한 이넘 값을 사용하는 것이 더 효율적일 수 있습니다. 이 경우 List<Interest>를 사용하는 것이 좋습니다.
//    비즈니스 로직: 데이터베이스와의 매핑이 필요하거나 추가적인 정보가 있는 경우, List<Interests>가 필요할 수 있습니다.

}
