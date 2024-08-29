package org.g9project4.member.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.global.entities.BaseEntity;
import org.g9project4.member.constants.Gender;
import org.g9project4.planner.entities.Planner;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(length = 45, nullable = false)
    private String gid;

    @Column(length = 65, unique = true, nullable = false)
    private String email;

    @Column(length = 65, nullable = false)
    private String password;

    @Column(length = 40, nullable = false)
    private String userName;

    @Column(length = 15, nullable = false)
    private String mobile;

    @Column(nullable = false)
    private  LocalDate birth;  // 출생일

    @NotNull
    @Enumerated(EnumType.STRING) // Enum 값을 데이터베이스에 문자열로 저장
    @Column(nullable = false)
    private  Gender gende;  //  성별 (MALE, FEMALE)

    @NotNull
    private  Boolean isForeigner;  // 외국인 여부 (외국인 true, 내국인 false)

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    @Size(max = 5, message = "A member can have a maximum of 5 interests.")
    private  List<Interests> interests;
    // 관심사 (맛집 | 호캉스 | 박물관 | 캠핑 | 등산 | 자연 | 예술 | 강/바다 | 아이와 함께 | 온가족 함께 | 연인과 함께 | 낚시)
    //MATJIB, HOCANCE, MUSEUM, CAMPING, HIKING, NATURE, ART, SEA, WITHCHILD, WITHFAMILY, WITHLOVER, FISHING
    @ToString.Exclude
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Authorities> authorities;

    @ToString.Exclude
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private  List<Planner> planners;

    @Transient
    private  FileInfo profileImage;




//    @ToString.Exclude
//    @OneToMany(mappedBy = "member")
//    private List<VisitRecords> visitRecords;

}