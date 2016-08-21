package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculum;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGroupOfLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfTeacher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

public class TeacherSchedule {
    public final ArrayList<TreeSet<TeacherLesson>> lessons;

    public TeacherSchedule(RawScheduleOfTeacher scheduleOfTeacher) {
        TreeMap<Integer, RawCurriculum> lessonIdToCurriculum = new TreeMap<>();
        for (RawCurriculum rawCurriculum : scheduleOfTeacher.getCurricula()) {
            lessonIdToCurriculum.put(rawCurriculum.getLessonId(), rawCurriculum);
        }
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
        lessons = new ArrayList<>(6);
        Comparator<TeacherLesson> comparator = new Comparator<TeacherLesson>() {
            @Override
            public int compare(TeacherLesson lhs, TeacherLesson rhs) {
                return lhs.period.begin.hour > rhs.period.begin.hour ? 1 : -1;
            }
        };
        for (int i = 0; i < 6; ++i) {
            lessons.add(new TreeSet<>(comparator));
        }
        for (RawLesson lesson : scheduleOfTeacher.getLessons()) {
            LessonTime lessonTime = new LessonTime(lesson.getTimeSlot());
            RawCurriculum curriculum = lessonIdToCurriculum.get(lesson.getId());
            int uberId = lesson.getUberId();
            LessonPeriod period = lessonTime.period;
            WeekType weekType = lessonTime.weekType;
            String subjectName = curriculum.getSubjectName();
            ArrayList<String> groups = uberIdToGroups.get(uberId);
            String room = curriculum.getRoomName().isEmpty() ? "" : "а." + curriculum.getRoomName();
            int dayOfWeek = lessonTime.dayOfWeek;
            lessons.get(dayOfWeek).add(new TeacherLesson(period, weekType, subjectName, groups, room));
        }
    }
}
