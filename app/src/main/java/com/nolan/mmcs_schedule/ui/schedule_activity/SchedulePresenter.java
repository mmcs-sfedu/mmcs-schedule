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
        void changeWeekType(WeekType weekType);
    }

    private View view;
    private ScheduleRepository repository;
    private UtilsPreferences preferences;
    private WeekType weekType;
    private WeekType currentWeek;

    public SchedulePresenter(View view, ScheduleRepository repository, UtilsPreferences preferences) {
        this.view = view;
        this.repository = repository;
        this.preferences = preferences;
    }

    public String getSubtitle() {
        if (weekType == null) return "";
        switch (weekType) {
            case UPPER:
                return "Верхняя неделя";
            case LOWER:
                return "Нижняя неделя";
            case FULL:
                return "Полное расписание";
            default:
                throw new Error("unreachable statement");
        }
    }

    public void onPickAnotherSchedule() {
        preferences.setScheduleWasPicked(false);
        view.startPickScheduleActivity();
    }

    public void onWeekTypeOptionChanged(WeekTypeOption weekTypeOption) {
        switch (weekTypeOption) {
            case CURRENT: weekType = currentWeek; break;
            case FULL: weekType = WeekType.FULL; break;
            case UPPER: weekType = WeekType.UPPER; break;
            case LOWER: weekType = WeekType.LOWER; break;
            default:
                throw new Error("unreachable statement");
        }
        view.changeWeekType(weekType);
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

    public void getSchedule(final boolean pickedScheduleOfGroup, final int id,
                            final RequestListener<ScheduleAdapter.ScheduleData> listener) {
        repository.getCurrentWeekType(new RequestListener<WeekType>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                listener.onRequestFailure(spiceException);
            }

            @Override
            public void onRequestSuccess(WeekType weekType) {
                currentWeek = weekType;
                onWeekTypeOptionChanged(preferences.getWeekTypeOption());
                getWeekTypeDone(pickedScheduleOfGroup, id, listener);
            }
        });
    }

    public void getWeekTypeDone(boolean pickedScheduleOfGroup, int id,
                            final RequestListener<ScheduleAdapter.ScheduleData> listener) {
        if (pickedScheduleOfGroup) {
            repository.getScheduleOfGroup(id, new RequestListener<GroupSchedule>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    listener.onRequestFailure(spiceException);
                }

                @Override
                public void onRequestSuccess(GroupSchedule groupSchedule) {
                    DaySchedule.List scheduleFull = new DaySchedule.List();
                    DaySchedule.List scheduleUpper = new DaySchedule.List();
                    DaySchedule.List scheduleLower = new DaySchedule.List();
                    for (int i = 0; i < 6; ++i) {
                        ArrayList<Lesson> lessonsFull = new ArrayList<>();
                        ArrayList<Lesson> lessonsUpper = new ArrayList<>();
                        ArrayList<Lesson> lessonsLower = new ArrayList<>();
                        for (GroupLesson lesson : groupSchedule.lessons.get(i)) {
                            Lesson textual = new Lesson(
                                    lesson.period.begin.toString(),
                                    lesson.period.end.toString(),
                                    lesson.subjectName,
                                    TextUtils.join(", ", lesson.rooms),
                                    TextUtils.join("\n", lesson.teachers),
                                    str(lesson.weekType));
                            lessonsFull.add(textual);
                            if (lesson.weekType != WeekType.LOWER) {
                                lessonsUpper.add(textual);
                            }
                            if (lesson.weekType != WeekType.UPPER) {
                                lessonsLower.add(textual);
                            }
                        }
                        if (lessonsFull.isEmpty())
                            continue;
                        scheduleFull.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsFull));
                        scheduleUpper.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsUpper));
                        scheduleLower.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsLower));
                    }
                    listener.onRequestSuccess(new ScheduleAdapter.ScheduleData(
                            scheduleFull, scheduleUpper, scheduleLower
                    ));
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
                    DaySchedule.List scheduleFull = new DaySchedule.List();
                    DaySchedule.List scheduleUpper = new DaySchedule.List();
                    DaySchedule.List scheduleLower = new DaySchedule.List();
                    for (int i = 0; i < 6; ++i) {
                        ArrayList<Lesson> lessonsFull = new ArrayList<>();
                        ArrayList<Lesson> lessonsUpper = new ArrayList<>();
                        ArrayList<Lesson> lessonsLower = new ArrayList<>();
                        for (TeacherLesson lesson : teacherSchedule.lessons.get(i)) {
                            Lesson textual = new Lesson(
                                    lesson.period.begin.toString(),
                                    lesson.period.end.toString(),
                                    lesson.subjectName,
                                    lesson.room,
                                    TextUtils.join(", ", lesson.groups),
                                    str(lesson.weekType));
                            lessonsFull.add(textual);
                            if (lesson.weekType != WeekType.LOWER) {
                                lessonsUpper.add(textual);
                            }
                            if (lesson.weekType != WeekType.UPPER) {
                                lessonsLower.add(textual);
                            }
                        }
                        if (lessonsFull.isEmpty())
                            continue;
                        scheduleFull.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsFull));
                        scheduleUpper.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsUpper));
                        scheduleLower.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsLower));
                    }
                    listener.onRequestSuccess(new ScheduleAdapter.ScheduleData(
                            scheduleFull, scheduleUpper, scheduleLower
                    ));
                }
            });
        }
    }
}
