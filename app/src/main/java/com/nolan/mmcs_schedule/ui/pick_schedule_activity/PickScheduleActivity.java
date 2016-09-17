package com.nolan.mmcs_schedule.ui.pick_schedule_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nolan.mmcs_schedule.Injector;
import com.nolan.mmcs_schedule.R;
import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.ui.BaseActivity;
import com.nolan.mmcs_schedule.ui.schedule_activity.ScheduleActivity;
import com.nolan.mmcs_schedule.utils.PrefUtils;

import java.util.List;

public class PickScheduleActivity extends BaseActivity implements PickSchedulePresenter.View, GroupFragment.Activity, TeacherFragment.Activity {
    private PickSchedulePresenter presenter;
    private PrefUtils preferences;

    private RadioGroup rgScheduleType;
    private GroupFragment groupFragment;
    private TeacherFragment teacherFragment;
    private Button btnOk;

    public static void start(Context context) {
        context.startActivity(new Intent(context, PickScheduleActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_schedule);

        ScheduleRepository repository = Injector.injectRepository(this);
        preferences = Injector.injectPreferences();
        presenter = new PickSchedulePresenter(this, repository, preferences);

        rgScheduleType = (RadioGroup) findViewById(R.id.rg_schedule_type);
        groupFragment = (GroupFragment) getSupportFragmentManager().findFragmentById(R.id.f_group);
        teacherFragment = (TeacherFragment) getSupportFragmentManager().findFragmentById(R.id.f_teacher);
        btnOk = (Button) findViewById(R.id.btn_ok);
    }

    @Override
    protected void onStart() {
        super.onStart();

        rgScheduleType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                onShowOptions(id == R.id.rb_group);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onOk();
            }
        });

        // For some         reason next code is required because there's no item selected
        // message on start.
        onShowOptions(rgScheduleType.getCheckedRadioButtonId() == R.id.rb_group);
    }

    private void onShowOptions(boolean forGroup) {
        if (forGroup) {
            groupFragment.getView().setVisibility(View.VISIBLE);
            groupFragment.reset();
            teacherFragment.getView().setVisibility(View.GONE);
        } else {
            groupFragment.getView().setVisibility(View.GONE);
            teacherFragment.getView().setVisibility(View.VISIBLE);
            teacherFragment.reset();
        }
        btnOk.setVisibility(View.GONE);
        presenter.onShowOptions(forGroup);
    }

    @Override
    public void setTeachers(List teachers) {
        teacherFragment.setTeachers(teachers);
    }

    @Override
    public void setGroups(List groups) {
        groupFragment.setGroups(groups);
    }

    @Override
    public void setGrades(List grades) {
        groupFragment.setGrades(grades);
    }

    @Override
    public void showError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPickedTeacher(int teacherPosition) {
        presenter.onPickTeacher(teacherPosition);
        btnOk.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPickedGrade(int gradePosition) {
        presenter.onPickGrade(gradePosition);
    }

    @Override
    public void onPickedGroup(int groupPosition) {
        presenter.onPickGroup(groupPosition);
        btnOk.setVisibility(View.VISIBLE);
    }

    @Override
    public void startScheduleActivity(boolean scheduleOfGroup, int id, String title) {
        ScheduleActivity.start(this, true, scheduleOfGroup, id, title);
        finish();
    }

}

