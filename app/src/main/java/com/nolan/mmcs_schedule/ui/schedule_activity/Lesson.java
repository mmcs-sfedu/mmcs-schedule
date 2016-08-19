package com.nolan.mmcs_schedule.ui.schedule_activity;

public class Lesson {
    public final String beginTime;
    public final String endTime;
    public final String primaryText;
    public final String secondaryText;
    public final String tertiaryText;
    public final String weekType;

    public Lesson(String beginTime, String endTime, String primaryText,
                  String secondaryText, String tertiaryText, String weekType) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.tertiaryText = tertiaryText;
        this.weekType = weekType;
    }
}
