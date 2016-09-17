package com.nolan.mmcs_schedule.ui.schedule_activity.lesson_dialog;


import com.nolan.mmcs_schedule.repository.primitives.LessonForGroup;
import com.nolan.mmcs_schedule.repository.primitives.LessonPeriod;
import com.nolan.mmcs_schedule.repository.primitives.RoomForLesson;
import com.nolan.mmcs_schedule.utils.PairSerializable;
import com.nolan.mmcs_schedule.utils.TimeUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class LessonForGroupDetails implements Serializable {
    private String subject;
    private String period;
    private String dayOfWeek;
    private ArrayList<PairSerializable<String, String>> roomsAndTeachers;

    public LessonForGroupDetails(LessonForGroup lesson) {
        ArrayList<PairSerializable<String, String>> roomsAndTeachers
                = new ArrayList<>(lesson.getRooms().size());
        for (RoomForLesson roomAndTeacher : lesson.getRooms()) {
            roomsAndTeachers.add(
                    new PairSerializable<>(roomAndTeacher.getRoom(), roomAndTeacher.getTeacherName()));
        }
        this.subject = lesson.getSubjectName();
        LessonPeriod period = lesson.getPeriod();
        this.period = period.getBegin().toString() + " - " + period.getEnd().toString();
        this.dayOfWeek = TimeUtils.getDisplayDayOfWeek(lesson.getDayOfWeek());
        this.roomsAndTeachers = roomsAndTeachers;
    }

    public String getSubject() {
        return subject;
    }

    public String getPeriod() {
        return period;
    }

    public ArrayList<PairSerializable<String, String>> getRoomsAndTeachers() {
        return roomsAndTeachers;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
}
