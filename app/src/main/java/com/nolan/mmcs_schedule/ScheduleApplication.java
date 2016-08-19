package com.nolan.mmcs_schedule;

import android.app.Application;

public class ScheduleApplication extends Application{

    private static ScheduleApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static ScheduleApplication get() {
        return context;
    }
}
