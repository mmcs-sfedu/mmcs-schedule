package com.nolan.mmcs_schedule.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.nolan.mmcs_schedule.R;
import com.nolan.mmcs_schedule.repository.primitives.Teacher;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment firstFragment = getSupportFragmentManager().findFragmentById(R.id.first_fragment);
        if (firstFragment == null) {
            PickScheduleTypeFragment fragment = new PickScheduleTypeFragment();
            fragment.setOnScheduleTypePickedListener(
                    new PickScheduleTypeFragment.OnScheduleTypePickedListener() {
                        @Override
                        public void onStudent() {
                            showStudentOptions();
                        }

                        @Override
                        public void onTeacher() {
                            showTeacherOptions();
                        }
                    });
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.first_fragment, fragment)
                    .commit();
            showStudentOptions();
        }
    }

    private void showStudentOptions() {
        Toast.makeText(this, "work in progress", Toast.LENGTH_SHORT).show();
    }

    private PickTeacherFragment createPickTeacherFragment() {
        PickTeacherFragment fragment = new PickTeacherFragment();
        fragment.setTeacherListener(new PickTeacherFragment.TeacherListener() {
            @Override
            public void onTeacherPicked(Teacher teacher) {
                Toast.makeText(MainActivity.this, teacher.name, Toast.LENGTH_LONG).show();
            }
        });
        return fragment;
    }

    private void showTeacherOptions() {
        Fragment secondFragment = getSupportFragmentManager().findFragmentById(R.id.second_fragment);
        if (secondFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.second_fragment, createPickTeacherFragment())
                    .commit();
        } else {
            if (!(secondFragment instanceof PickTeacherFragment)) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.second_fragment, createPickTeacherFragment())
                        .commit();
            }
        }
        getScheduleRepository()
                .getTeachers(new RequestListener<Teacher.List>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        Toast.makeText(MainActivity.this,
                                "Error occurred while loading list of teacher.\n"
                                        + spiceException.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRequestSuccess(Teacher.List teachers) {
                        showTeachersList(teachers);
                    }
                });
    }

    private void showTeachersList(Teacher.List teacher) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.second_fragment);
        if (!(fragment instanceof PickTeacherFragment)) return;
        PickTeacherFragment pickTeacherFragment = (PickTeacherFragment) fragment;
        pickTeacherFragment.setTeacherList(teacher);
    }
}
