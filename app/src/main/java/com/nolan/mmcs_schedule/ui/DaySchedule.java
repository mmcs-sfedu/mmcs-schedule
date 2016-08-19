package com.nolan.mmcs_schedule.ui;

import java.util.ArrayList;

class DaySchedule {
    public final String dayOfWeek;
    public final ArrayList<Lesson> lessons;

    public DaySchedule(String dayOfWeek, ArrayList<Lesson> lessons) {
        this.dayOfWeek = dayOfWeek;
        this.lessons = lessons;
    }
}
