package com.nolan.mmcs_schedule.ui.pick_schedule_activity;

import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.request.listener.RequestListener;

public class PickSchedulePresenter {
    public interface View {
        void startScheduleActivity();
    }

    private View view;
    private ScheduleRepository repository;
    private UtilsPreferences preferences;

    private boolean pickedScheduleOfGroup;
    private int scheduleId;
    private String title;

    public PickSchedulePresenter(View view, ScheduleRepository repository,
                                 UtilsPreferences preferences) {
        this.view = view;
        this.repository = repository;
        this.preferences = preferences;
    }

    public void onPickTeacher(Teacher teacher) {
        pickedScheduleOfGroup = false;
        scheduleId = teacher.id;
        title = teacher.name;
    }

    public void onPickGroup(Group group) {
        pickedScheduleOfGroup = true;
        scheduleId = group.id;
        if (group.name == null) {
            title = "Группа " + group.gradeNum + "." + group.num;
        } else {
            title = "Группа " + group.name + " " + group.gradeNum + "." + group.num;
        }
    }

    public void onOk() {
        preferences.setScheduleWasPicked(true);
        preferences.setPickedScheduleOfGroup(pickedScheduleOfGroup);
        if (pickedScheduleOfGroup) {
            preferences.setGroupId(scheduleId);
        } else {
            preferences.setTeacherId(scheduleId);
        }
        preferences.setTitle(title);
        view.startScheduleActivity();
    }

    public void getTeachers(RequestListener<Teacher.List> listener) {
        repository.getTeachers(listener);
    }

    public void getGrades(RequestListener<Grade.List> listener) {
        repository.getGrades(listener);
    }

    public void getGroups(int gradeId, int gradeNum, RequestListener<Group.List> listener) {
        repository.getGroups(gradeId, gradeNum, listener);
    }
}
