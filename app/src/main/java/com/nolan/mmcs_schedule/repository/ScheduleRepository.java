package com.nolan.mmcs_schedule.repository;

import com.nolan.mmcs_schedule.repository.api.ScheduleApi;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGrade;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGroup;
import com.nolan.mmcs_schedule.repository.api.primitives.RawTeacher;
import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.repository.primitives.GroupSchedule;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.repository.primitives.TeacherSchedule;
import com.nolan.mmcs_schedule.repository.primitives.WeekType;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class ScheduleRepository {

    private static final long CACHE_EXPIRY_DURATION = DurationInMillis.ONE_DAY;

    private SpiceManager spiceManager;

    public ScheduleRepository(SpiceManager spiceManager) {
        this.spiceManager = spiceManager;
    }

    private static class GradesRequest extends RetrofitSpiceRequest<Grade.List, ScheduleApi> {
        public GradesRequest() {
            super(Grade.List.class, ScheduleApi.class);
        }

        @Override
        public Grade.List loadDataFromNetwork() throws Exception {
            RawGrade.List rawGrades = getService().getGrades();
            Grade.List grades = new Grade.List();
            grades.ensureCapacity(rawGrades.size());
            for (RawGrade rawGrade : rawGrades) {
                grades.add(new Grade(rawGrade));
            }
            return grades;
        }
    }

    public void getGrades(RequestListener<Grade.List> listener) {
        spiceManager.execute(new GradesRequest(), "getGrades()", CACHE_EXPIRY_DURATION, listener);
    }

    private static class GroupsRequest extends RetrofitSpiceRequest<Group.List, ScheduleApi> {
        private int gradeId;
        private int gradeNum;

        public GroupsRequest(int gradeId, int gradeNum) {
            super(Group.List.class, ScheduleApi.class);
            this.gradeId = gradeId;
            this.gradeNum = gradeNum;
        }

        @Override
        public Group.List loadDataFromNetwork() throws Exception {
            RawGroup.List rawGroups = getService().getGroups(gradeId);
            Group.List groups = new Group.List();
            groups.ensureCapacity(rawGroups.size());
            for (RawGroup rawGroup : rawGroups) {
                groups.add(new Group(rawGroup, gradeNum));
            }
            return groups;
        }
    }

    public void getGroups(int gradeId, int gradeNum, RequestListener<Group.List> listener) {
        spiceManager.execute(new GroupsRequest(gradeId, gradeNum),
                "getGroups(" + gradeId + ")", CACHE_EXPIRY_DURATION, listener);
    }

    private static class TeachersRequest extends RetrofitSpiceRequest<Teacher.List, ScheduleApi> {

        public TeachersRequest() {
            super(Teacher.List.class, ScheduleApi.class);
        }

        @Override
        public Teacher.List loadDataFromNetwork() throws Exception {
            RawTeacher.List rawTeachers = getService().getTeachers();
            Teacher.List teachers = new Teacher.List();
            teachers.ensureCapacity(rawTeachers.size());
            for (RawTeacher rawTeacher : rawTeachers) {
                teachers.add(new Teacher(rawTeacher));
            }
            return teachers;
        }
    }

    public void getTeachers(RequestListener<Teacher.List> listener) {
        spiceManager.execute(new TeachersRequest(), "getTeachers", CACHE_EXPIRY_DURATION, listener);
    }

    private static class ScheduleOfGroupRequest extends RetrofitSpiceRequest<GroupSchedule, ScheduleApi> {
        private int groupId;

        public ScheduleOfGroupRequest(int groupId) {
            super(GroupSchedule.class, ScheduleApi.class);
            this.groupId = groupId;
        }

        @Override
        public GroupSchedule loadDataFromNetwork() throws Exception {
            return new GroupSchedule(getService().getScheduleOfGroup(groupId));
        }
    }

    public void getScheduleOfGroup(final int groupId, RequestListener<GroupSchedule> listener) {
        spiceManager.execute(new ScheduleOfGroupRequest(groupId), "getScheduleOfGroup(" + groupId + ")", CACHE_EXPIRY_DURATION, listener);
    }

    private static class ScheduleOfTeacherRequest extends RetrofitSpiceRequest<TeacherSchedule, ScheduleApi> {
        private int teacherId;

        public ScheduleOfTeacherRequest(int teacherId) {
            super(TeacherSchedule.class, ScheduleApi.class);
            this.teacherId = teacherId;
        }

        @Override
        public TeacherSchedule loadDataFromNetwork() throws Exception {
            return new TeacherSchedule(getService().getScheduleOfTeacher(teacherId));
        }
    }

    public void getScheduleOfTeacher(int teacherId, RequestListener<TeacherSchedule> listener) {
        spiceManager.execute(new ScheduleOfTeacherRequest(teacherId), "getScheduleOfTeacher(" + teacherId + ")", CACHE_EXPIRY_DURATION, listener);
    }

    private static class WeekTypeRequest extends RetrofitSpiceRequest<WeekType, ScheduleApi> {
        public WeekTypeRequest() {
            super(WeekType.class, ScheduleApi.class);
        }

        @Override
        public WeekType loadDataFromNetwork() throws Exception {
            return WeekType.convert(getService().getCurrentWeek());
        }
    }

    public void getCurrentWeekType(RequestListener<WeekType> listener) {
        spiceManager.execute(new WeekTypeRequest(), "getCurrentWeekType()", CACHE_EXPIRY_DURATION, listener);
    }
}






