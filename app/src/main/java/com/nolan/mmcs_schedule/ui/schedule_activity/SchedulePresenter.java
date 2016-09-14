package com.nolan.mmcs_schedule.ui.schedule_activity;

import android.util.Pair;

import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.repository.primitives.LessonForGroup;
import com.nolan.mmcs_schedule.repository.primitives.ScheduleOfGroup;
import com.nolan.mmcs_schedule.repository.primitives.LessonForTeacher;
import com.nolan.mmcs_schedule.repository.primitives.ScheduleOfTeacher;
import com.nolan.mmcs_schedule.repository.primitives.WeekType;
import com.nolan.mmcs_schedule.ui.schedule_activity.lesson_dialog.LessonDetails;
import com.nolan.mmcs_schedule.utils.PairSerializable;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SchedulePresenter implements ScheduleAdapter.OnLessonClickListener {
    public interface View {
        void setSubtitle(String subtitle);
        void setSchedule(ArrayList<DaySchedule> schedule);
        void showLessonDetails(LessonDetails details);
        void startReportErrorActivity(String subject, String text);
        void startPickScheduleActivity();
        void onError(String message);
    }

    private View view;

    private ScheduleRepository repository;
    private UtilsPreferences preferences;

    private WeekType weekType;

    // Data acquired from repository.
    private WeekType currentWeek;
    // One of these fields will be set. Use @code{showingScheduleOfGroup} to know which one.
    private ScheduleOfGroup scheduleOfGroup;
    private ScheduleOfTeacher scheduleOfTeacher;

    // Map from day and lesson number in displayed schedule into LessonForGroup of scheduleOfGroup
    // if preferences.getPickedScheduleOfGroup() or LessonForTeacher otherwise.
    // Key is generated by getKey().
    private Map<Integer, Object> dayNumAndLessonNumToLesson;

    public static int getKey(int day, int lesson) {
        return 100 * day + lesson;
    }

    // Data describing current state of loading.
    // Only when both current week and schedule are loaded we can show schedule. But
    // they are loading concurrently so we need some kind of synchronization.
    private boolean loadedCurrentWeek = false;
    private boolean loadedSchedule = false;

    public SchedulePresenter(View view, ScheduleRepository repository, UtilsPreferences preferences) {
        this.view = view;
        this.repository = repository;
        this.preferences = preferences;
    }

    public void start() {
        repository.getCurrentWeekType(new RequestListener<WeekType>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                preferences.setScheduleWasPicked(false);
                view.onError("Не удалось получить текущую неделю");
            }

            @Override
            public void onRequestSuccess(WeekType wt) {
                WeekTypeOption weekTypeOption = preferences.getWeekTypeOption();
                currentWeek = wt;
                weekType = getWeekType(weekTypeOption);
                view.setSubtitle(getSubtitle(weekTypeOption));
                loadedCurrentWeek = true;
                showScheduleIfLoadingDone();
            }
        });

        if (preferences.getPickedScheduleOfGroup()) {
            repository.getScheduleOfGroup(preferences.getGroupId(), new RequestListener<ScheduleOfGroup>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    preferences.setScheduleWasPicked(false);
                    view.onError("Ошибка при загрузке расписания");
                }

                @Override
                public void onRequestSuccess(ScheduleOfGroup schedule) {
                    scheduleOfGroup = schedule;
                    loadedSchedule = true;
                    showScheduleIfLoadingDone();
                }
            });
        } else {
            repository.getScheduleOfTeacher(preferences.getTeacherId(), new RequestListener<ScheduleOfTeacher>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    preferences.setScheduleWasPicked(false);
                    view.onError("Ошибка при загрузке расписания");
                }

                @Override
                public void onRequestSuccess(ScheduleOfTeacher schedule) {
                    scheduleOfTeacher = schedule;
                    loadedSchedule = true;
                    showScheduleIfLoadingDone();
                }
            });
        }
    }

    private static final String[] DAYS_OF_WEEK = new String[] {
            "Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота", "Воскресенье"
    };

    private void showScheduleIfLoadingDone() {
        if (!loadedCurrentWeek || !loadedSchedule) return;
        view.setSchedule(preferences.getPickedScheduleOfGroup()
                         ? getWeekForGroup() : getWeekForTeacher());
    }

    private ArrayList<DaySchedule> getWeekForGroup() {
        ArrayList<DaySchedule> schedule = new ArrayList<>();
        dayNumAndLessonNumToLesson = new TreeMap<>();
        for (int i = 0; i < scheduleOfGroup.getDays().size(); ++i) {
            ArrayList<Lesson> lessons = new ArrayList<>();
            for (LessonForGroup lesson : scheduleOfGroup.getDays().get(i)) {
                if (weekType != WeekType.FULL
                        && lesson.getWeekType() != WeekType.FULL
                        && lesson.getWeekType() != weekType) {
                    continue;
                }
                Lesson textual = new Lesson(
                        lesson.getPeriod().getBegin().toString(),
                        lesson.getPeriod().getEnd().toString(),
                        lesson.getSubjectName(),
                        WeekType.FULL != weekType ? "" : weekTypeToString(lesson.getWeekType()));
                dayNumAndLessonNumToLesson.put(getKey(schedule.size(), lessons.size()), lesson);
                lessons.add(textual);
            }
            if (!lessons.isEmpty()) {
                schedule.add(new DaySchedule(DAYS_OF_WEEK[i], lessons));
            }
        }
        return schedule;
    }

    private ArrayList<DaySchedule> getWeekForTeacher() {
        ArrayList<DaySchedule> schedule = new ArrayList<>();
        dayNumAndLessonNumToLesson = new TreeMap<>();
        for (int i = 0; i < scheduleOfTeacher.getDays().size(); ++i) {
            ArrayList<Lesson> lessons = new ArrayList<>();
            for (LessonForTeacher lesson : scheduleOfTeacher.getDays().get(i)) {
                if (weekType != WeekType.FULL
                        && lesson.getWeekType() != WeekType.FULL
                        && lesson.getWeekType() != weekType) {
                    continue;
                }
                Lesson textual = new Lesson(
                        lesson.getPeriod().getBegin().toString(),
                        lesson.getPeriod().getEnd().toString(),
                        lesson.getSubjectName(),
                        weekType == WeekType.FULL ? weekTypeToString(lesson.getWeekType()) : "");
                lessons.add(textual);
                dayNumAndLessonNumToLesson.put(getKey(schedule.size() - 1, lessons.size() - 1), lesson);
            }
            if (!lessons.isEmpty()) {
                schedule.add(new DaySchedule(DAYS_OF_WEEK[i], lessons));
            }
        }
        return schedule;
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
            default: throw new Error("unreachable statement");
        }
    }

    public void onWeekTypeOptionChanged(WeekTypeOption weekTypeOption) {
        preferences.setWeekTypeOption(weekTypeOption);
        weekType = getWeekType(weekTypeOption);
        view.setSubtitle(getSubtitle(weekTypeOption));
        // TODO: Agrhhh
        showScheduleIfLoadingDone();
    }

    private static String str(WeekType weekType) {
        switch (weekType) {
            case UPPER: return "верхняя";
            case LOWER: return "нижняя";
            case FULL: return "";
            default: throw new Error("unreachable statement");
        }
    }

    private String getSubtitle(WeekTypeOption weekTypeOption) {
        switch (weekTypeOption) {
            case CURRENT: return "текущая \"" + str(currentWeek) + "\"";
            case FULL: return str(WeekType.FULL);
            case UPPER: return str(WeekType.UPPER);
            case LOWER: return str(WeekType.LOWER);
            default: throw new Error("unreachable statement");
        }
    }

    private static String weekTypeToString(WeekType weekType) {
        switch (weekType) {
            case FULL:  return "";
            case LOWER: return "нижняя неделя";
            case UPPER: return "верхняя неделя";
            default: throw new Error("unreachable statement");
        }
    }

    public void onReportError() {
        String subject = "Ошибка в расписании";
        // todo: Fill text with grade,group/teacher name information
        view.startReportErrorActivity(subject, "");
    }

    @Override
    public void onLessonClick(int dayIndex, int lessonIndex) {
        Object lesson = dayNumAndLessonNumToLesson.get(getKey(dayIndex, lessonIndex));
        if (preferences.getPickedScheduleOfGroup()) {
            LessonForGroup lessonForGroup = (LessonForGroup) lesson;
            ArrayList<PairSerializable<String, String>> roomsAndTeachers
                    = new ArrayList<>(lessonForGroup.getRoomsAndTeachers().size());
            for (Pair<String, String> roomAndTeacher : lessonForGroup.getRoomsAndTeachers()) {
                roomsAndTeachers.add(
                        new PairSerializable<>(roomAndTeacher.first, roomAndTeacher.second));
            }
            view.showLessonDetails(new LessonDetails(
                    lessonForGroup.getPeriod().getBegin().toString(),
                    lessonForGroup.getPeriod().getEnd().toString(),
                    lessonForGroup.getSubjectName(),
                    roomsAndTeachers));
        } else {
            LessonForTeacher lessonForTeacher = (LessonForTeacher) lesson;
            ArrayList<PairSerializable<String, String>> roomAndGroups = new ArrayList<>();
            if (lessonForTeacher.getGroups().isEmpty()) {
                roomAndGroups.add(new PairSerializable<>(lessonForTeacher.getRoom(), ""));
            } else {
                roomAndGroups.add(new PairSerializable<>(lessonForTeacher.getRoom(),
                        lessonForTeacher.getGroups().get(0)));
                for (int i = 1; i < lessonForTeacher.getGroups().size(); ++i) {
                    roomAndGroups.add(new PairSerializable<>("", lessonForTeacher.getGroups().get(1)));
                }
            }
            view.showLessonDetails(new LessonDetails(
                    lessonForTeacher.getPeriod().getBegin().toString(),
                    lessonForTeacher.getPeriod().getEnd().toString(),
                    lessonForTeacher.getSubjectName(),
                    roomAndGroups));
        }
    }
}
