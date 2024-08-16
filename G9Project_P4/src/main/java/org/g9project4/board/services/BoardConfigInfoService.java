package org.g9project4.board.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.Board;
import org.g9project4.global.Utils;
import org.g9project4.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {
    private final RestTemplate restTemplate;
    private final ObjectMapper om;
    private final Utils utils;

    /**
     * 게시판 설정 조회
     *
     * @param bid
     * @return
     */
    public Board get(String bid) {
        try {
            String url = utils.adminUrl("/api/board/config/" + bid);
            ResponseEntity<JSONData> response = restTemplate.getForEntity(url, JSONData.class);
            if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                JSONData jsonData = response.getBody();
                if (!jsonData.isSuccess()) {
                    return null;
                }

                Object data = jsonData.getData();

                Board board = om.readValue(om.writeValueAsString(data), Board.class);

                return board;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
