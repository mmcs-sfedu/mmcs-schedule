package com.nolan.mmcs_schedule.repository.primitives;

import android.util.Pair;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculum;
import com.nolan.mmcs_schedule.repository.api.primitives.RawLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

public class ScheduleOfGroup {
    private ArrayList<ArrayList<LessonForGroup>> days;

    public ScheduleOfGroup(RawScheduleOfGroup rawScheduleOfGroup) {
        TreeMap<Integer, ArrayList<RawCurriculum>> lessonIdToCurricula = new TreeMap<>();
        for (RawCurriculum rawCurriculum : rawScheduleOfGroup.getCurricula()) {
            int lessonId = rawCurriculum.getLessonId();
            ArrayList<RawCurriculum> curricula = lessonIdToCurricula.get(lessonId);
            if (curricula == null) {
                curricula = new ArrayList<>();
                lessonIdToCurricula.put(lessonId, curricula);
            }
            curricula.add(rawCurriculum);
        }
        days = new ArrayList<>(7);
        for (int i = 0; i < 7; ++i) {
            days.add(new ArrayList<LessonForGroup>());
        }
        for (RawLesson rawLesson : rawScheduleOfGroup.getLessons()) {
            ArrayList<RawCurriculum> curricula = lessonIdToCurricula.get(rawLesson.getId());
            if (curricula == null) continue;
            LessonTime lessonTime = new LessonTime(rawLesson.getTimeSlot());
            LessonPeriod period = lessonTime.getPeriod();
            WeekType weekType = lessonTime.getWeekType();
            String subjectName = curricula.get(0).getSubjectName();
            ArrayList<Pair<String, String>> roomsAndTeachers = new ArrayList<>();
            for (RawCurriculum curriculum : curricula) {
                roomsAndTeachers.add(
                        new Pair<>(curriculum.getRoomName(), curriculum.getTeacherName()));
            }
            int dayOfWeek = lessonTime.getDayOfWeek();
            days.get(dayOfWeek).add(new LessonForGroup(
                    period, weekType, subjectName, roomsAndTeachers));
        }
        for (ArrayList<LessonForGroup> lessons : days) {
            Collections.sort(lessons, new Comparator<LessonForGroup>() {
                @Override
                public int compare(LessonForGroup lhs, LessonForGroup rhs) {
                    return lhs.getPeriod().getBegin().getHour() - rhs.getPeriod().getBegin().getHour();
                }
            });
        }
    }

    public ArrayList<ArrayList<LessonForGroup>> getDays() { return days; }
}
