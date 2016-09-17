package com.nolan.mmcs_schedule.ui.initial_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nolan.mmcs_schedule.Injector;
import com.nolan.mmcs_schedule.ui.pick_schedule_activity.PickScheduleActivity;
import com.nolan.mmcs_schedule.ui.schedule_activity.ScheduleActivity;
import com.nolan.mmcs_schedule.utils.PrefUtils;

public class InitialActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefUtils preferences = Injector.injectPreferences();
        if (!preferences.getScheduleWasPicked()) {
            startActivity(new Intent(this, PickScheduleActivity.class));
        } else {
            boolean isShowingScheduleOfGroup = preferences.getPickedScheduleOfGroup();
            int id = isShowingScheduleOfGroup ? preferences.getGroupId() : preferences.getTeacherId();
            ScheduleActivity.start(this, true, isShowingScheduleOfGroup, id, preferences.getTitle());
        }
        finish();
    }
}
