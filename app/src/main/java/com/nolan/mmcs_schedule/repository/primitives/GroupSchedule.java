package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculum;
import com.nolan.mmcs_schedule.repository.api.primitives.RawLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

public class GroupSchedule {
    private ArrayList<TreeSet<GroupLesson>> lessons;

    public GroupSchedule(RawScheduleOfGroup rawScheduleOfGroup) {
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
        lessons = new ArrayList<>(7);
        for (int i = 0; i < 7; ++i) {
            lessons.add(new TreeSet<GroupLesson>());
        }
        for (RawLesson rawLesson : rawScheduleOfGroup.getLessons()) {
            ArrayList<RawCurriculum> curricula = lessonIdToCurricula.get(rawLesson.getId());
            if (curricula == null) continue;
            LessonTime lessonTime = new LessonTime(rawLesson.getTimeSlot());
            LessonPeriod period = lessonTime.getPeriod();
            WeekType weekType = lessonTime.getWeekType();
            String subjectName = curricula.get(0).getSubjectName();
            TreeSet<String> teachers = new TreeSet<>();
            ArrayList<String> rooms = new ArrayList<>();
            for (RawCurriculum curriculum : curricula) {
                teachers.add(curriculum.getTeacherName());
                String roomName = curriculum.getRoomName();
                if (!roomName.isEmpty()) {
                    rooms.add("а." + roomName);
                }
            }
            int dayOfWeek = lessonTime.getDayOfWeek();
            lessons.get(dayOfWeek).add(new GroupLesson(
                    period, weekType, subjectName, teachers, rooms));
        }
    }

    public ArrayList<TreeSet<GroupLesson>> getLessons() {
        return lessons;
    }
}
