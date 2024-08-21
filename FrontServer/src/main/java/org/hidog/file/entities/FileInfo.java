package org.hidog.file.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hidog.global.entities.BaseMemberEntity;

import java.util.UUID;

@Data
@Entity
@Builder @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo extends BaseMemberEntity {
    @Id
    @GeneratedValue
    private Long seq; // 서버에 업로드될 파일 이름  - seq.확장자

    @Column(length=45, nullable = false)
    private String gid = UUID.randomUUID().toString(); // 그룹 ID

    @Column(length=45)
    private String location; // 그룹 안에 세부 위치

    @Column(length=80, nullable = false)
    private String fileName;

    @Column(length=30)
    private String extension; // 파일 확장자

    @Column(length=80)
    private String contentType;

    private boolean done; // 그룹 작업 완료 여부

    @Column(length = 255) // 적절한 길이 설정
    private String fileUrl; // 파일 접근 URL

    @Transient
    private String filePath; // 파일 업로드 경로
}