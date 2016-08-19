package com.nolan.mmcs_schedule.ui;

import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.repository.primitives.GroupSchedule;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.repository.primitives.TeacherSchedule;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.request.listener.RequestListener;

import java.lang.ref.WeakReference;

public class MainPresenter {
    public interface OnScheduleTypePickedListener {
        void onStudent();
        void onTeacher();
    }

    public interface OnGroupPickedListener {
        void onGroupPicked(Grade grade, Group group);
    }

    public interface OnTeacherPickedListener {
        void onTeacherPicked(Teacher teacher);
    }

    public interface OnPickAnotherScheduleListener {
        void onPickAnotherSchedule();
    }

    public interface View {
        void setOnScheduleTypePickedListener(OnScheduleTypePickedListener listener);
        void setOnGroupPickedListener(OnGroupPickedListener listener);
        void setOnTeacherPickedListener(OnTeacherPickedListener listener);
        void setOnPickAnotherScheduleListener(OnPickAnotherScheduleListener listener);
        void showScheduleTypeOptions();
        void showStudentOptions();
        void showTeacherOptions();
        void showGroupSchedule();
        void showTeacherSchedule();
    }

    // We use weak reference to prevent activity leaks as View can have references to activity.
    private WeakReference<View> view;
    private ScheduleRepository repository;
    private UtilsPreferences preferences;

    public MainPresenter(View view, ScheduleRepository repository, UtilsPreferences preferences) {
        this.view = new WeakReference<>(view);
        this.repository = repository;
        this.preferences = preferences;
    }

    public void rebindView(View view) {
        this.view = new WeakReference<>(view);
        registerListeners();
    }

    public void start() {
        View view = this.view.get();
        if (view == null) return;
        registerListeners();
        if (!preferences.getScheduleWasPicked()) {
            view.showScheduleTypeOptions();
        } else {
            if (preferences.getGroupSchedule()) {
                view.showGroupSchedule();
            } else {
                view.showTeacherSchedule();
            }
        }
    }

    private void registerListeners() {
        view.get().setOnScheduleTypePickedListener(new OnScheduleTypePickedListener() {
            @Override
            public void onStudent() {
                View v = view.get();
                if (v == null) return;
                v.showStudentOptions();
            }

            @Override
            public void onTeacher() {
                View v = view.get();
                if (v == null) return;
                v.showTeacherOptions();
            }
        });
        view.get().setOnGroupPickedListener(new OnGroupPickedListener() {
            @Override
            public void onGroupPicked(Grade grade, Group group) {
                View v = view.get();
                if (v == null) return;
                preferences.setScheduleWasPicked(true);
                preferences.setScheduleOfStudent(true);
                preferences.setGradeId(grade.id);
                preferences.setGroupId(group.id);
                if ("NULL".equals(group.name)) {
                    preferences.setTitle("Группа " + group.gradeNum + "." + group.num);
                } else {
                    preferences.setTitle("Группа " + group.name + " " + group.gradeNum + "." + group.num);
                }
                v.showGroupSchedule();
            }
        });
        view.get().setOnTeacherPickedListener(new OnTeacherPickedListener() {
            @Override
            public void onTeacherPicked(Teacher teacher) {
                View v = view.get();
                if (v == null) return;
                preferences.setScheduleWasPicked(true);
                preferences.setScheduleOfStudent(false);
                preferences.setTeacherId(teacher.id);
                preferences.setTitle(teacher.name);
                v.showTeacherSchedule();
            }
        });
        view.get().setOnPickAnotherScheduleListener(new OnPickAnotherScheduleListener() {
            @Override
            public void onPickAnotherSchedule() {
                View v = view.get();
                if (v == null) return;
                preferences.setScheduleWasPicked(false);
                v.showScheduleTypeOptions();
            }
        });
    }

    public void getTeacherList(RequestListener<Teacher.List> listener) {
        repository.getTeachers(listener);
    }

    public void getGrades(RequestListener<Grade.List> listener) {
        repository.getGrades(listener);
    }

    public void getGroups(Grade grade, RequestListener<Group.List> listener) {
        repository.getGroups(grade.id, grade.num, listener);
    }

    public void getScheduleOfTeacher(int teacherId, RequestListener<TeacherSchedule> listener) {
        repository.getScheduleOfTeacher(teacherId, listener);
    }

    public void getScheduleOfGroup(int groupId, RequestListener<GroupSchedule> listener) {
        repository.getScheduleOfGroup(groupId, listener);
    }
}
