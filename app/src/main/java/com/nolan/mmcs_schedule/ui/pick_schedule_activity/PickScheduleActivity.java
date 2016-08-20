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
import com.nolan.mmcs_schedule.ui.schedule_activity.CustomArrayAdapter;
import com.nolan.mmcs_schedule.ui.schedule_activity.ScheduleActivity;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class PickScheduleActivity extends BaseActivity implements PickSchedulePresenter.View {
    private static class GradeAdapter extends CustomArrayAdapter<Grade> {
        @Override
        protected String str(Grade grade) {
            String result;
            switch (grade.degree) {
                case BACHELOR:   result = "Бакалавриат ";  break;
                case MASTER:     result = "Магистратура "; break;
                case SPECIALIST: result = "Специалитет ";  break;
                default:
                    throw new Error("unreachable statement");
            }
            result += grade.num;
            result += " курс";
            return result;
        }
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, PickScheduleActivity.class));
    }

    private static class GroupAdapter extends CustomArrayAdapter<Group> {
        @Override
        protected String str(Group group) {
            if ("NULL".equals(group.name)) {
                return "" + group.num + " группа";
            } else {
                return group.name + " " + group.num;
            }
        }
    }

    private static class TeacherAdapter extends CustomArrayAdapter<Teacher> {
        @Override
        protected String str(Teacher teacher) {
            return teacher.name;
        }
    }

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

    private int groupId;

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

        showStudentOptions();
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
        btnOk.setEnabled(false);

        onShowStudentOptions();
    }

    private void onShowStudentOptions() {
        showLoading();
        presenter.getGrades(new RequestListener<Grade.List>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(
                        PickScheduleActivity.this,
                        "Ошибка при загрузке списка курсов",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestSuccess(Grade.List grades) {
                gradeAdapter.setData(grades);
                Grade firstGrade = grades.get(0);
                presenter.getGroups(firstGrade.id, firstGrade.num, new RequestListener<Group.List>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        Toast.makeText(
                                PickScheduleActivity.this,
                                "Ошибка при загрузке списка групп",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRequestSuccess(Group.List groups) {
                        groupAdapter.setData(groups);
                        showStudentOptions();
                    }
                });
            }
        });
    }

    private void onShowTeacherOptions() {
        showLoading();
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

    private void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        spTeacher.setVisibility(View.GONE);
        spGrade.setVisibility(View.GONE);
        spGroup.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);
    }

    private void showStudentOptions() {
        pbLoading.setVisibility(View.GONE);
        spTeacher.setVisibility(View.GONE);
        spGrade.setVisibility(View.VISIBLE);
        spGroup.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
    }

    private void showTeacherOptions() {
        pbLoading.setVisibility(View.GONE);
        spTeacher.setVisibility(View.VISIBLE);
        spGrade.setVisibility(View.GONE);
        spGroup.setVisibility(View.GONE);
        btnOk.setVisibility(View.VISIBLE);
    }

    @Override
    public void startScheduleActivity() {
        ScheduleActivity.start(this);
        finish();
    }
}
