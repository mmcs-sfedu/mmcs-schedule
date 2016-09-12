package com.nolan.mmcs_schedule.ui.pick_schedule_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.nolan.mmcs_schedule.R;

import java.util.List;

public class TeacherFragment extends Fragment {
    public interface Activity {
        void onPickedTeacher(int teacherPosition);
    }

    private Activity activity;

    private Spinner spTeacher;
    private ProgressBar pbLoading;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teacher, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spTeacher = (Spinner) view.findViewById(R.id.sp_teacher);
        pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);

        spTeacher.setVisibility(View.GONE);

        spTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity.onPickedTeacher(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    public void setTeachers(List<Object> teachers) {
        spTeacher.setAdapter(
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, teachers));

        spTeacher.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
    }

    public void reset() {
        spTeacher.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }
}
