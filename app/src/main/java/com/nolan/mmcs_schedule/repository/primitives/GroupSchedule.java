package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculum;
import com.nolan.mmcs_schedule.repository.api.primitives.RawLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfGroup;

import java.util.TreeMap;

public class GroupSchedule {
    // first index means day of week(0..6)
    // second index means ordinal number of lesson(0..6)
    public final GroupLesson[][] lessons;

    public GroupSchedule(RawScheduleOfGroup rawScheduleOfGroup) {
        TreeMap<Integer, LessonTime> idToLessonInfo = new TreeMap<>();
        for (RawLesson rawLesson : rawScheduleOfGroup.getLessons()) {
            idToLessonInfo.put(rawLesson.getId(), new LessonTime(rawLesson.getTimeSlot()));
        }

        lessons = new GroupLesson[6][];
        for (int i = 0; i < 6; ++i) {
            lessons[i] = new GroupLesson[6];
        }
        for (RawCurriculum rawCurriculum : rawScheduleOfGroup.getCurricula()) {
            LessonPeriod period = idToLessonInfo.get(rawCurriculum.getLessonId()).period;
            WeekType weekType = idToLessonInfo.get(rawCurriculum.getLessonId()).weekType;
            String subjectName = rawCurriculum.getSubjectName();
            String teacher = rawCurriculum.getTeacherName();
            String room = rawCurriculum.getRoomName();
            int dayOfWeek = idToLessonInfo.get(rawCurriculum.getLessonId()).dayOfWeek;
            int lessonNumber;
            // It looks awful but I don't see any other way to find ordinal number of lesson.
            switch (period.begin.hour) {
                case 8: lessonNumber = 0; break;
                case 9: lessonNumber = 1; break;
                case 11: lessonNumber = 2; break;
                case 13: lessonNumber = 3; break;
                case 15: lessonNumber = 4; break;
                case 18: lessonNumber = 5; break;
                default: throw new Error("Illegal lesson begin time.");
            }
            lessons[dayOfWeek][lessonNumber] =
                    new GroupLesson(period, weekType, subjectName, teacher, room);
        }
    }

}
