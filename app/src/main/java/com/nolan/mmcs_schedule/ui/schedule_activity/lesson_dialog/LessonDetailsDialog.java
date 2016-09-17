package com.nolan.mmcs_schedule.ui.schedule_activity.lesson_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nolan.mmcs_schedule.R;
import com.nolan.mmcs_schedule.utils.JsonUtils;
import com.nolan.mmcs_schedule.utils.PairSerializable;

public class LessonDetailsDialog extends DialogFragment {
    public interface Activity {
        void onShowSchedule(int dayIndex, int lessonIndex, int teacherOrGroupIndex);
    }

    private static final String KEY_IS_FOR_GROUP = "key_is_for_group";
    private static final String KEY_LESSON_DETAILS = "key_lesson_details";
    private static final String KEY_DAY_INDEX = "key_day_index";
    private static final String KEY_LESSON_INDEX = "key_lesson_index";

    public static LessonDetailsDialog create(int dayIndex, int lessonIndex,
                                             LessonForGroupDetails details) {
        LessonDetailsDialog dialog = new LessonDetailsDialog();
        Bundle arguments = new Bundle();
        arguments.putBoolean(KEY_IS_FOR_GROUP, true);
        arguments.putInt(KEY_DAY_INDEX, dayIndex);
        arguments.putInt(KEY_LESSON_INDEX, lessonIndex);
        arguments.putString(KEY_LESSON_DETAILS, JsonUtils.toJson(details, LessonForGroupDetails.class));
        dialog.setArguments(arguments);
        return dialog;
    }

    public static LessonDetailsDialog create(int dayIndex, int lessonIndex,
                                             LessonForTeacherDetails details) {
        LessonDetailsDialog dialog = new LessonDetailsDialog();
        Bundle arguments = new Bundle();
        arguments.putBoolean(KEY_IS_FOR_GROUP, false);
        arguments.putInt(KEY_DAY_INDEX, dayIndex);
        arguments.putInt(KEY_LESSON_INDEX, lessonIndex);
        arguments.putString(KEY_LESSON_DETAILS, JsonUtils.toJson(details, LessonForTeacherDetails.class));
        dialog.setArguments(arguments);
        return dialog;
    }

    private View.OnClickListener onItemClickListener;
    private int dayIndex;
    private int lessonIndex;
    private boolean isForGroup;
    private Object details;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        isForGroup = arguments.getBoolean(KEY_IS_FOR_GROUP);
        dayIndex = arguments.getInt(KEY_DAY_INDEX);
        lessonIndex = arguments.getInt(KEY_LESSON_INDEX);
        if (isForGroup) {
            details = JsonUtils.fromJson(
                    arguments.getString(KEY_LESSON_DETAILS), LessonForGroupDetails.class);
        } else {
            details = JsonUtils.fromJson(
                    arguments.getString(KEY_LESSON_DETAILS), LessonForTeacherDetails.class);
        }
        onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ((Activity) getActivity())
                        .onShowSchedule(dayIndex, lessonIndex, (Integer) view.getTag());
            }
        };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View content;
        String subject;
        if (isForGroup) {
            LessonForGroupDetails groupDetails = (LessonForGroupDetails) details;
            subject = groupDetails.getSubject();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            content = inflater.inflate(R.layout.dialog_lesson_details, null);
            TextView dayOfWeek = (TextView) content.findViewById(R.id.tv_day_of_week);
            dayOfWeek.setText(groupDetails.getDayOfWeek());
            TextView period = (TextView) content.findViewById(R.id.tv_period);
            period.setText(groupDetails.getPeriod());
            LinearLayout llTeacherList = (LinearLayout) content.findViewById(R.id.ll_teacher_list);
            int pos = 0;
            for (PairSerializable<String, String> roomAndTeacher : groupDetails.getRoomsAndTeachers()) {
                View row = inflater.inflate(R.layout.details_teacher_list_item, null, false);
                llTeacherList.addView(row);
                TextView tvTeacher = (TextView) row.findViewById(R.id.tv_teacher);
                TextView tvRoom = (TextView) row.findViewById(R.id.tv_room);
                tvRoom.setText(roomAndTeacher.first);
                tvTeacher.setText(roomAndTeacher.second);
                if (!TextUtils.isEmpty(roomAndTeacher.second)) {
                    row.setTag(pos);
                    row.setClickable(true);
                    row.setOnClickListener(onItemClickListener);
                }
                ++pos;
            }
        } else {
            LessonForTeacherDetails teacherDetails = (LessonForTeacherDetails) details;
            subject = teacherDetails.getSubject();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            content = inflater.inflate(R.layout.dialog_lesson_details, null);
            TextView dayOfWeek = (TextView) content.findViewById(R.id.tv_day_of_week);
            dayOfWeek.setText(teacherDetails.getDayOfWeek());
            TextView period = (TextView) content.findViewById(R.id.tv_period);
            period.setText(teacherDetails.getPeriod());
            LinearLayout llTeacherList = (LinearLayout) content.findViewById(R.id.ll_teacher_list);
            TextView room = (TextView) content.findViewById(R.id.tv_room);
            room.setVisibility(View.VISIBLE);
            room.setText(teacherDetails.getRoom());
            int pos = 0;
            for (String group : teacherDetails.getGroups()) {
                TextView tvGroup = (TextView) inflater.inflate(R.layout.details_group_list_item, null, false);
                llTeacherList.addView(tvGroup);
                tvGroup.setTag(pos++);
                tvGroup.setText(group);
                tvGroup.setOnClickListener(onItemClickListener);
            }
        }
        return new AlertDialog.Builder(getActivity())
                .setTitle(subject)
                .setView(content)
                .setPositiveButton(android.R.string.ok, null)
                .create() ;
    }

}
