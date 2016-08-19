package com.nolan.mmcs_schedule.ui.schedule_activity;

import java.util.ArrayList;

public class DaySchedule {
    public final String dayOfWeek;
    public final ArrayList<Lesson> lessons;

    public DaySchedule(String dayOfWeek, ArrayList<Lesson> lessons) {
        this.dayOfWeek = dayOfWeek;
        this.lessons = lessons;
    }

    public static class List extends ArrayList<DaySchedule> { }
}
