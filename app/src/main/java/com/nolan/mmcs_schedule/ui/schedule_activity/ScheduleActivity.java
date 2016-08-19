package com.nolan.mmcs_schedule.ui.schedule_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.nolan.mmcs_schedule.Injector;
import com.nolan.mmcs_schedule.R;
import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.repository.primitives.GroupLesson;
import com.nolan.mmcs_schedule.repository.primitives.GroupSchedule;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.repository.primitives.TeacherLesson;
import com.nolan.mmcs_schedule.repository.primitives.TeacherSchedule;
import com.nolan.mmcs_schedule.repository.primitives.WeekType;
import com.nolan.mmcs_schedule.ui.BaseActivity;
import com.nolan.mmcs_schedule.ui.pick_schedule_activity.PickScheduleActivity;
import com.nolan.mmcs_schedule.ui.pick_schedule_activity.PickSchedulePresenter;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

public class ScheduleActivity extends BaseActivity implements SchedulePresenter.View {
    private ProgressBar pbLoading;
    private ListView lvSchedule;

    private ScheduleAdapter adapter = new ScheduleAdapter();

    private SchedulePresenter presenter;
    private UtilsPreferences preferences;

    private static final String KEY_SCHEDULE_OF_TEACHER = "schedule_of_teacher";
    private boolean scheduleOfTeacher;
    private static final String KEY_SCHEDULE_ID = "schedule_id";
    private int scheduleId;

    public static void start(Context context, boolean scheduleOfTeacher, int scheduleId) {
        Intent intent = new Intent(context, ScheduleActivity.class);
        intent.putExtra(KEY_SCHEDULE_OF_TEACHER, scheduleOfTeacher);
        intent.putExtra(KEY_SCHEDULE_ID, scheduleId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduleOfTeacher = getIntent().getBooleanExtra(KEY_SCHEDULE_OF_TEACHER, false);
        scheduleId = getIntent().getIntExtra(KEY_SCHEDULE_ID, -1);

        setContentView(R.layout.activity_main);

        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        lvSchedule = (ListView) findViewById(R.id.lv_schedule);

        lvSchedule.setAdapter(adapter);

        preferences = Injector.injectPreferences();
        ScheduleRepository repository = Injector.injectRepository(this);

        presenter = new SchedulePresenter(this, repository, preferences);

        showLoading();
        presenter.getSchedule(scheduleOfTeacher, scheduleId,
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
