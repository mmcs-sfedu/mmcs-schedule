package com.nolan.mmcs_schedule.ui.schedule_activity.lesson_dialog;

import com.nolan.mmcs_schedule.repository.primitives.GroupAtLesson;
import com.nolan.mmcs_schedule.repository.primitives.LessonForTeacher;
import com.nolan.mmcs_schedule.repository.primitives.LessonPeriod;
import com.nolan.mmcs_schedule.utils.TimeUtils;

import java.util.ArrayList;

public class LessonForTeacherDetails {
    private String subject;
    private String period;
    private String dayOfWeek;
    private String room;
    private ArrayList<String> groups;

    public LessonForTeacherDetails(LessonForTeacher lesson) {
        this.subject = lesson.getSubjectName();
        LessonPeriod period = lesson.getPeriod();
        this.period = period.getBegin().toString() + " - " + period.getEnd().toString();
        this.dayOfWeek = TimeUtils.getDisplayDayOfWeek(lesson.getDayOfWeek());
        this.groups = new ArrayList<>(lesson.getGroups().size());
        for (GroupAtLesson groupAtLesson : lesson.getGroups()) {
            this.groups.add(groupAtLesson.getName());
        }
        this.room = lesson.getRoom();
    }

    public String getSubject() {
        return subject;
    }

    public String getPeriod() {
        return period;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getRoom() {
        return room;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }
}