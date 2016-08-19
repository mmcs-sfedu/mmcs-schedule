package com.nolan.mmcs_schedule.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nolan.mmcs_schedule.R;

import java.util.ArrayList;

class ScheduleAdapter extends BaseAdapter {
    private ArrayList<DaySchedule> schedule;

    public ScheduleAdapter(ArrayList<DaySchedule> schedule) {
        super();
        this.schedule = schedule;
    }

    @Override
    public int getCount() {
        return schedule.size();
    }

    @Override
    public DaySchedule getItem(int i) {
        return schedule.get(i);
    }

    @Override
    public long getItemId(int i) {
        return -1;
    }

    private static class LessonViewHolder {
        public final View self;
        public final TextView tvBeginTime;
        public final TextView tvEndTime;
        public final TextView tvPrimaryText;
        public final TextView tvSecondaryText;
        public final TextView tvTertiaryText;
        public final TextView tvWeekType;

        public LessonViewHolder(View view) {
            this.self = view;
            this.tvBeginTime = (TextView) view.findViewById(R.id.tv_begin_time);
            this.tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
            this.tvPrimaryText = (TextView) view.findViewById(R.id.tv_primary_text);
            this.tvSecondaryText = (TextView) view.findViewById(R.id.tv_secondary_text);
            this.tvTertiaryText = (TextView) view.findViewById(R.id.tv_tertiary_text);
            this.tvWeekType = (TextView) view.findViewById(R.id.tv_week_type);
        }
    }

    private static class DayViewHolder {
        public final TextView tvDayOfWeek;
        public final LessonViewHolder[] lessons;

        public DayViewHolder(View view) {
            this.tvDayOfWeek = (TextView) view.findViewById(R.id.tv_day_of_week);
            this.lessons = new LessonViewHolder[6];
            this.lessons[0] = new LessonViewHolder(view.findViewById(R.id.lesson_0));
            this.lessons[1] = new LessonViewHolder(view.findViewById(R.id.lesson_1));
            this.lessons[2] = new LessonViewHolder(view.findViewById(R.id.lesson_2));
            this.lessons[3] = new LessonViewHolder(view.findViewById(R.id.lesson_3));
            this.lessons[4] = new LessonViewHolder(view.findViewById(R.id.lesson_4));
            this.lessons[5] = new LessonViewHolder(view.findViewById(R.id.lesson_5));
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DayViewHolder dayViewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.day_schedule, viewGroup, false);
            dayViewHolder = new DayViewHolder(view);
            view.setTag(dayViewHolder);
        }
        if (dayViewHolder == null) {
            dayViewHolder = (DayViewHolder) view.getTag();
        }
        DaySchedule daySchedule = schedule.get(i);
        dayViewHolder.tvDayOfWeek.setText(daySchedule.dayOfWeek);
        int j = 0;
        for (; j < daySchedule.lessons.size(); ++j) {
            dayViewHolder.lessons[j].self.setVisibility(View.VISIBLE);
            dayViewHolder.lessons[j].tvBeginTime.setText(daySchedule.lessons.get(j).beginTime);
            dayViewHolder.lessons[j].tvEndTime.setText(daySchedule.lessons.get(j).endTime);
            dayViewHolder.lessons[j].tvPrimaryText.setText(daySchedule.lessons.get(j).primaryText);
            dayViewHolder.lessons[j].tvSecondaryText.setText(daySchedule.lessons.get(j).secondaryText);
            dayViewHolder.lessons[j].tvTertiaryText.setText(daySchedule.lessons.get(j).tertiaryText);
            dayViewHolder.lessons[j].tvWeekType.setText(daySchedule.lessons.get(j).weekType);
        }
        for (; j < 6; ++j) {
            dayViewHolder.lessons[j].self.setVisibility(View.GONE);
        }
        return view;
    }
}
