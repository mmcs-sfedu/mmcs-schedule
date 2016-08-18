package com.nolan.mmcs_schedule.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class CustomArrayAdapter<T> extends BaseAdapter {
    private ArrayList<T> data;

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public T getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return id(data.get(i));
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
        T o = getItem(i);
        tv.setText(str(o));
        return view;
    }

    protected long id(T o) { return -1; }

    protected String str(T o) { return o.toString(); }

    public void setData(ArrayList<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
