package com.nolan.mmcs_schedule.repository;

import com.nolan.mmcs_schedule.repository.api.ScheduleApi;
import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.repository.primitives.GroupSchedule;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.repository.primitives.TeacherSchedule;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class ScheduleRepository {

    private SpiceManager spiceManager;

    public ScheduleRepository(SpiceManager spiceManager) {
        this.spiceManager = spiceManager;
    }

    public void getGrades(final RequestListener<Grade.List> listener) {
        spiceManager.execute(new RetrofitSpiceRequest<Grade.List, ScheduleApi>(
                Grade.List.class, ScheduleApi.class) {
            @Override
            public Grade.List loadDataFromNetwork() throws Exception {
                return new Grade.List(getService().getGrades());
            }
        }, listener);
    }

    public void getGroups(final int gradeId, final int gradeNum, RequestListener<Group.List> listener) {
        spiceManager.execute(new RetrofitSpiceRequest<Group.List, ScheduleApi>(
                Group.List.class, ScheduleApi.class) {
            @Override
            public Group.List loadDataFromNetwork() throws Exception {
                return new Group.List(getService().getGroups(gradeId), gradeNum);
            }
        }, listener);
    }

    public void getTeachers(RequestListener<Teacher.List> listener) {
        spiceManager.execute(new RetrofitSpiceRequest<Teacher.List, ScheduleApi>(
                Teacher.List.class, ScheduleApi.class) {
            @Override
            public Teacher.List loadDataFromNetwork() throws Exception {
                Teacher.List result = new Teacher.List(getService().getTeachers());
                if (result.get(0).name.isEmpty()) {
                    result.remove(0);
                }
                return result;
            }
        }, listener);
    }

    public void getScheduleOfGroup(final int groupId, RequestListener<GroupSchedule> listener) {
        spiceManager.execute(new RetrofitSpiceRequest<GroupSchedule, ScheduleApi>(
                GroupSchedule.class, ScheduleApi.class) {
            @Override
            public GroupSchedule loadDataFromNetwork() throws Exception {
                return new GroupSchedule(getService().getScheduleOfGroup(groupId));
            }
        }, listener);
    }

    public void getScheduleOfTeacher(final int teacherId, RequestListener<TeacherSchedule> listener) {
        spiceManager.execute(new RetrofitSpiceRequest<TeacherSchedule, ScheduleApi>(
                TeacherSchedule.class, ScheduleApi.class) {
            @Override
            public TeacherSchedule loadDataFromNetwork() throws Exception {
                return new TeacherSchedule(getService().getScheduleOfTeacher(teacherId));
            }
        }, listener);
    }
}






