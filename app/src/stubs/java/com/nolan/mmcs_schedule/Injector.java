package com.nolan.mmcs_schedule;

import android.os.Handler;
import android.os.Looper;

import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGrade;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGroup;
import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.repository.primitives.GroupLesson;
import com.nolan.mmcs_schedule.repository.primitives.GroupSchedule;
import com.nolan.mmcs_schedule.repository.primitives.HourMinute;
import com.nolan.mmcs_schedule.repository.primitives.LessonPeriod;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.repository.primitives.TeacherSchedule;
import com.nolan.mmcs_schedule.repository.primitives.WeekType;
import com.nolan.mmcs_schedule.ui.BaseActivity;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class Injector {
    private static UtilsPreferences preferences;

    public static UtilsPreferences injectPreferences() {
        if (preferences == null) {
            preferences = new UtilsPreferences();
        }
        return preferences;
    }

    private static class ScheduleRepositoryStub extends ScheduleRepository {
        public ScheduleRepositoryStub(SpiceManager spiceManager) {
            super(spiceManager);
        }

        @Override
        public void getGrades(RequestListener<Grade.List> listener) {
            Grade.List grades = new Grade.List();
            grades.add(new Grade(new RawGrade(0, 0, RawGrade.Degree.BACHELOR)));
            listener.onRequestSuccess(grades);
        }

        @Override
        public void getGroups(int gradeId, int gradeNum, RequestListener<Group.List> listener) {
            Group.List groups = new Group.List();
            groups.add(new Group(new RawGroup(0, "NULL", 1, 0), 0));
            listener.onRequestSuccess(groups);
        }

        @Override
        public void getTeachers(RequestListener<Teacher.List> listener) {
            listener.onRequestSuccess(new Teacher.List());
        }

        private static LessonPeriod timeTable[] = new LessonPeriod[] {
                new LessonPeriod(new HourMinute(8, 0), new HourMinute(9, 35)),
                new LessonPeriod(new HourMinute(9, 50), new HourMinute(11, 25)),
                new LessonPeriod(new HourMinute(11, 55), new HourMinute(13, 30)),
                new LessonPeriod(new HourMinute(13, 45), new HourMinute(15, 20)),
                new LessonPeriod(new HourMinute(15, 50), new HourMinute(17, 25)),
        };

        private static void addGroupLesson(TreeSet<GroupLesson> lessons, int ordinalNumber,
                                           WeekType weekType, String subject) {
            lessons.add(new GroupLesson(timeTable[ordinalNumber], weekType, subject,
                    new TreeSet<String>(), new ArrayList<String>()));
        }

        @Override
        public void getScheduleOfGroup(int groupId, RequestListener<GroupSchedule> listener) {
            ArrayList<TreeSet<GroupLesson>> lessons = new ArrayList<>();
            for (int i = 0; i < 7; ++i) {
                TreeSet<GroupLesson> lessonsOfDay = new TreeSet<>(comparator);
                lessons.add(lessonsOfDay);
                addGroupLesson(lessons.get(i), 0, WeekType.UPPER, "One");
                addGroupLesson(lessons.get(i), 1, WeekType.UPPER, "Two");
                addGroupLesson(lessons.get(i), 1, WeekType.LOWER, "Three");
                if (i % 2 == 0) {
                    addGroupLesson(lessons.get(i), 2, WeekType.FULL, "Four");
                }
                addGroupLesson(lessons.get(i), 3, WeekType.UPPER, "Five");
                addGroupLesson(lessons.get(i), 3, WeekType.LOWER, "Six");
                if (i % 3 == 0) {
                    addGroupLesson(lessons.get(i), 4, WeekType.FULL, "Seven");
                }
            }
            listener.onRequestSuccess(new GroupSchedule(lessons));
        }

        @Override
        public void getScheduleOfTeacher(int teacherId, RequestListener<TeacherSchedule> listener) {
        }

        @Override
        public void getCurrentWeekType(RequestListener<WeekType> listener) {
            listener.onRequestSuccess(WeekType.UPPER);
        }
    }

    public static ScheduleRepository injectRepository(BaseActivity activity) {
        return new ScheduleRepositoryStub(activity.getSpiceManager());
    }
}
