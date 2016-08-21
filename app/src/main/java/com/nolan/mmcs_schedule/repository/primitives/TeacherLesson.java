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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TeacherLesson)) return false;
        TeacherLesson another = (TeacherLesson) obj;
        return period.equals(another.period) &&
                weekType.equals(another.weekType) &&
                subjectName.equals(another.subjectName) &&
                groups.equals(another.groups) &&
                room.equals(another.room);
    }
}
