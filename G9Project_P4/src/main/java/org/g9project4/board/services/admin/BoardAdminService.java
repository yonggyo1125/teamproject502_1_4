package org.g9project4.board.services.admin;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.controllers.RequestBoard;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.repositories.BoardDataRepository;
import org.g9project4.global.exceptions.script.AlertException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardAdminService {
    private final BoardDataRepository dataRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    public void update(String mode, List<BoardData> items) {

        if (items == null || items.isEmpty()) {
            String modeStr = mode.equals("delete") ? "삭제" : "수정";
            throw new AlertException(String.format("%s할 게시글을 선택하세요.", modeStr), HttpStatus.BAD_REQUEST);
        }

        List<BoardData> updateItems = new ArrayList<>();
        for (BoardData item : items) {
            BoardData original = dataRepository.findById(item.getSeq()).orElse(null);
            if (original == null) continue;

            if (mode.equals("delete")) { // 삭제
                dataRepository.delete(original);
            } else { // 수정
                original.setPoster(item.getPoster());
                original.setSubject(item.getSubject());
                original.setContent(item.getContent());
                original.setCategory(item.getCategory());
                original.setEditorView(item.getBoard().isUseEditor());

                original.setNum1(item.getNum1());
                original.setNum2(item.getNum2());
                original.setNum3(item.getNum3());

                original.setText1(item.getText1());
                original.setText2(item.getText2());
                original.setText3(item.getText3());

                original.setLongText1(item.getLongText1());
                original.setLongText2(item.getLongText2());

                // 비회원 비밀번호 처리
                String guestPw = item.getGuestPw();
                if (StringUtils.hasText(guestPw)) {
                    original.setGuestPw(encoder.encode(guestPw));
                }

                original.setNotice(item.isNotice());
                updateItems.add(original);
            }
        }

        if (mode.equals("delete")) {
            dataRepository.flush();
        } else { // 수정
            dataRepository.saveAllAndFlush(updateItems);

        }
    }

    public void update(String mode, BoardData item) {
        update(mode, List.of(item));
    }

    public void update(String mode, RequestBoard form) {
        BoardData data = modelMapper.map(form, BoardData.class);

        update(mode, data);
    }
}