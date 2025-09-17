package com.muyingmall.user.event;

import org.springframework.context.ApplicationEvent;

public class CheckinEvent extends ApplicationEvent {
    private final Integer userId;
    private final Integer earnedPoints;
    private final int continuousDays;
    private final String extraJson;

    public CheckinEvent(Object source, Integer userId, Integer earnedPoints, int continuousDays, String extraJson) {
        super(source);
        this.userId = userId;
        this.earnedPoints = earnedPoints;
        this.continuousDays = continuousDays;
        this.extraJson = extraJson;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getEarnedPoints() {
        return earnedPoints;
    }

    public int getContinuousDays() {
        return continuousDays;
    }

    public String getExtraJson() {
        return extraJson;
    }
}
