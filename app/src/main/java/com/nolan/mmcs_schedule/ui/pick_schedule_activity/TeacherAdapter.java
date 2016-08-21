package com.nolan.mmcs_schedule.ui.pick_schedule_activity;

import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.ui.schedule_activity.CustomArrayAdapter;

public class TeacherAdapter extends CustomArrayAdapter<Teacher> {
    @Override
    protected String str(Teacher teacher) {
        return teacher.name;
    }
}
