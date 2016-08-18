package com.nolan.mmcs_schedule.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.nolan.mmcs_schedule.ScheduleApplication;

/**
 * Class that presents access to shared preferences.
 */
public class UtilsPreferences {

    private static SharedPreferences getPreferences() {
        return ScheduleApplication.get().getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    /**
     * Has user made choice of schedule?
     */
    private static final String KEY_SCHEDULE_WAS_PICKED = "schedule-was-picked";
    public boolean getScheduleWasPicked() {
        return getPreferences().getBoolean(KEY_SCHEDULE_WAS_PICKED, false);
    }
    public void setScheduleWasPicked(boolean scheduleWasPicked) {
        getPreferences()
                .edit()
                .putBoolean(KEY_SCHEDULE_WAS_PICKED, scheduleWasPicked)
                .apply();
    }

    /**
     * Was schedule of student picked or schedule of teacher?
     */
    private static final String KEY_SCHEDULE_OF_STUDENT = "schedule-of-student";
    public boolean getGroupSchedule() {
        return getPreferences().getBoolean(KEY_SCHEDULE_OF_STUDENT, true);
    }
    public void setScheduleOfStudent(boolean scheduleOfStudent) {
        getPreferences()
                .edit()
                .putBoolean(KEY_SCHEDULE_OF_STUDENT, scheduleOfStudent)
                .apply();
    }

    /**
     * Id of the grade that user picked. This value must be correct if getGroupSchedule()
     * returns true.
     */
    private static final String KEY_GRADE_ID = "group-id";
    public long getGradeId() {
        return getPreferences().getLong(KEY_GRADE_ID, -1);
    }
    public void setGradeId(long id) {
        getPreferences()
                .edit()
                .putLong(KEY_GRADE_ID, id)
                .apply();
    }

    /**
     * Id of the group that user picked. This value must be correct if getGroupSchedule()
     * returns true.
     */
    private static final String KEY_GROUP_ID = "group-id";
    public long getGroupId() {
        return getPreferences().getLong(KEY_GROUP_ID, -1);
    }
    public void setGroupId(long id) {
        getPreferences()
                .edit()
                .putLong(KEY_GROUP_ID, id)
                .apply();
    }

    /**
     * Id of the teacher that user picked. This value must be correct if getGroupSchedule()
     * returns false.
     */
    private static final String KEY_TEACHER_ID = "teacher-id";
    public long getTeacherId() {
        return getPreferences().getLong(KEY_TEACHER_ID, -1);
    }
    public void setTeacherId(long id) {
        getPreferences()
                .edit()
                .putLong(KEY_TEACHER_ID, id)
                .apply();
    }

}
