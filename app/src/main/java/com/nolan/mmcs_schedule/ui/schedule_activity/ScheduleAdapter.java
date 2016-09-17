package com.nolan.mmcs_schedule.ui.schedule_activity;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nolan.mmcs_schedule.R;

import java.util.ArrayList;

public class ScheduleAdapter extends BaseAdapter {
    public interface OnLessonClickListener {
        void onLessonClick(int day, int lesson);
    }

    private static class LessonViewHolder {
        public final View self;
        public final TextView tvBeginTime;
        public final TextView tvEndTime;
        public final TextView tvPrimaryText;
        public final TextView tvWeekType;
        public final View vBottomDivider;

        public LessonViewHolder(View view) {
            this.self = view;
            this.tvBeginTime = (TextView) view.findViewById(R.id.tv_begin_time);
            this.tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
            this.tvPrimaryText = (TextView) view.findViewById(R.id.tv_primary_text);
            this.tvWeekType = (TextView) view.findViewById(R.id.tv_week_type);
            this.vBottomDivider = view.findViewById(R.id.v_bottom_divider);
        }
    }

    private static class DayViewHolder {
        public final LinearLayout ll;
        public final TextView tvDayOfWeek;

        public DayViewHolder(View view) {
            this.ll = (LinearLayout) view.findViewById(R.id.ll);
            this.tvDayOfWeek = (TextView) view.findViewById(R.id.tv_day_of_week);
        }
    }

    private static class LessonViewHolderCache {
        private static int INITIAL_SIZE = 10;

        private ArrayList<LessonViewHolder> freeViews;
        private LayoutInflater inflater;

        public LessonViewHolderCache(LayoutInflater inflater) {
            this.inflater = inflater;
            this.freeViews = new ArrayList<>(INITIAL_SIZE);
            for (int i = 0; i < INITIAL_SIZE; ++i) {
                freeViews.add(inflateLessonViewHolder());
            }
        }

        private LessonViewHolder inflateLessonViewHolder() {
            View view = inflater.inflate(R.layout.lesson, null);
            LessonViewHolder lessonViewHolder =  new LessonViewHolder(view);
            view.setTag(lessonViewHolder);
            return lessonViewHolder;
        }

        public LessonViewHolder acquire() {
            if (freeViews.isEmpty()) {
                return inflateLessonViewHolder();
            }
            int lastItem = freeViews.size() - 1;
            LessonViewHolder lastViewHolder = freeViews.get(lastItem);
            freeViews.remove(lastItem);
            return lastViewHolder;
        }

        public void put(LessonViewHolder lessonViewHolder) {
            freeViews.add(lessonViewHolder);
        }
    }

    @NonNull
    private OnLessonClickListener listener;

    private ArrayList<DaySchedule> schedule;
    private LessonViewHolderCache lessonViewHolderCache;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int day = (int) view.getTag(R.id.tag_day);
            int lesson = (int) view.getTag(R.id.tag_lesson);
            listener.onLessonClick(day, lesson);
        }
    };

    public ScheduleAdapter(@NonNull OnLessonClickListener listener) {
        super();
        this.listener = listener;
    }

    public void setData(ArrayList<DaySchedule> schedule) {
        this.schedule = schedule;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return schedule == null ? 0 : schedule.size();
    }

    @Override public Object getItem(int i) { return null; }
    @Override public long getItemId(int i) { return 0; }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (lessonViewHolderCache == null) {
            lessonViewHolderCache =
                    new LessonViewHolderCache(LayoutInflater.from(viewGroup.getContext()));
        }
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
        if (dayViewHolder.ll.getChildCount() - 2 < daySchedule.lessons.size()) {
            for (int j = dayViewHolder.ll.getChildCount() - 2; j < daySchedule.lessons.size(); ++j) {
                dayViewHolder.ll.addView(lessonViewHolderCache.acquire().self);
            }
        } else {
            for (int j = dayViewHolder.ll.getChildCount() - 1; j > daySchedule.lessons.size() + 1; --j) {
                lessonViewHolderCache.put((LessonViewHolder) dayViewHolder.ll.getChildAt(j).getTag());
                dayViewHolder.ll.removeViewAt(j);
            }
        }
        for (int j = 0; j < daySchedule.lessons.size(); ++j) {
            LessonViewHolder lvh = (LessonViewHolder) dayViewHolder.ll.getChildAt(j + 2).getTag();
            Lesson lesson = daySchedule.lessons.get(j);
            lvh.self.setTag(R.id.tag_day, i);
            lvh.self.setTag(R.id.tag_lesson, j);
            lvh.self.setOnClickListener(onClickListener);
            lvh.self.setVisibility(View.VISIBLE);
            lvh.tvBeginTime.setText(lesson.beginTime);
            lvh.tvEndTime.setText(lesson.endTime);
            lvh.tvPrimaryText.setText(lesson.primaryText);
            lvh.tvWeekType.setText(lesson.weekType);
            lvh.tvWeekType.setVisibility(lesson.weekType.isEmpty() ? View.GONE : View.VISIBLE);
            lvh.vBottomDivider.setVisibility(View.VISIBLE);
        }
        LessonViewHolder lastViewHolder = (LessonViewHolder)
                dayViewHolder.ll.getChildAt(daySchedule.lessons.size() + 1).getTag();
        lastViewHolder.vBottomDivider.setVisibility(View.GONE);
        return view;
    }
}
