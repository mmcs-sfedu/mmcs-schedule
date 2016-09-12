package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculum;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGroupAtLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfTeacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class TeacherSchedule {
    private ArrayList<TreeSet<TeacherLesson>> lessons;

    public TeacherSchedule(RawScheduleOfTeacher scheduleOfTeacher) {
        TreeMap<Integer, RawCurriculum> lessonIdToCurriculum = new TreeMap<>();
        for (RawCurriculum rawCurriculum : scheduleOfTeacher.getCurricula()) {
            lessonIdToCurriculum.put(rawCurriculum.getLessonId(), rawCurriculum);
        }
        TreeMap<Integer, TreeSet<RawGroupAtLesson>> uberIdToGroups = new TreeMap<>();
        for (RawGroupAtLesson group : scheduleOfTeacher.getGroups()) {
            int uberId = group.getUberId();
            TreeSet<RawGroupAtLesson> groupsWithSameUberId = uberIdToGroups.get(uberId);
            if (groupsWithSameUberId == null) {
                groupsWithSameUberId = new TreeSet<>();
                uberIdToGroups.put(uberId, groupsWithSameUberId);
            }
            groupsWithSameUberId.add(group);
        }
        lessons = new ArrayList<>(7);
        for (int i = 0; i < 7; ++i) {
            lessons.add(new TreeSet<TeacherLesson>());
        }
        for (RawLesson lesson : scheduleOfTeacher.getLessons()) {
            LessonTime lessonTime = new LessonTime(lesson.getTimeSlot());
            RawCurriculum curriculum = lessonIdToCurriculum.get(lesson.getId());
            int uberId = lesson.getUberId();
            LessonPeriod period = lessonTime.getPeriod();
            WeekType weekType = lessonTime.getWeekType();
            String subjectName = curriculum.getSubjectName();
            ArrayList<String> groups = new ArrayList<>(uberIdToGroups.get(uberId).size());
            for (RawGroupAtLesson groupAtLesson : uberIdToGroups.get(uberId)) {
                groups.add(groupAtLesson.toString());
            }
            String room = curriculum.getRoomName().isEmpty() ? "" : "ауд.: " + curriculum.getRoomName();
            int dayOfWeek = lessonTime.getDayOfWeek();
            lessons.get(dayOfWeek).add(new TeacherLesson(period, weekType, subjectName, groups, room));
        }
    }

    public ArrayList<TreeSet<TeacherLesson>> getLessons() {
        return lessons;
    }
}
