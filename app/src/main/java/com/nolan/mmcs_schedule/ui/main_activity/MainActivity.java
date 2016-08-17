package com.nolan.mmcs_schedule.ui.main_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nolan.mmcs_schedule.repository.api.ScheduleApi;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGrade;
import com.nolan.mmcs_schedule.ui.BaseActivity;
import com.nolan.mmcs_schedule.ui.PickScheduleTypeFragment;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class MainActivity extends BaseActivity {
    private ListFragment listFragment;

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
        listFragment = new ListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .add(android.R.id.content, listFragment)
                .commit();
    }

    private static class RequestGrades extends RetrofitSpiceRequest<RawGrade.List, ScheduleApi> {
        public RequestGrades() {
            super(RawGrade.List.class, ScheduleApi.class);
        }

        @Override
        public RawGrade.List loadDataFromNetwork() throws Exception {
            return getService().getGrades();
        }
    }

    private void showStudentOptions() {
        getSpiceManager()
                .execute(new RequestGrades(), new RequestListener<RawGrade.List>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {

                    }

                    @Override
                    public void onRequestSuccess(final RawGrade.List rawGrades) {
                        listFragment.setListAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return rawGrades.size();
                            }

                            @Override
                            public Object getItem(int i) {
                                return rawGrades.get(i);
                            }

                            @Override
                            public long getItemId(int i) {
                                return -1;
                            }

                            @Override
                            public View getView(int i, View view, ViewGroup viewGroup) {
                                if (view == null) {
                                    view = LayoutInflater.from(MainActivity.this)
                                            .inflate(android.R.layout.simple_list_item_1,
                                                    viewGroup, false);
                                }
                                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                tv.setText(Integer.toString(rawGrades.get(i).getNum()));
                                return view;
                            }
                        });
                    }
                });
    }

    private void showTeacherOptions() {
    }

}
