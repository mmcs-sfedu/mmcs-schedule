package com.nolan.mmcs_schedule.ui.main_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nolan.mmcs_schedule.ui.PickScheduleTypeFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                .add(android.R.id.content, fragment)
                .commit();
    }

    private void showStudentOptions() {
    }

    private void showTeacherOptions() {
    }

}
