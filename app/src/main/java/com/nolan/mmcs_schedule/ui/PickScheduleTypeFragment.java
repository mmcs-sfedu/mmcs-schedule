package com.nolan.mmcs_schedule.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.nolan.mmcs_schedule.R;

public class PickScheduleTypeFragment extends Fragment {

    public interface OnScheduleTypePickedListener {
        void onStudent();
        void onTeacher();
    }

    @Nullable
    private OnScheduleTypePickedListener scheduleTypePickedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return registerListeners(
                inflater.inflate(R.layout.fragment_pick_schedule_type, container, false));
    }

    private View registerListeners(View view) {
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (scheduleTypePickedListener == null)
                    return;
                switch (radioGroup.getId()) {
                    case R.id.rb_student:
                        scheduleTypePickedListener.onStudent();
                        break;
                    case R.id.rb_teacher:
                        scheduleTypePickedListener.onTeacher();
                        break;
                }
            }
        });
        return view;
    }

    public void setOnScheduleTypePickedListener(@NonNull OnScheduleTypePickedListener listener) {
        scheduleTypePickedListener = listener;
    }
}
