package org.g9project4.board.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.Board;
import org.g9project4.global.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {
    private final RestTemplate restTemplate;
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
            ResponseEntity<Board> response = restTemplate.getForEntity(url, Board.class);
            if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
