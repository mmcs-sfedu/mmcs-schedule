package com.nolan.mmcs_schedule.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nolan.mmcs_schedule.R;

import java.util.ArrayList;

public class ScheduleAdapter extends BaseAdapter {
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
        public final View vBottomDivider;

        public LessonViewHolder(View view) {
            this.self = view;
            this.tvBeginTime = (TextView) view.findViewById(R.id.tv_begin_time);
            this.tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
            this.tvPrimaryText = (TextView) view.findViewById(R.id.tv_primary_text);
            this.tvSecondaryText = (TextView) view.findViewById(R.id.tv_secondary_text);
            this.tvTertiaryText = (TextView) view.findViewById(R.id.tv_tertiary_text);
            this.tvWeekType = (TextView) view.findViewById(R.id.tv_week_type);
            this.vBottomDivider = view.findViewById(R.id.v_bottom_divider);
        }
    }

    private static class DayViewHolder {
        public final TextView tvDayOfWeek;
        public final LessonViewHolder[] lessons;

        public DayViewHolder(View view) {
            this.tvDayOfWeek = (TextView) view.findViewById(R.id.tv_day_of_week);
            this.lessons = new LessonViewHolder[12];
            this.lessons[0] = new LessonViewHolder(view.findViewById(R.id.lesson_0));
            this.lessons[1] = new LessonViewHolder(view.findViewById(R.id.lesson_1));
            this.lessons[2] = new LessonViewHolder(view.findViewById(R.id.lesson_2));
            this.lessons[3] = new LessonViewHolder(view.findViewById(R.id.lesson_3));
            this.lessons[4] = new LessonViewHolder(view.findViewById(R.id.lesson_4));
            this.lessons[5] = new LessonViewHolder(view.findViewById(R.id.lesson_5));
            this.lessons[6] = new LessonViewHolder(view.findViewById(R.id.lesson_6));
            this.lessons[7] = new LessonViewHolder(view.findViewById(R.id.lesson_7));
            this.lessons[8] = new LessonViewHolder(view.findViewById(R.id.lesson_8));
            this.lessons[9] = new LessonViewHolder(view.findViewById(R.id.lesson_9));
            this.lessons[10] = new LessonViewHolder(view.findViewById(R.id.lesson_10));
            this.lessons[11] = new LessonViewHolder(view.findViewById(R.id.lesson_11));
        }
    }

    private void setTextAndSetVisibility(TextView textView, String text) {
        textView.setText(text);
        if (text.isEmpty()) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
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
            LessonViewHolder lessonViewHolder = dayViewHolder.lessons[j];
            Lesson lesson = daySchedule.lessons.get(j);
            lessonViewHolder.self.setVisibility(View.VISIBLE);
            lessonViewHolder.tvBeginTime.setText(lesson.beginTime);
            lessonViewHolder.tvEndTime.setText(lesson.endTime);
            lessonViewHolder.tvPrimaryText.setText(lesson.primaryText);
            setTextAndSetVisibility(lessonViewHolder.tvSecondaryText, lesson.secondaryText);
            setTextAndSetVisibility(lessonViewHolder.tvTertiaryText, lesson.tertiaryText);
            setTextAndSetVisibility(lessonViewHolder.tvWeekType, lesson.weekType);
            lessonViewHolder.vBottomDivider.setVisibility(View.VISIBLE);
        }
        dayViewHolder.lessons[j - 1].vBottomDivider.setVisibility(View.GONE);
        for (; j < 12; ++j) {
            dayViewHolder.lessons[j].self.setVisibility(View.GONE);
        }
        return view;
    }
}
