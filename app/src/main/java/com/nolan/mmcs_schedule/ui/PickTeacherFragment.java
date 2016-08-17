package com.nolan.mmcs_schedule.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nolan.mmcs_schedule.repository.primitives.Teacher;

public class PickTeacherFragment extends ListFragment {
    private static class Adapter extends BaseAdapter {
        private Teacher.List data;

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return data.get(i).id;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView tv = null;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                tv = (TextView) view.findViewById(android.R.id.text1);
                view.setTag(tv);
            }
            if (tv == null) {
                tv = (TextView) view.getTag();
            }
            tv.setText(data.get(i).name);
            return view;
        }

        public void setData(Teacher.List data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public interface TeacherListener {
        void onTeacherPicked(Teacher teacher);
    }

    private Adapter adapter;
    private TeacherListener listener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listener == null) return;
                Teacher teacher = (Teacher) adapter.getItem(i);
                listener.onTeacherPicked(teacher);
            }
        });
    }

    public void setTeacherList(Teacher.List teachers) {
        adapter.setData(teachers);
    }

    public void setTeacherListener(TeacherListener listener) {
        this.listener = listener;
    }
}
