package org.g9project4.publicData.events;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PublicDataDoneEvent {
    private String workNm;
    public PublicDataDoneEvent(String workNm) {
        this.workNm = workNm;
        log.info("PublicDataEvent - {} 종료", workNm);
    }
}