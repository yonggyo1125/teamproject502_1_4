package org.g9project4.publicData.tour.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.g9project4.publicData.tour.events.PublicDataDoneEvent;
import org.g9project4.publicData.tour.events.PublicDataStartEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicDataEventListener {

    private final PublicDataUpdateService updateService;

    @Async
    @EventListener
    public void update1(PublicDataStartEvent event) throws InterruptedException{
        log.info("이벤트 수신, 작업명: {} 시작", event.getWorkNm());

        // 작업 코드 추가

        // 작업 끝나면 이벤트 발생 시켜 후속 작업 진행
        updateService.end();
    }

    @Async
    @EventListener
    public void update2(PublicDataDoneEvent event) {
        log.info("이벤트 수신, 작업명: {} 종료", event.getWorkNm());

        // 종료 관련 코드 처리 ...
    }
}
