package org.g9project4.publicData.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public class PublicDataStartEvent {
    private String workNm;
    private String serviceKey;
    public PublicDataStartEvent(String workNm,String serviceKey) {
        this.workNm = workNm;
        this.serviceKey = serviceKey;
        log.info("PublicDataEvent - {}시작", workNm);
    }

    public PublicDataStartEvent(String 작업명) {
    }
}
