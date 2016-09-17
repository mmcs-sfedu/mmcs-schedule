package com.nolan.mmcs_schedule.repository.primitives;

import java.util.ArrayList;

public class LessonForGroup {
    private LessonPeriod period;
    private WeekType weekType;
    private String subjectName;
    private ArrayList<RoomForLesson> rooms;
    private int dayOfWeek; // 0-based

    public LessonForGroup(LessonPeriod period, WeekType weekType, String subjectName,
                          ArrayList<RoomForLesson> rooms, int dayOfWeek) {
        this.period = period;
        this.weekType = weekType;
        this.subjectName = subjectName;
        this.rooms = rooms;
        this.dayOfWeek = dayOfWeek;
    }

    public LessonPeriod getPeriod() {
        return period;
    }

    public WeekType getWeekType() {
        return weekType;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public ArrayList<RoomForLesson> getRooms() {
        return rooms;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }
}

