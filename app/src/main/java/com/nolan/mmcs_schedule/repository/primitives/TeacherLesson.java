package com.nolan.mmcs_schedule.repository.primitives;

import java.util.ArrayList;

public class TeacherLesson {
    public final LessonPeriod period;
    public final WeekType weekType;
    public final String subjectName;
    public final ArrayList<String> groups;
    public final String room;

    public TeacherLesson(LessonPeriod period, WeekType weekType, String subjectName, ArrayList<String> groups,
                         String room) {
        this.period = period;
        this.weekType = weekType;
        this.subjectName = subjectName;
        this.groups = groups;
        this.room = room;
    }
}
