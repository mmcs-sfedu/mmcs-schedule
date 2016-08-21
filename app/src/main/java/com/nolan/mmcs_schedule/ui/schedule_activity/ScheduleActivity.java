package com.nolan.mmcs_schedule.ui.schedule_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nolan.mmcs_schedule.Injector;
import com.nolan.mmcs_schedule.R;
import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.ui.BaseActivity;
import com.nolan.mmcs_schedule.ui.pick_schedule_activity.PickScheduleActivity;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ScheduleActivity extends BaseActivity implements SchedulePresenter.View {
    private ProgressBar pbLoading;
    private ListView lvSchedule;

    private ScheduleAdapter adapter = new ScheduleAdapter();

    private SchedulePresenter presenter;
    private UtilsPreferences preferences;

    private boolean pickedScheduleOfGroup;
    private int scheduleId;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ScheduleActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        lvSchedule = (ListView) findViewById(R.id.lv_schedule);

        lvSchedule.setAdapter(adapter);

        preferences = Injector.injectPreferences();
        ScheduleRepository repository = Injector.injectRepository(this);

        pickedScheduleOfGroup = preferences.getPickedScheduleOfGroup();
        scheduleId = pickedScheduleOfGroup ? preferences.getGroupId() : preferences.getTeacherId();

        presenter = new SchedulePresenter(this, repository, preferences);

        setTitle(preferences.getTitle());
    }

    @Override
    protected void onStart() {
        super.onStart();

        showLoading();
        presenter.getSchedule(pickedScheduleOfGroup, scheduleId,
                new RequestListener<DaySchedule.List>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        Toast.makeText(
                                ScheduleActivity.this,
                                "Ошибка при загрузке расписания",
                                Toast.LENGTH_LONG).show();
                        preferences.setScheduleWasPicked(false);
                    }

                    @Override
                    public void onRequestSuccess(DaySchedule.List schedule) {
                        adapter.setData(schedule);
                        showSchedule();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.mi_another_schedule)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        presenter.onPickAnotherSchedule();
                        return true;
                    }
                });
        return true;
    }

    private void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        lvSchedule.setVisibility(View.GONE);
    }

    private void showSchedule() {
        pbLoading.setVisibility(View.GONE);
        lvSchedule.setVisibility(View.VISIBLE);

    }

    @Override
    public void startPickScheduleActivity() {
        PickScheduleActivity.start(this);
        finish();
    }
}