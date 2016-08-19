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
    public int getGradeId() {
        return getPreferences().getInt(KEY_GRADE_ID, -1);
    }
    public void setGradeId(int id) {
        getPreferences()
                .edit()
                .putInt(KEY_GRADE_ID, id)
                .apply();
    }

    /**
     * Id of the group that user picked. This value must be correct if getGroupSchedule()
     * returns true.
     */
    private static final String KEY_GROUP_ID = "group-id";
    public int getGroupId() {
        return getPreferences().getInt(KEY_GROUP_ID, -1);
    }
    public void setGroupId(int id) {
        getPreferences()
                .edit()
                .putInt(KEY_GROUP_ID, id)
                .apply();
    }

    /**
     * Id of the teacher that user picked. This value must be correct if getGroupSchedule()
     * returns false.
     */
    private static final String KEY_TEACHER_ID = "teacher-id";
    public int getTeacherId() {
        return getPreferences().getInt(KEY_TEACHER_ID, -1);
    }
    public void setTeacherId(int id) {
        getPreferences()
                .edit()
                .putInt(KEY_TEACHER_ID, id)
                .apply();
    }

    /**
     * String that will be shown in action bar. It should contain name of the teacher
     * or group identifier.
     */
    private static final String KEY_TITLE = "title";
    public String getTitle() {
        return getPreferences().getString(KEY_TITLE, "");
    }
    public void setTitle(String title) {
        getPreferences()
                .edit()
                .putString(KEY_TITLE, title)
                .apply();
    }

}
