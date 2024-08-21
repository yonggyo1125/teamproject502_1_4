package org.hidog.member;

import lombok.RequiredArgsConstructor;
import org.hidog.file.entities.FileInfo;
import org.hidog.member.constants.Authority;
import org.hidog.member.entities.Authorities;
import org.hidog.member.entities.Member;
import org.hidog.member.repositories.MemberRepository;
import org.hidog.mypage.services.MypageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberUtil {
   //  private final HttpSession session;
   //  private final MemberInfoService infoService;
    private final MemberRepository memberRepository;
    private final MypageService myPageService;
    @Value("${file.upload.url}")
    private String fileUploadUrl;

    public boolean isLogin() {
        return getMember() != null;
    }

    public boolean isAdmin() {
        if (isLogin()) {
            List<Authorities> authorities = getMember().getAuthorities();
            return authorities.stream().anyMatch(s -> s.getAuthority().equals(Authority.ADMIN));
        }
        return false;
    }

    public Member getMember() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo memberInfo) {

            /*
            if (session.getAttribute("userInfoChanged") != null) { // 회원 정보를 변경한 경우
                memberInfo = (MemberInfo)infoService.loadUserByUsername(memberInfo.getEmail());
            }
            */
            return memberInfo.getMember();
        }

        return null;
    }

    // 프로필 이미지 저장
    public String saveProfileImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IOException("파일이 비어 있습니다.");
        }

        Member member = getMember();
        if (member == null) {
            throw new IOException("회원 정보를 찾을 수 없습니다.");
        }

        // 파일 정보 저장 및 파일 시스템에 저장
        FileInfo fileInfo = myPageService.uploadProfileImage(image, member.getSeq());

        // 파일 정보로부터 URL 생성
        return "/files/" + fileInfo.getSeq() + fileInfo.getExtension();
    }

    // 데이터베이스에 프로필 이미지 URL 업데이트
    public void updateProfileImage(Long memberId, String newImageUrl) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.setProfileImage(newImageUrl);
        memberRepository.save(member);
    }

    // 현재 프로필 이미지 URL 가져옴
    public String getProfileImageUrl() {
        Member member = getMember();
        if (member != null) {
            // 실제 데이터베이스에서 프로필 이미지 URL을 가져오는 로직 추가
            return member.getProfileImage();
        }
        return "/images/default-profile.png";
    }
}