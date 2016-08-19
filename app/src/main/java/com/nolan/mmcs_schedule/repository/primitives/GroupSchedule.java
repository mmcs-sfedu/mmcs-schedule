package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculum;
import com.nolan.mmcs_schedule.repository.api.primitives.RawLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

public class GroupSchedule {
    public final ArrayList<TreeSet<GroupLesson>> lessons;

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
        lessons = new ArrayList<>(6);
        Comparator<GroupLesson> comparator = new Comparator<GroupLesson>() {
            @Override
            public int compare(GroupLesson lhs, GroupLesson rhs) {
                return lhs.period.begin.hour > rhs.period.begin.hour ? 1 : -1;
            }
        };
        for (int i = 0; i < 6; ++i) {
            lessons.add(new TreeSet<>(comparator));
        }
        for (RawLesson rawLesson : rawScheduleOfGroup.getLessons()) {
            LessonTime lessonTime = new LessonTime(rawLesson.getTimeSlot());
            ArrayList<RawCurriculum> curricula = lessonIdToCurricula.get(rawLesson.getId());
            LessonPeriod period = lessonTime.period;
            WeekType weekType = lessonTime.weekType;
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
            int dayOfWeek = lessonTime.dayOfWeek;
            lessons.get(dayOfWeek).add(new GroupLesson(
                    period, weekType, subjectName, teachers, rooms));
        }
    }

}
