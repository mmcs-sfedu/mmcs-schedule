package com.nolan.mmcs_schedule.ui.schedule_activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nolan.mmcs_schedule.R;
import com.nolan.mmcs_schedule.repository.primitives.WeekType;

import java.util.ArrayList;

public class ScheduleAdapter extends BaseAdapter {
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
        public final LinearLayout ll;
        public final TextView tvDayOfWeek;

        public DayViewHolder(View view) {
            this.ll = (LinearLayout) view.findViewById(R.id.ll);
            this.tvDayOfWeek = (TextView) view.findViewById(R.id.tv_day_of_week);
        }
    }

    public static class Data {
        private final ArrayList<DaySchedule> scheduleFull;
        private final ArrayList<DaySchedule> scheduleUpper;
        private final ArrayList<DaySchedule> scheduleLower;

        public Data(ArrayList<DaySchedule> scheduleFull,
                    ArrayList<DaySchedule> scheduleUpper,
                    ArrayList<DaySchedule> scheduleLower) {
            this.scheduleFull = scheduleFull;
            this.scheduleUpper = scheduleUpper;
            this.scheduleLower = scheduleLower;
        }

        public ArrayList<DaySchedule> get(WeekType weekType) {
            switch (weekType) {
                case FULL: return scheduleFull;
                case UPPER: return scheduleUpper;
                case LOWER: return scheduleLower;
                default:
                    throw new Error("unreachable statement");
            }
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
            if (!freeViews.isEmpty()) {
                int lastItem = freeViews.size() - 1;
                LessonViewHolder lastViewHolder = freeViews.get(lastItem);
                freeViews.remove(lastItem);
                return lastViewHolder;
            }
            return inflateLessonViewHolder();
        }

        public void put(LessonViewHolder lessonViewHolder) {
            freeViews.add(lessonViewHolder);
        }
    }

    private Data data;
    private WeekType weekType = WeekType.FULL;
    private LessonViewHolderCache lessonViewHolderCache;

    public void setData(Data data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setWeekType(WeekType weekType) {
        this.weekType = weekType;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data == null) return 0;
        ArrayList<DaySchedule> schedule = data.get(weekType);
        return schedule == null ? 0 : schedule.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static void setTextAndSetVisibility(TextView textView, String text) {
        textView.setText(text);
        if (text.isEmpty()) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
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
        DaySchedule daySchedule = data.get(weekType).get(i);
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
            LessonViewHolder lessonViewHolder =
                    (LessonViewHolder) dayViewHolder.ll.getChildAt(j + 2).getTag();
            Lesson lesson = daySchedule.lessons.get(j);
            lessonViewHolder.self.setVisibility(View.VISIBLE);
            lessonViewHolder.tvBeginTime.setText(lesson.beginTime);
            lessonViewHolder.tvEndTime.setText(lesson.endTime);
            lessonViewHolder.tvPrimaryText.setText(lesson.primaryText);
            setTextAndSetVisibility(lessonViewHolder.tvSecondaryText, lesson.secondaryText);
            setTextAndSetVisibility(lessonViewHolder.tvTertiaryText, lesson.tertiaryText);
            lessonViewHolder.tvWeekType.setText(lesson.weekType);
            if (weekType != WeekType.FULL || lesson.weekType.isEmpty()) {
                lessonViewHolder.tvWeekType.setVisibility(View.GONE);
            } else {
                lessonViewHolder.tvWeekType.setVisibility(View.VISIBLE);
            }
            lessonViewHolder.vBottomDivider.setVisibility(View.VISIBLE);
        }
        LessonViewHolder lessonViewHolder = (LessonViewHolder)
                dayViewHolder.ll.getChildAt(daySchedule.lessons.size() + 1).getTag();
        lessonViewHolder.vBottomDivider.setVisibility(View.GONE);
        return view;
    }
}
