package com.atypon.task1.data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IPSession {

    private int intervalRequestsCount;

    private int throttlingRequestsCount;

    private long minuteIntervalStart;

    public void updateThrottlingRequestCount(int value) {
        throttlingRequestsCount += value;
    }

    public void updateIntervalRequestsCount(int value) {
        intervalRequestsCount += value;
    }

}
