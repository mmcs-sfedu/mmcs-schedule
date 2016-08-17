package com.nolan.mmcs_schedule.repository;

import com.nolan.mmcs_schedule.repository.api.ScheduleApi;
import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.repository.primitives.GroupSchedule;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.repository.primitives.TeacherSchedule;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import java.util.ArrayList;

public class ScheduleRepository {

    public interface ResponseListener<T> {
        void onResponse(T response);
        void onError(Throwable error);
    }

    private SpiceManager spiceManager;

    private ScheduleRepository(SpiceManager spiceManager) {
        this.spiceManager = spiceManager;
    }

    public void getGrades(final ResponseListener<Grade.List> listener) {
        spiceManager.execute(new RetrofitSpiceRequest<Grade.List, ScheduleApi>(
                Grade.List.class, ScheduleApi.class) {
            @Override
            public Grade.List loadDataFromNetwork() throws Exception {
                return new Grade.List(getService().getGrades());
            }
        }, new RequestListener<Grade.List>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                listener.onError(spiceException);
            }

            @Override
            public void onRequestSuccess(Grade.List grades) {
                listener.onResponse(grades);
            }
        });
    }

    public void getGroups(Grade grade, ResponseListener<ArrayList<Group>> lisneter) {
        // todo: implement
    }

    public void getTeachers(ResponseListener<ArrayList<Teacher>> listener) {
        // todo: implement
    }

    public void getSchedule(Group group, ResponseListener<ArrayList<GroupSchedule>> listener) {
        // todo: implement
    }

    public void getSchedule(Teacher teacher, ResponseListener<ArrayList<TeacherSchedule>> listener) {
        // todo: implement
    }
}






