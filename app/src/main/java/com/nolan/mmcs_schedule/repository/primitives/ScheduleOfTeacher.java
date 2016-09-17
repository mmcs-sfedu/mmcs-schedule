package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculum;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGroupAtLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfTeacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

public class ScheduleOfTeacher {
    private ArrayList<ArrayList<LessonForTeacher>> days;

    public ScheduleOfTeacher(RawScheduleOfTeacher scheduleOfTeacher) {
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
        days = new ArrayList<>(7);
        for (int i = 0; i < 7; ++i) {
            days.add(new ArrayList<LessonForTeacher>());
        }
        for (RawLesson lesson : scheduleOfTeacher.getLessons()) {
            LessonTime lessonTime = new LessonTime(lesson.getTimeSlot());
            RawCurriculum curriculum = lessonIdToCurriculum.get(lesson.getId());
            int uberId = lesson.getUberId();
            LessonPeriod period = lessonTime.getPeriod();
            WeekType weekType = lessonTime.getWeekType();
            String subjectName = curriculum.getSubjectName();
            ArrayList<GroupAtLesson> groups = new ArrayList<>(uberIdToGroups.get(uberId).size());
            for (RawGroupAtLesson groupAtLesson : uberIdToGroups.get(uberId)) {
                String name = groupAtLesson.toString();
                int gradeNum = groupAtLesson.getGradeNum();
                int groupNum = groupAtLesson.getGroupNum();
                groups.add(new GroupAtLesson(name, gradeNum, groupNum));
            }
            String room = curriculum.getRoomName().isEmpty() ? "" : "а." + curriculum.getRoomName();
            int dayOfWeek = lessonTime.getDayOfWeek();
            days.get(dayOfWeek).add(new LessonForTeacher(period, weekType, subjectName, dayOfWeek, groups, room));
        }
        for (ArrayList<LessonForTeacher> lessons : days) {
            Collections.sort(lessons, new Comparator<LessonForTeacher>() {
                @Override
                public int compare(LessonForTeacher lhs, LessonForTeacher rhs) {
                    return lhs.getPeriod().getBegin().getHour() - rhs.getPeriod().getBegin().getHour();
                }
            });
        }
    }

    public ArrayList<ArrayList<LessonForTeacher>> getDays() { return days; }
}
