package org.g9project4.board.services.admin;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.controllers.RequestAdminList;
import org.g9project4.board.controllers.RequestBoard;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.repositories.BoardDataRepository;
import org.g9project4.global.exceptions.BadRequestException;
import org.modelmapper.ModelMapper;
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

    //목록 조회 삭제
    public List<BoardData> update(String mode, List<BoardData> items){

        if (items == null || items.isEmpty()){
            String modeStr = mode.equals("delete") ? "삭제" : "수정";
            throw new BadRequestException(String.format("%s할 게시글을 선택하세요.", modeStr));
        }

        List<BoardData> updateItems = new ArrayList<>();
        for (BoardData item : items){
            BoardData original = dataRepository.findById(item.getSeq()).orElse(null);
            if (original == null) continue;

            if (mode.equals("delete")) {
                dataRepository.delete(original);
            } else { //수정
                /* 글 작성, 글 수정 공통 S */
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
                /* 글 작성, 글 수정 공통 E */
            }
        }

        if (mode.equals("delete")) {
            dataRepository.flush();

            return items;
        } else { //수정
            dataRepository.saveAll(updateItems);
            return updateItems;
        }
    }

    //한개씩 조회 삭제
    public BoardData update(String mode, BoardData item){
        List<BoardData> items = update(mode, List.of(item));

        return items.get(0);
    }

    public BoardData update(String mode, RequestBoard form){
        BoardData data = modelMapper.map(form, BoardData.class);

        return update(mode, data);
    }
}
