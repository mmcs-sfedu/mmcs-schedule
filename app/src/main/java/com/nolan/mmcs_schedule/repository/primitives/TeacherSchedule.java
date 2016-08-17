package com.nolan.mmcs_schedule.repository.primitives;

import android.util.Pair;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculum;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGroupOfLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfTeacher;

import java.util.ArrayList;
import java.util.TreeMap;

public class TeacherSchedule {
    // first index means day of week(0..6)
    // second index means ordinal number of lesson(0..6)
    public final TeacherLesson[][] lessons;

    public TeacherSchedule(RawScheduleOfTeacher scheduleOfTeacher) {
        TreeMap<Integer, ArrayList<String>> uberIdToGroups = new TreeMap<>();
        for (RawGroupOfLesson group : scheduleOfTeacher.getGroups()) {
            int uberId = group.getUberId();
            ArrayList<String> groupsWithSameUberId = uberIdToGroups.get(uberId);
            if (groupsWithSameUberId == null) {
                groupsWithSameUberId = new ArrayList<>();
                uberIdToGroups.put(uberId, groupsWithSameUberId);
            }
            groupsWithSameUberId.add(group.toString());
        }

        TreeMap<Integer, Pair<LessonTime, ArrayList<String>>> idToLessonInfo = new TreeMap<>();
        for (RawLesson lesson : scheduleOfTeacher.getLessons()) {
            Pair<LessonTime, ArrayList<String>> info = new Pair<>(
                    new LessonTime(lesson.getTimeSlot()),
                    uberIdToGroups.get(lesson.getUberId()));
            idToLessonInfo.put(lesson.getId(), info);
        }

        lessons = new TeacherLesson[6][];
        for (int i = 0; i < 6; ++i) {
            lessons[i] = new TeacherLesson[6];
        }
        for (RawCurriculum curriculum : scheduleOfTeacher.getCurricula()) {
            LessonPeriod period = idToLessonInfo.get(curriculum.getLessonId()).first.period;
            WeekType weekType = idToLessonInfo.get(curriculum.getLessonId()).first.weekType;
            String subjectName = curriculum.getSubjectName();
            ArrayList<String> groups = idToLessonInfo.get(curriculum.getLessonId()).second;
            String room = curriculum.getRoomName();
            int dayOfWeek = idToLessonInfo.get(curriculum.getLessonId()).first.dayOfWeek;
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
                    new TeacherLesson(period, weekType, subjectName, groups, room);
        }
    }

}
