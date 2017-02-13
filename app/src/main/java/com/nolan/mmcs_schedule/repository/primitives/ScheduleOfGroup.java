package com.nolan.mmcs_schedule.repository.primitives;

import android.util.Log;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculum;
import com.nolan.mmcs_schedule.repository.api.primitives.RawLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ScheduleOfGroup {
    private ArrayList<ArrayList<LessonForGroup>> days;

    public ScheduleOfGroup(RawScheduleOfGroup rawScheduleOfGroup) {
        Map<Integer, Map<String, List<RawCurriculum>>> lessonIdToCurricula = new TreeMap<>();
        for (RawCurriculum rawCurriculum : rawScheduleOfGroup.getCurricula()) {
            int lessonId = rawCurriculum.getLessonId();
            Map<String, List<RawCurriculum>> curricula = lessonIdToCurricula.get(lessonId);
            if (curricula == null) {
                curricula = new TreeMap<>();
                lessonIdToCurricula.put(lessonId, curricula);
            }
            List<RawCurriculum> curriculaWithSameSubjectName
                    = curricula.get(rawCurriculum.getSubjectName());
            if (curriculaWithSameSubjectName == null) {
                curriculaWithSameSubjectName = new ArrayList<>();
                curricula.put(rawCurriculum.getSubjectName(), curriculaWithSameSubjectName);
            }
            curriculaWithSameSubjectName.add(rawCurriculum);
        }
        days = new ArrayList<>(7);
        for (int i = 0; i < 7; ++i) {
            days.add(new ArrayList<LessonForGroup>());
        }
        for (RawLesson rawLesson : rawScheduleOfGroup.getLessons()) {
            Map<String, List<RawCurriculum>> curriculaWithSameSubjectName
                    = lessonIdToCurricula.get(rawLesson.getId());
            for (Map.Entry<String, List<RawCurriculum>> entry
                    : curriculaWithSameSubjectName.entrySet()) {
                List<RawCurriculum> curricula = entry.getValue();
                LessonTime lessonTime = new LessonTime(rawLesson.getTimeSlot());
                LessonPeriod period = lessonTime.getPeriod();
                WeekType weekType = lessonTime.getWeekType();
                String subjectName = curricula.get(0).getSubjectName();
                ArrayList<RoomForLesson> roomsAndTeachers = new ArrayList<>();
                for (RawCurriculum curriculum : curricula) {
                    roomsAndTeachers.add(new RoomForLesson(
                            curriculum.getTeacherId(),
                            curriculum.getTeacherName(),
                            curriculum.getRoomName()));
                }
                int dayOfWeek = lessonTime.getDayOfWeek();
                days.get(dayOfWeek).add(new LessonForGroup(
                        period, weekType, subjectName, roomsAndTeachers, dayOfWeek));
            }
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
