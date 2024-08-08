package org.g9project4.mypage.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.member.services.MemberSaveService;
import org.g9project4.mypage.controllers.RequestProfile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberSaveService saveService;

    public void update(RequestProfile form) {

    }
}
