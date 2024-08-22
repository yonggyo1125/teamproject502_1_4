package org.hidog.board.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hidog.board.entities.Board;
import org.hidog.global.Utils;
import org.hidog.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {
    private final RestTemplate restTemplate; // 외부 API를 호출하기 위해 사용되는 Spring의 HTTP 클라이언트
    private final ObjectMapper om; // JSON 데이터를 Java 객체로 변환하거나, 그 반대로 변환하기 위해 사용되는 Jackson 라이브러리의 객체
    private final Utils utils;

    /**
     * 게시판 설정 조회
     *
     * @param bid
     * @return
     */
    public Optional<Board> get(String bid) {
        try {
            String url = utils.adminUrl("/api/board/config/" + bid); // 특정 게시판 ID(bid)에 대한 설정을 가져올 API의 URL을 생성
            ResponseEntity<JSONData> response = restTemplate.getForEntity(url, JSONData.class);

            if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                JSONData jsonData = response.getBody();
                if (!jsonData.isSuccess()) {

                    return Optional.empty();
                }

                Object data = jsonData.getData();

                Board board = om.readValue(om.writeValueAsString(data), Board.class);

                return Optional.ofNullable(board);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * 게시판 리스트 조회
     *
     * @return
     */
    public Optional<Board> getBoardList() {
        try {
            String url = utils.adminUrl("/api/board");
            ResponseEntity<JSONData> response = restTemplate.getForEntity(url, JSONData.class);

            if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                JSONData jsonData = response.getBody();
                if (!jsonData.isSuccess()) {

                    return Optional.empty();
                }

                Object data = jsonData.getData();

                Board board = om.readValue(om.writeValueAsString(data), Board.class);


                return Optional.ofNullable(board);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}