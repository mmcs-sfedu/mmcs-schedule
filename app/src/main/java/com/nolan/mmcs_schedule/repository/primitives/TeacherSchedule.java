package com.nolan.mmcs_schedule.repository.primitives;

import android.util.Pair;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculum;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGroupOfLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfTeacher;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeacherSchedule {
    // first index means day of week(0..6)
    // second index means ordinal number of lesson(0..6)
    private TeacherLesson[][] lessons;

    private static class LessonTime {
        public final LessonPeriod period;
        public final int dayOfWeek;
        public final WeekType weekType;

        public LessonTime(LessonPeriod period, int dayOfWeek, WeekType weekType) {
            this.period = period;
            this.dayOfWeek = dayOfWeek;
            this.weekType = weekType;
        }
    }

    private static LessonTime parseLessonPeriod(String timeSlot) {
        Pattern pattern = Pattern.compile(
                "\\(([0-6]),(\\d\\d):(\\d\\d):00,(\\d\\d):(\\d\\d):00,(upper|lower|full)\\)");
        Matcher matcher = pattern.matcher(timeSlot);
        if (!matcher.matches()) throw new Error("Wrong timeSlot format.");
        int dayOfWeek = Integer.parseInt(matcher.group(1));
        LessonPeriod lessonPeriod = new LessonPeriod(
                new HourMinute(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))),
                new HourMinute(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)))
        );
        WeekType weekType;
        switch (matcher.group(6).charAt(0)) {
            case 'u': weekType = WeekType.UPPER; break;
            case 'l': weekType = WeekType.LOWER; break;
            case 'f': weekType = WeekType.FULL; break;
            default:
                throw new Error("unreachable statement");
        }
        return new LessonTime(lessonPeriod, dayOfWeek, weekType);
    }

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
                    parseLessonPeriod(lesson.getTimeSlot()),
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
            switch (idToLessonInfo.get(curriculum.getLessonId()).first.period.begin.hour) {
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
