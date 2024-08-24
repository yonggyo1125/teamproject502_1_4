package org.g9project4.publicData.tour.events;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PublicDataStartEvent {
    private String workNm;
    public PublicDataStartEvent(String workNm) {
        this.workNm = workNm;
        log.info("PublicDataEvent - {} 시작", workNm);
    }
}
