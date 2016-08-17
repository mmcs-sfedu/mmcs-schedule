package com.nolan.mmcs_schedule.ui;

import android.support.v7.app.AppCompatActivity;

import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.repository.api.RetrofitSpiceService;
import com.octo.android.robospice.SpiceManager;

public class BaseActivity extends AppCompatActivity {
    private SpiceManager spiceManager = new SpiceManager(RetrofitSpiceService.class);
    private ScheduleRepository scheduleRepository = new ScheduleRepository(spiceManager);

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        spiceManager.shouldStop();
    }

    public ScheduleRepository getScheduleRepository() {
        return scheduleRepository;
    }

}
