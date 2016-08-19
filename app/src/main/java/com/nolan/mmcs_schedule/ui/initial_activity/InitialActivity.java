package com.nolan.mmcs_schedule.ui.initial_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nolan.mmcs_schedule.Injector;
import com.nolan.mmcs_schedule.ui.pick_schedule_activity.PickScheduleActivity;
import com.nolan.mmcs_schedule.ui.schedule_activity.ScheduleActivity;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;

public class InitialActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilsPreferences preferences = Injector.injectPreferences();
        if (!preferences.getScheduleWasPicked()) {
            startActivity(new Intent(this, PickScheduleActivity.class));
        } else {
            if (preferences.getPickedScheduleOfGroup()) {
                ScheduleActivity.start(this, false, preferences.getTeacherId());
            } else {
                ScheduleActivity.start(this, true, preferences.getGroupId());
            }
        }
        finish();
    }
}
