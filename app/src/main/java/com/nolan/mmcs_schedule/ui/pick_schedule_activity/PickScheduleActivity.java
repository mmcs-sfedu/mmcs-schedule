package com.nolan.mmcs_schedule.ui.pick_schedule_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.nolan.mmcs_schedule.Injector;
import com.nolan.mmcs_schedule.R;
import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.ui.BaseActivity;
import com.nolan.mmcs_schedule.ui.schedule_activity.ScheduleActivity;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class PickScheduleActivity extends BaseActivity implements PickSchedulePresenter.View {
    private PickSchedulePresenter presenter;
    private UtilsPreferences preferences;

    private GradeAdapter gradeAdapter = new GradeAdapter();
    private GroupAdapter groupAdapter = new GroupAdapter();
    private TeacherAdapter teacherAdapter = new TeacherAdapter();

    private RadioGroup rgScheduleType;
    private ProgressBar pbLoading;
    private Spinner spTeacher;
    private Spinner spGrade;
    private Spinner spGroup;
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

        setContentView(R.layout.activity_pick_schedule);

        rgScheduleType = (RadioGroup) findViewById(R.id.rg_schedule_type);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        spTeacher = (Spinner) findViewById(R.id.sp_teacher);
        spGrade = (Spinner) findViewById(R.id.sp_grade);
        spGroup = (Spinner) findViewById(R.id.sp_group);
        btnOk = (Button) findViewById(R.id.btn_ok);

        spTeacher.setAdapter(teacherAdapter);
        spGrade.setAdapter(gradeAdapter);
        spGroup.setAdapter(groupAdapter);

        showGradeOption();
    }

    @Override
    protected void onStart() {
        super.onStart();

        rgScheduleType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.rb_student: onShowStudentOptions(); break;
                    case R.id.rb_teacher: onShowTeacherOptions(); break;
                    default:
                        throw new Error("unreachable statement");
                }
            }
        });

        spGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                showGradeOption();
                Grade grade = gradeAdapter.getItem(i);
                presenter.getGroups(grade.id, grade.num, new RequestListener<Group.List>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        Toast.makeText(
                                PickScheduleActivity.this,
                                "Ошибка при загрузке списка групп",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRequestSuccess(Group.List groups) {
                        showGroupOption();
                        groupAdapter.setData(groups);
                        spGroup.setSelection(0);
                        spGroup.getOnItemSelectedListener()
                                .onItemSelected(spGroup, spGroup.getChildAt(0), 0, 0);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        spGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onPickGroup(groupAdapter.getItem(i));
                btnOk.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        spTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onPickTeacher(teacherAdapter.getItem(i));
                btnOk.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onOk();
            }
        });

        switch (rgScheduleType.getCheckedRadioButtonId()) {
            case R.id.rb_student: onShowStudentOptions(); break;
            case R.id.rb_teacher: onShowTeacherOptions(); break;
        }
    }

    private void onShowStudentOptions() {
        showTotalLoading();
        presenter.getGrades(new RequestListener<Grade.List>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(
                        PickScheduleActivity.this,
                        "Ошибка при загрузке списка курсов",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestSuccess(final Grade.List grades) {
                showGradeOption();
                gradeAdapter.setData(grades);
                spGrade.setSelection(0);
                spGrade.getOnItemSelectedListener()
                        .onItemSelected(spGrade, spGrade.getChildAt(0), 0, 0);
            }
        });
    }

    private void onShowTeacherOptions() {
        showTotalLoading();
        presenter.getTeachers(new RequestListener<Teacher.List>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(
                        PickScheduleActivity.this,
                        "Ошибка при загрузке списка преподавателей",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestSuccess(Teacher.List teachers) {
                teacherAdapter.setData(teachers);
                showTeacherOptions();
            }
        });
    }

    private void showTotalLoading() {
        spTeacher.setVisibility(View.GONE);
        spGrade.setVisibility(View.GONE);
        spGroup.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
        btnOk.setEnabled(false);
    }

    private void showGradeOption() {
        spTeacher.setVisibility(View.GONE);
        spGrade.setVisibility(View.VISIBLE);
        spGroup.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
        btnOk.setEnabled(false);
    }

    private void showGroupOption() {
        spTeacher.setVisibility(View.GONE);
        spGrade.setVisibility(View.VISIBLE);
        spGroup.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
    }

    private void showTeacherOptions() {
        spTeacher.setVisibility(View.VISIBLE);
        spGrade.setVisibility(View.GONE);
        spGroup.setVisibility(View.GONE);
        btnOk.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void startScheduleActivity() {
        ScheduleActivity.start(this);
        finish();
    }
}
