package com.nolan.mmcs_schedule.ui.schedule_activity;

public class Lesson {
    public final String beginTime;
    public final String endTime;
    public final String primaryText;
    public final String weekType;

    public Lesson(String beginTime, String endTime, String primaryText, String weekType) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.primaryText = primaryText;
        this.weekType = weekType;
    }
}
