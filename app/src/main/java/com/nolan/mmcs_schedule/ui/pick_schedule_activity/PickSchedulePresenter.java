package com.nolan.mmcs_schedule.ui.pick_schedule_activity;

import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

public class PickSchedulePresenter {
    public interface View {
        void startScheduleActivity();
        void setTeachers(List teachers);
        void setGroups(List groups);
        void setGrades(List grades);
        void showError(String text);
    }

    private View view;
    private ScheduleRepository repository;
    private UtilsPreferences preferences;

    private Teacher.List teachers = null;
    private Grade.List grades = null;
    private Group.List groups = null;

    private Teacher pickedTeacher = null;
    private Grade pickedGrade = null;
    private Group pickedGroup = null;

    private boolean groupWasPicked;

    public PickSchedulePresenter(View view, ScheduleRepository repository,
                                 UtilsPreferences preferences) {
        this.view = view;
        this.repository = repository;
        this.preferences = preferences;
    }

    public void onShowOptions(boolean forGroup) {
        if (forGroup) requestGrades();
        else requestTeachers();
    }

    private void requestGrades() {
        repository.getGrades(new RequestListener<Grade.List>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                view.showError("Ошибка при загрузке курсов");
            }

            @Override
            public void onRequestSuccess(Grade.List grades) {
                PickSchedulePresenter.this.grades = grades;
                view.setGrades(grades);
            }
        });
    }

    private void requestGroups(int gradeId, int gradeNum) {
        repository.getGroups(gradeId, gradeNum, new RequestListener<Group.List>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                view.showError("Ошибка при загрузке групп");
            }

            @Override
            public void onRequestSuccess(Group.List groups) {
                PickSchedulePresenter.this.groups = groups;
                view.setGroups(groups);
            }
        });
    }

    private void requestTeachers() {
        repository.getTeachers(new RequestListener<Teacher.List>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                view.showError("Ошибка при загрузке преподавателей");
            }

            @Override
            public void onRequestSuccess(Teacher.List teachers) {
                PickSchedulePresenter.this.teachers = teachers;
                view.setTeachers(teachers);
            }
        });
    }

    public void onPickTeacher(int position) {
        pickedTeacher = teachers.get(position);
        groupWasPicked = false;
    }

    public void onPickGrade(int position) {
        pickedGrade = grades.get(position);
        requestGroups(pickedGrade.getId(), pickedGrade.getNum());
    }

    public void onPickGroup(int position) {
        pickedGroup = groups.get(position);
        groupWasPicked = true;
    }

    public void onOk() {
        preferences.setScheduleWasPicked(true);
        preferences.setPickedScheduleOfGroup(groupWasPicked);
        if (groupWasPicked) {
            preferences.setGroupId(pickedGroup.getId());
            String title;
            switch (pickedGrade.getDegree()) {
                case BACHELOR:   title = "Бак. ";  break;
                case MASTER:     title = "Маг. ";  break;
                case SPECIALIST: title = "Спец. "; break;
                default: throw new Error("unreachable statement");
            }
            if (null != pickedGroup.getName()) {
                title += pickedGroup.getName() + " ";
            }
            title += pickedGrade.getNum() + "." + pickedGroup.getNum();
            preferences.setTitle(title);
        } else {
            preferences.setTeacherId(pickedTeacher.getId());
            preferences.setTitle(pickedTeacher.getName());
        }
        view.startScheduleActivity();
    }
}
