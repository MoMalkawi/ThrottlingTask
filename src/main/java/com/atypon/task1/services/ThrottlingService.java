package com.atypon.task1.services;

import com.atypon.task1.data.IPSession;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ThrottlingService {

    private final ConcurrentHashMap<String, IPSession> ipSessions = new ConcurrentHashMap<>();

    private final IPSession emptySession = new IPSession();

    public boolean throttledRequest(String ipAddress, int maxRequestsPerInterval, int intervalTime) {
        synchronized (ipSessions) {
            ipSessions.putIfAbsent(ipAddress, new IPSession());
            IPSession session = ipSessions.get(ipAddress);
            handleIntervalTime(session, intervalTime);
            session.updateIntervalRequestsCount(1);
            return throttleRequest(session, maxRequestsPerInterval);
        }
    }

    private boolean throttleRequest(IPSession session, int maxRequestsPerInterval) {
        if((session.getIntervalRequestsCount() - 1) >= maxRequestsPerInterval) {
            session.updateThrottlingRequestCount(1);
            return true;
        }
        return false;
    }

    private void handleIntervalTime(IPSession session, int intervalTime) {
        long currentTime = System.currentTimeMillis();
        if((currentTime - session.getMinuteIntervalStart()) > intervalTime) {
            session.setMinuteIntervalStart(currentTime);
            session.setIntervalRequestsCount(0);
        }
    }

    public void finalizeRequestThrottle(String ipAddress) {
        synchronized (ipSessions) {
            IPSession session = ipSessions.get(ipAddress);
            session.updateThrottlingRequestCount(-1);
        }
    }

    public boolean exceedsThrottlingLimit(String ipAddress, int maxThrottlingLimit) {
        IPSession session = getIPSession(ipAddress);
        return session.getThrottlingRequestsCount() > maxThrottlingLimit;
    }

    private IPSession getIPSession(String ipAddress) {
        return ipSessions.getOrDefault(ipAddress, emptySession);
    }

}
