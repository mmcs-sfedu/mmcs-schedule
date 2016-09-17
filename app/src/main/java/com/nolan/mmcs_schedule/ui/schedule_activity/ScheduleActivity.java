package com.nolan.mmcs_schedule.ui.schedule_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.nolan.mmcs_schedule.ui.schedule_activity.lesson_dialog.LessonDetailsDialog;
import com.nolan.mmcs_schedule.ui.schedule_activity.lesson_dialog.LessonForGroupDetails;
import com.nolan.mmcs_schedule.ui.schedule_activity.lesson_dialog.LessonForTeacherDetails;
import com.nolan.mmcs_schedule.utils.PrefUtils;

import java.util.ArrayList;

public class ScheduleActivity extends BaseActivity
        implements SchedulePresenter.View, LessonDetailsDialog.Activity {
    private static final String KEY_IS_SAVING_WEEK_TYPE_OPTION = "key_persistent";
    private static final String KEY_IS_SHOWING_SCHEDULE_OF_GROUP = "key_schedule_of_group";
    private static final String KEY_ID = "key_id";
    private static final String KEY_TITLE = "key_title";

    private ProgressBar pbLoading;
    private ListView lvSchedule;

    private ScheduleAdapter adapter;

    private SchedulePresenter presenter;

    private DialogFragment detailsDialog;

    public static void start(Context context, boolean isSavingWeekTypeOption,
                             boolean scheduleOfGroup, int id, String title) {
        Intent intent = new Intent(context, ScheduleActivity.class);
        intent.putExtra(KEY_IS_SAVING_WEEK_TYPE_OPTION, isSavingWeekTypeOption);
        intent.putExtra(KEY_IS_SHOWING_SCHEDULE_OF_GROUP, scheduleOfGroup);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();

        boolean isSavingWeekTypeOption = intent.getBooleanExtra(KEY_IS_SAVING_WEEK_TYPE_OPTION, false);
        boolean isShowingScheduleOfGroup = intent.getBooleanExtra(KEY_IS_SHOWING_SCHEDULE_OF_GROUP, false);
        int id = intent.getIntExtra(KEY_ID, -1);
        String title = intent.getStringExtra(KEY_TITLE);

        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        lvSchedule = (ListView) findViewById(R.id.lv_schedule);

        PrefUtils preferences = Injector.injectPreferences();
        ScheduleRepository repository = Injector.injectRepository(this);

        presenter = new SchedulePresenter(
                this, isSavingWeekTypeOption, isShowingScheduleOfGroup, id,
                title, repository, preferences);

        adapter = new ScheduleAdapter(presenter);
        lvSchedule.setAdapter(adapter);
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setSubtitle(String subtitle) {
        getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void setSchedule(ArrayList<DaySchedule> schedule) {
        adapter.setData(schedule);
        showSchedule();
    }

    @Override
    public void showLessonDetails(int dayIndex, int lessonIndex, LessonForGroupDetails details) {
        detailsDialog = LessonDetailsDialog.create(dayIndex, lessonIndex, details);
        detailsDialog.show(getSupportFragmentManager(), "lesson-details");
    }

    @Override
    public void showLessonDetails(int dayIndex, int lessonIndex, LessonForTeacherDetails details) {
        detailsDialog = LessonDetailsDialog.create(dayIndex, lessonIndex, details);
        detailsDialog.show(getSupportFragmentManager(), "lesson-details");
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
        showLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem miChangeSchedule = menu.findItem(R.id.mi_change_schedule);
        if (!presenter.isChangeScheduleButtonVisible()) {
            miChangeSchedule.setVisible(false);
            miChangeSchedule.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_change_schedule:
                presenter.onPickAnotherSchedule();
                break;
            case R.id.mi_show_current:
                presenter.onWeekTypeOptionChanged(WeekTypeOption.CURRENT);
                break;
            case R.id.mi_show_full:
                presenter.onWeekTypeOptionChanged(WeekTypeOption.FULL);
                break;
            case R.id.mi_show_upper:
                presenter.onWeekTypeOptionChanged(WeekTypeOption.UPPER);
                break;
            case R.id.mi_show_lower:
                presenter.onWeekTypeOptionChanged(WeekTypeOption.LOWER);
                break;
            case R.id.mi_report_error:
                presenter.onReportError();
                break;
            case R.id.mi_week_type:
                break;
            default:
                throw new Error("unreachable statement");
        }
        return true;
    }

    @Override
    public void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        lvSchedule.setVisibility(View.GONE);
    }

    @Override
    public void showSchedule() {
        pbLoading.setVisibility(View.GONE);
        lvSchedule.setVisibility(View.VISIBLE);
    }

    @Override
    public void startReportErrorActivity(String subject, String text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "yadummer+schedule@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(emailIntent, "Отправить письмо..."));
    }

    @Override
    public void startPickScheduleActivity() {
        PickScheduleActivity.start(this);
        finish();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(ScheduleActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowSchedule(int dayIndex, int lessonIndex, int teacherOfGroupIndex) {
        presenter.onShowSchedule(dayIndex, lessonIndex, teacherOfGroupIndex);
    }

    @Override
    public void startScheduleActivity(boolean isScheduleOfGroup, int id, String title) {
        ScheduleActivity.start(this, false, isScheduleOfGroup, id, title);
    }
}

