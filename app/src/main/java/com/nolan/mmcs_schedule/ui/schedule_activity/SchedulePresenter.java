package com.nolan.mmcs_schedule.ui.schedule_activity;

import android.text.TextUtils;

import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.repository.primitives.GroupLesson;
import com.nolan.mmcs_schedule.repository.primitives.GroupSchedule;
import com.nolan.mmcs_schedule.repository.primitives.TeacherLesson;
import com.nolan.mmcs_schedule.repository.primitives.TeacherSchedule;
import com.nolan.mmcs_schedule.repository.primitives.WeekType;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

public class SchedulePresenter {
    public interface View {
        void startPickScheduleActivity();
    }

    private View view;
    private ScheduleRepository repository;
    private UtilsPreferences preferences;

    public SchedulePresenter(View view, ScheduleRepository repository, UtilsPreferences preferences) {
        this.view = view;
        this.repository = repository;
        this.preferences = preferences;
    }

    public void onPickAnotherSchedule() {
        preferences.setScheduleWasPicked(false);
        view.startPickScheduleActivity();
    }

    private static String str(WeekType weekType) {
        switch (weekType) {
            case UPPER: return "верхняя неделя";
            case LOWER: return "нижняя неделя";
            case FULL: return "";
        }
        throw new Error("unreachable statement");
    }

    private static final String[] DAYS_OF_WEEK = new String[] {
            "Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота", "Воскресенье"
    };

    public void getSchedule(boolean pickedScheduleOfGroup, int id,
                            final RequestListener<DaySchedule.List> listener) {
        if (pickedScheduleOfGroup) {
            repository.getScheduleOfGroup(id, new RequestListener<GroupSchedule>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    listener.onRequestFailure(spiceException);
                }

                @Override
                public void onRequestSuccess(GroupSchedule groupSchedule) {
                    DaySchedule.List schedule = new DaySchedule.List();
                    for (int i = 0; i < 6; ++i) {
                        ArrayList<Lesson> lessons = new ArrayList<>();
                        for (GroupLesson lesson : groupSchedule.lessons.get(i)) {
                            lessons.add(new Lesson(
                                    lesson.period.begin.toString(),
                                    lesson.period.end.toString(),
                                    lesson.subjectName,
                                    TextUtils.join(", ", lesson.rooms),
                                    TextUtils.join("\n", lesson.teachers),
                                    str(lesson.weekType)));
                        }
                        if (lessons.isEmpty())
                            continue;
                        schedule.add(new DaySchedule(DAYS_OF_WEEK[i], lessons));
                    }
                    listener.onRequestSuccess(schedule);
                }
            });
        } else {
            repository.getScheduleOfTeacher(id, new RequestListener<TeacherSchedule>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    listener.onRequestFailure(spiceException);
                }

                @Override
                public void onRequestSuccess(TeacherSchedule teacherSchedule) {
                    DaySchedule.List schedule = new DaySchedule.List();
                    for (int i = 0; i < 6; ++i) {
                        ArrayList<Lesson> lessons = new ArrayList<>();
                        for (TeacherLesson lesson : teacherSchedule.lessons.get(i)) {
                            lessons.add(new Lesson(
                                    lesson.period.begin.toString(),
                                    lesson.period.end.toString(),
                                    lesson.subjectName,
                                    lesson.room,
                                    TextUtils.join(", ", lesson.groups),
                                    str(lesson.weekType)));
                        }
                        if (lessons.isEmpty())
                            continue;
                        schedule.add(new DaySchedule(DAYS_OF_WEEK[i], lessons));
                    }
                    listener.onRequestSuccess(schedule);
                }
            });
        }
    }
}
