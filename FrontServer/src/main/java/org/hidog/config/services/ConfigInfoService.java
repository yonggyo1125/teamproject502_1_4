package org.hidog.config.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.hidog.board.entities.Board;
import org.hidog.board.exceptions.BoardNotFoundException;
import org.hidog.board.repositories.BoardRepository;
import org.hidog.config.entities.Configs;
import org.hidog.config.repositories.ConfigsRepository;
import org.hidog.file.services.FileInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigInfoService {

    private final BoardRepository boardRepository;
    private final ConfigsRepository repository;
    private final FileInfoService fileInfoService;

    /**
     * 게시판 설정 조회
     *
     * @param bid
     * @return
     */
    public Board get(String bid) {
        Board board = boardRepository.findById(bid).orElseThrow(BoardNotFoundException::new);

        addBoardInfo(board);

        return board;

    }

    /**
     * 게시판 설정 추가 정보
     *      - 에디터 첨부 파일 목록
     * @param board
     */
    public void addBoardInfo(Board board) {
        String gid = board.getGid();

        //List<FileInfo> htmlTopImages = fileInfoService.getListDone(gid, "html_top");

        //List<FileInfo> htmlBottomImages = fileInfoService.getListDone(gid, "html_bottom");

        //board.setHtmlTopImages(htmlTopImages);
        //board.setHtmlBottomImages(htmlBottomImages);
    }

    public <T> Optional<T> get(String code, Class<T> clazz) {
        return get(code, clazz, null);
    }

    public <T> Optional<T> get(String code, TypeReference<T> typeReference) {
        return get(code, null, typeReference);
    }

    public <T> Optional<T> get(String code, Class<T> clazz, TypeReference<T> typeReference) {
        Configs config = repository.findById(code).orElse(null);
        if (config == null || !StringUtils.hasText(config.getData())) {
            return Optional.ofNullable(null);
        }

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        String jsonString = config.getData();
        try {
            T data = null;
            if (clazz == null) { // TypeRefernce로 처리
                data = om.readValue(jsonString, new TypeReference<T>() {});
            } else { // Class로 처리
                data = om.readValue(jsonString, clazz);
            }
            return Optional.ofNullable(data);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            e.printStackTrace();
            return Optional.ofNullable(null);
        }
    }
}
