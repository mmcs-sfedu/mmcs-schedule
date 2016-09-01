package com.nolan.mmcs_schedule.ui.schedule_activity;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
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
        void setSubtitle(String subtitle);
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

    public void onPickAnotherSchedule() {
        preferences.setScheduleWasPicked(false);
        view.startPickScheduleActivity();
    }

    public WeekType getWeekType(WeekTypeOption weekTypeOption) {
        switch (weekTypeOption) {
            case CURRENT: return currentWeek;
            case FULL: return WeekType.FULL;
            case UPPER: return WeekType.UPPER;
            case LOWER: return WeekType.LOWER;
            default:
                throw new Error("unreachable statement");
        }
    }

    public void onWeekTypeOptionChanged(WeekTypeOption weekTypeOption) {
        preferences.setWeekTypeOption(weekTypeOption);
        weekType = getWeekType(weekTypeOption);
        view.setSubtitle(getSubtitle(weekType));
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

    private String getSubtitle(WeekType weekType) {
        if (weekType == null) return "";
        switch (weekType) {
            case UPPER: return "Верхняя неделя";
            case LOWER: return "Нижняя неделя";
            case FULL: return "Полное расписание";
            default:
                throw new Error("unreachable statement");
        }
    }

    public void getSchedule(final boolean pickedScheduleOfGroup, final int id,
                            final RequestListener<ScheduleAdapter.Data> listener) {
        repository.getCurrentWeekType(new RequestListener<WeekType>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                listener.onRequestFailure(spiceException);
            }

            @Override
            public void onRequestSuccess(WeekType weekType) {
                currentWeek = weekType;
                WeekTypeOption weekTypeOption = preferences.getWeekTypeOption();
                SchedulePresenter.this.weekType = getWeekType(weekTypeOption);
                view.setSubtitle(getSubtitle(SchedulePresenter.this.weekType));
                view.changeWeekType(weekType);
                getWeekTypeDone(pickedScheduleOfGroup, id, listener);
            }
        });
    }

    private void getWeekTypeDone(boolean pickedScheduleOfGroup, int id,
                            final RequestListener<ScheduleAdapter.Data> listener) {
        if (pickedScheduleOfGroup) {
            repository.getScheduleOfGroup(id, new RequestListener<GroupSchedule>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    listener.onRequestFailure(spiceException);
                }

                @Override
                public void onRequestSuccess(GroupSchedule groupSchedule) {
                    Gson gson = new Gson();
                    String json = gson.toJson(groupSchedule);
                    Log.i("json", json);
                    GroupSchedule groupSchedule1 = gson.fromJson(json, GroupSchedule.class);
                    listener.onRequestSuccess(groupScheduleToAdapterData(groupSchedule1));
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
                    listener.onRequestSuccess(teacherScheduleToAdapterData(teacherSchedule));
                }
            });
        }
    }

    private static ScheduleAdapter.Data groupScheduleToAdapterData(GroupSchedule groupSchedule) {
        DaySchedule.List scheduleFull = new DaySchedule.List();
        DaySchedule.List scheduleUpper = new DaySchedule.List();
        DaySchedule.List scheduleLower = new DaySchedule.List();
        for (int i = 0; i < groupSchedule.getLessons().size(); ++i) {
            ArrayList<Lesson> lessonsFull = new ArrayList<>();
            ArrayList<Lesson> lessonsUpper = new ArrayList<>();
            ArrayList<Lesson> lessonsLower = new ArrayList<>();
            for (GroupLesson lesson : groupSchedule.getLessons().get(i)) {
                Lesson textual = new Lesson(
                        lesson.getPeriod().getBegin().toString(),
                        lesson.getPeriod().getEnd().toString(),
                        lesson.getSubjectName(),
                        TextUtils.join(", ", lesson.getRooms()),
                        TextUtils.join("\n", lesson.getTeachers()),
                        str(lesson.getWeekType()));
                lessonsFull.add(textual);
                if (lesson.getWeekType() != WeekType.LOWER) {
                    lessonsUpper.add(textual);
                }
                if (lesson.getWeekType() != WeekType.UPPER) {
                    lessonsLower.add(textual);
                }
            }
            if (!lessonsFull.isEmpty()) {
                scheduleFull.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsFull));
            }
            if (!lessonsUpper.isEmpty()) {
                scheduleUpper.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsUpper));
            }
            if (!lessonsLower.isEmpty()) {
                scheduleLower.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsLower));
            }
        }
        return new ScheduleAdapter.Data(scheduleFull, scheduleUpper, scheduleLower);
    }

    private static ScheduleAdapter.Data teacherScheduleToAdapterData(TeacherSchedule teacherSchedule) {
        DaySchedule.List scheduleFull = new DaySchedule.List();
        DaySchedule.List scheduleUpper = new DaySchedule.List();
        DaySchedule.List scheduleLower = new DaySchedule.List();
        for (int i = 0; i < 6; ++i) {
            ArrayList<Lesson> lessonsFull = new ArrayList<>();
            ArrayList<Lesson> lessonsUpper = new ArrayList<>();
            ArrayList<Lesson> lessonsLower = new ArrayList<>();
            for (TeacherLesson lesson : teacherSchedule.getLessons().get(i)) {
                Lesson textual = new Lesson(
                        lesson.getPeriod().getBegin().toString(),
                        lesson.getPeriod().getEnd().toString(),
                        lesson.getSubjectName(),
                        lesson.getRoom(),
                        TextUtils.join(", ", lesson.getGroups()),
                        str(lesson.getWeekType()));
                lessonsFull.add(textual);
                if (lesson.getWeekType() != WeekType.LOWER) {
                    lessonsUpper.add(textual);
                }
                if (lesson.getWeekType() != WeekType.UPPER) {
                    lessonsLower.add(textual);
                }
            }
            if (!lessonsFull.isEmpty()) {
                scheduleFull.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsFull));
            }
            if (!lessonsUpper.isEmpty()) {
                scheduleUpper.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsUpper));
            }
            if (!lessonsLower.isEmpty()) {
                scheduleLower.add(new DaySchedule(DAYS_OF_WEEK[i], lessonsLower));
            }
        }
        return new ScheduleAdapter.Data(scheduleFull, scheduleUpper, scheduleLower);
    }
}
