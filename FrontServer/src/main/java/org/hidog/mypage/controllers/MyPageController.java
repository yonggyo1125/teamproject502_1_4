package org.hidog.mypage.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hidog.board.entities.BoardData;
import org.hidog.file.entities.FileInfo;
import org.hidog.global.Utils;
import org.hidog.member.MemberUtil;
import org.hidog.member.entities.Member;
import org.hidog.member.services.MemberService;
import org.hidog.mypage.services.MypageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final Utils utils;
    private final MemberUtil memberUtil;
    private final MemberService memberService;
    private final MypageService mypageService;

    @Value("${file.upload.url}")
    private String fileUploadUrl;

    /**
     * 1) 주소 창에 mypage/myhome 입력 시 마이 페이지 홈으로 이동
     * 2) 마이 페이지 홈 -> 버튼 O (프로필 이미지, 회원 정보 확인 버튼, 찜 목록 버튼, 게시글 버튼, 판매 내역 & 구매 내역 버튼)
     * 3) 원형 프로필 클릭 시 프로필 이미지 수정 팝업 생성 -> 이미지 저장 버튼 클릭 시 수정된 이미지로 변경 및 마이 페이지 홈에 가만히 있음
     * 4) 회원 정보 확인 버튼 클릭 시 회원 정보 페이지 (/mypage/info)로 이동 -> 로그인한 사용자의 정보가 나오고 그 아래에 메인 페이지 버튼 / 회원 정보 수정 버튼
     * -> 회원 정보 수정 버튼 클릭 시 로그인할 때 사용한 비밀번호로 본인 인증 -> 성공 시 회원 정보 수정 페이지 (/mypage/changInfo)로 이동 | 실패 시 마이 페이지로 이동
     * 5) 찜 목록 버튼 클릭 시 찜 목록 페이지 (/mypage/like)로 이동 -> 사진첩 처럼 이미지 목록으로 찜한 내역 목록화된 페이지 나옴
     * 6) 게시글 버튼 클릭 시 게시글 페이지 (/mypage/post)로 이동 -> 표 형태로 작성한 글 목록 나옴
     * 7) 판매 내역 & 구매 내역 버튼 클릭 시 판매 내역 & 구매 내역 페이지 (/mypage/sellAndBuy)로 이동 -> 5)와 동일하게 목록화된 페이지 나옴
     */

    // 마이 페이지 홈 + 프로필 수정 원
    @GetMapping("/myhome")
    public String myHome(Model model, HttpServletRequest request) {
        commonProcess("myhome", model);
        String profileImageUrl = (String) request.getSession().getAttribute("profileImage");
        if (profileImageUrl == null) {
            profileImageUrl = memberUtil.getProfileImageUrl();
        }
        model.addAttribute("profileImage", profileImageUrl);
        return utils.tpl("mypage/myhome");
    }

    // 프로필 수정 팝업 창
    @PostMapping("/myhome")
    public String updateProfileImage(@RequestParam("newProfileImage") MultipartFile newProfileImage, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getSession().getAttribute("userId");
            FileInfo uploadedFile = mypageService.uploadProfileImage(newProfileImage, userId); // 파일 업로드 및 FileInfo 반환
            String newImageUrl = "/files/" + uploadedFile.getSeq() + uploadedFile.getExtension(); // 새 이미지 URL 생성
            mypageService.updateProfileImage(userId, newImageUrl); // 프로필 이미지 URL 업데이트
            request.getSession().setAttribute("profileImage", newImageUrl); // 새 프로필 이미지 URL 저장

            return "redirect:" + utils.redirectUrl("mypage/myhome");
        } catch (IOException e) {
            handleException(request, "이미지 변경 중에 오류가 발생했습니다.");
        } catch (Exception e) {
            handleException(request, e.getMessage());
        }
        return "redirect:" + utils.redirectUrl("mypage/myhome");
    } /* 문제 : 선택한 이미지가 원 안에 저장이 안 됨 ㅠㅠ */

    // 마이 페이지 -> 회원 정보 확인 페이지
    @GetMapping("/info")
    public String memberInfo(Model model) {
        commonProcess("info", model);
        model.addAttribute("member", memberUtil.getMember());
        return utils.tpl("mypage/info");
    }

    // 회원 정보 확인 페이지 -> 회원 정보 수정 페이지 이동
    @GetMapping("/changeInfo")
    public String changeInfoPage(Model model) {

        Member member = memberUtil.getMember();
        model.addAttribute("member", member);
        commonProcess("changeInfo", model);

        return utils.tpl("mypage/changeInfo");
    }

    // 회원 정보 수정 페이지
    @PostMapping("/changeInfo")
    public String changeInfo(@Valid @ModelAttribute Member member, Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("errorMessage", "정보 수정 중 오류가 발생했습니다.");
            commonProcess("changeInfo", model);
            return utils.tpl("mypage/changeInfo");
        }

        try {
            memberService.updateMember(member);
            model.addAttribute("successMessage", "회원 정보가 수정되었습니다.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원 정보 수정 중 오류가 발생했습니다.");
            e.printStackTrace();
        }

        commonProcess("changeInfo", model);
        return utils.tpl("mypage/changeInfo");
    } /* 회원 정보 수정 버튼 클릭 시 '무결성 제약 조건에 위배' 에러 발생 */

    // 찜 목록 페이지
    @GetMapping("/like")
    public String like(Model model) {
        commonProcess("like", model);
        return utils.tpl("mypage/like");
    } /* 게시글 찜하기 기능 금요일 완성 예정 */

    // 내가 쓴 게시글 페이지
    @GetMapping("/post")
    public String post(Model model) {
        commonProcess("post", model);

        Member member = memberUtil.getMember();
        String userName = member.getUserName(); // 로그인한 사용자

        List<BoardData> posts = mypageService.getPostsByUserName(userName); // 사용자로 게시글 조회

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 날짜 포매터 정의

        List<BoardData> postWithFormattedDate = posts.stream().map(post -> {
            post.setFormattedCreatedAt(post.getCreatedAt().format(formatter));
            return post;
        }).collect(Collectors.toList()); // 날짜 -> 문자열 변환

        model.addAttribute("posts", posts); // 모델에 게시글 목록 추가

        return utils.tpl("mypage/post");
    }

    // 판매 내역 & 구매 내역 페이지
    @GetMapping("/sellAndBuy")
    public String sellAndBuy(Model model) {
        commonProcess("sellAndBuy", model);
        return utils.tpl("mypage/sellAndBuy");
    } /* 결제 기능 미완성 */

    /**
     * 마이 페이지 공통
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        mode = Objects.requireNonNullElse(mode, "myhome"); // mode 변수가 null 인지 체크 -> mode 가 null 이면 "myhome" 문자열로 초기화 -> null 이 아니면 mode 는 원래의 값 유지

        List<String> addCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        String pageTitle = ""; // 기본 페이지 제목 설정

        addCss.add("mypage/style"); // 마이 페이지 공통
        switch (mode) {
            case "info": // 회원 정보 확인 페이지
                addCss.add("mypage/info");
                pageTitle = "회원 정보 확인";
                break;
            case "changeInfo": // 회원 정보 수정 페이지
                addCss.add("mypage/changeInfo");
                addScript.add("mypage/changeInfo");
                pageTitle = "회원 정보 수정";
                break;
            case "like": // 찜 목록 페이지
                addCss.add("mypage/like");
                pageTitle = "찜 목록";
                break;
            case "post": // 게시글 목록 페이지
                addCss.add("mypage/post");
                pageTitle = "내 게시글";
                break;
            case "sellAndBuy": // 판매 & 구매 내역 목록 페이지
                addCss.add("mypage/sellAndBuy");
                pageTitle = "판매 및 구매 내역";
                break;
            default:
                addCss.add("mypage/myhome"); // 마이 페이지 홈
                pageTitle = "마이 페이지 홈";
                break;
        }

        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("pageName", mode);
        model.addAttribute("pageTitle", pageTitle); // 페이지 제목 추가
    }

    private String handleException(HttpServletRequest request, String message) {
        request.getSession().setAttribute("error", message);
        log.error(message);
        return "redirect:" + utils.redirectUrl("mypage/myhome");
    }
}