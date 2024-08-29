package org.g9project4.publicData.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.g9project4.publicData.events.PublicDataDoneEvent;
import org.g9project4.publicData.events.PublicDataStartEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicDataUpdateService {
    private final ApplicationEventPublisher publisher;

    // 작업 시작 이벤트 발생시키기
    public void start() {
        publisher.publishEvent(new PublicDataStartEvent("작업명"));
    }


    // 작업 종료 이벤트 발생 시키기
    public void end() {
        publisher.publishEvent(new PublicDataDoneEvent("작업명"));
    }
}