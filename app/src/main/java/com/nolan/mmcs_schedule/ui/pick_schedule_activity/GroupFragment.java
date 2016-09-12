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

public class GroupFragment extends Fragment {
    public interface Activity {
        void onPickedGrade(int gradePosition);
        void onPickedGroup(int groupPosition);
    }

    private Activity activity;

    private Spinner spGrade;
    private Spinner spGroup;
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
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spGrade = (Spinner) view.findViewById(R.id.sp_grade);
        spGroup = (Spinner) view.findViewById(R.id.sp_group);
        pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);

        spGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity.onPickedGrade(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        spGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity.onPickedGroup(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    // Actual class doesn't matter as array adapter will use Object.toString();
    // Moreover MVP view becomes completely independent of business logic.
    public void setGrades(List<Object> grades) {
        spGrade.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, grades));
        showLoadingGroups();
    }

    public void setGroups(List<Object> groups) {
        spGroup.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, groups));
        showLoadingDone();
    }

    private void show(boolean grade, boolean group, boolean loading) {
        spGrade.setVisibility(grade ? View.VISIBLE : View.GONE);
        spGroup.setVisibility(group ? View.VISIBLE : View.GONE);
        pbLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void showTotalLoading()  { show(false, false, true);  }
    private void showLoadingGroups() { show(true,  false, true);  }
    private void showLoadingDone()   { show(true,  true,  false); }

    public void reset() { showTotalLoading(); }

}
