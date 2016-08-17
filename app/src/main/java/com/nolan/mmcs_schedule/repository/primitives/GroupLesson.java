package com.nolan.mmcs_schedule.repository.primitives;

public class GroupLesson {
    public final LessonPeriod period;
    public final WeekType weekType;
    public final String subjectName;
    public final String teacher;
    public final String room;

    public GroupLesson(LessonPeriod period, WeekType weekType, String subjectName,
                       String teacher, String room) {
        this.period = period;
        this.weekType = weekType;
        this.subjectName = subjectName;
        this.teacher = teacher;
        this.room = room;
    }
}
