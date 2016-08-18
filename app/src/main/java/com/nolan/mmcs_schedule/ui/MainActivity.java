package com.nolan.mmcs_schedule.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.nolan.mmcs_schedule.R;
import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends BaseActivity implements MainPresenter.View {
    private LinearLayout llContent;
    private MainPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llContent = (LinearLayout) findViewById(R.id.content);
        presenter = new MainPresenter(this, getScheduleRepository(), new UtilsPreferences());
        presenter.start();
    }

    private MainPresenter.OnScheduleTypePickedListener onScheduleTypePickedListener;
    private MainPresenter.OnGroupPickedListener onGroupPickedListener;
    private MainPresenter.OnTeacherPickedListener onTeacherPickedListener;

    @Override
    public void setOnScheduleTypePickedListener(MainPresenter.OnScheduleTypePickedListener listener) {
        onScheduleTypePickedListener = listener;
    }

    @Override
    public void setOnGroupPickedListener(MainPresenter.OnGroupPickedListener listener) {
        onGroupPickedListener = listener;
    }

    @Override
    public void setOnTeacherPickedListener(MainPresenter.OnTeacherPickedListener listener) {
        onTeacherPickedListener = listener;
    }

    @Override
    public void showScheduleTypeOptions() {
        llContent.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        RadioGroup view = (RadioGroup) inflater.inflate(R.layout.pick_schedule_type, llContent, false);
        llContent.addView(view);
        view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                case R.id.rb_student:
                    onScheduleTypePickedListener.onStudent();
                    break;
                case R.id.rb_teacher:
                    onScheduleTypePickedListener.onTeacher();
                    break;
                default:
                    throw new Error("unreachable statement");
                }
            }
        });
        onScheduleTypePickedListener.onStudent();
    }

    private static class GradeAdapter extends CustomArrayAdapter<Grade> {
        @Override
        protected long id(Grade grade) {
            return grade.id;
        }

        @Override
        protected String str(Grade grade) {
            String result;
            switch (grade.degree) {
                case BACHELOR:   result = "Бакалавры ";   break;
                case MASTER:     result = "Магистры ";    break;
                case SPECIALIST: result = "Специалисты "; break;
                default:
                    throw new Error("unreachable statement");
            }
            result += grade.num;
            result += " курс";
            return result;
        }
    }

    private static class GroupAdapter extends CustomArrayAdapter<Group> {
        @Override
        protected long id(Group group) {
            return group.id;
        }

        @Override
        protected String str(Group group) {
            if ("NULL".equals(group.name)) {
                return group.num + " группа";
            } else {
                return group.name + " " + group.num + " группа";
            }
        }
    }

    private Grade pickedGrade;
    private Group pickedGroup;

    private GradeAdapter gradeAdapter = new GradeAdapter();
    private GroupAdapter groupAdapter = new GroupAdapter();

    @Override
    public void showStudentOptions() {
        if (llContent.getChildCount() > 1) {
            llContent.removeViewAt(1);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.student_options, llContent, false);
        llContent.addView(view);
        Spinner spGrade = (Spinner) view.findViewById(R.id.sp_grade);
        Spinner spGroup = (Spinner) view.findViewById(R.id.sp_group);
        final Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        spGrade.setAdapter(gradeAdapter);
        spGroup.setAdapter(groupAdapter);
        spGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pickedGrade = (Grade) adapterView.getItemAtPosition(i);
                presenter.getGroups(pickedGrade, new RequestListener<Group.List>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        Toast.makeText(MainActivity.this,
                                "Ошибка при загрузке списка групп",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRequestSuccess(Group.List groups) {
                        groupAdapter.setData(groups);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        spGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pickedGroup = (Group) adapterView.getItemAtPosition(i);
                btnOk.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGroupPickedListener.onGroupPicked(pickedGrade, pickedGroup);
            }
        });
        presenter.getGrades(new RequestListener<Grade.List>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(MainActivity.this,
                        "Ошибка при загрузке списка курсов",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestSuccess(Grade.List grades) {
                gradeAdapter.setData(grades);
            }
        });
    }

    private static class TeacherAdapter extends CustomArrayAdapter<Teacher> {
        @Override
        protected long id(Teacher teacher) {
            return teacher.id;
        }

        @Override
        protected String str(Teacher teacher) {
            return teacher.name;
        }
    }

    private Teacher pickedTeacher;

    private TeacherAdapter teacherAdapter = new TeacherAdapter();

    @Override
    public void showTeacherOptions() {
        if (llContent.getChildCount() > 1) {
            llContent.removeViewAt(1);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.teacher_options, llContent, false);
        llContent.addView(view);
        Spinner spTeachers = (Spinner) view.findViewById(R.id.sp_teacher);
        final Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        spTeachers.setAdapter(teacherAdapter);
        spTeachers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pickedTeacher = (Teacher) adapterView.getItemAtPosition(i);
                btnOk.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTeacherPickedListener.onTeacherPicked(pickedTeacher);
            }
        });
        presenter.getTeacherList(new RequestListener<Teacher.List>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(MainActivity.this,
                        "Ошибка при загрузке списка курсов",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestSuccess(Teacher.List teachers) {
                teacherAdapter.setData(teachers);
            }
        });
    }

    @Override
    public void showGroupSchedule() {
        Toast.makeText(MainActivity.this,
                "showGroupSchedule()",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showTeacherSchedule() {
        Toast.makeText(MainActivity.this,
                "showTeacherSchedule()",
                Toast.LENGTH_LONG).show();
    }
}
