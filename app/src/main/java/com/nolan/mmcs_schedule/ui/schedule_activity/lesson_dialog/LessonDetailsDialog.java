package com.nolan.mmcs_schedule.ui.schedule_activity.lesson_dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nolan.mmcs_schedule.R;
import com.nolan.mmcs_schedule.utils.PairSerializable;

public class LessonDetailsDialog extends DialogFragment {
    private static final String KEY_LESSON_DETAILS = "key_lesson_details";

    public static LessonDetailsDialog create(LessonDetails details) {
        LessonDetailsDialog dialog = new LessonDetailsDialog();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_LESSON_DETAILS, details);
        dialog.setArguments(arguments);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LessonDetails details = (LessonDetails) getArguments().getSerializable(KEY_LESSON_DETAILS);

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View content = inflater.inflate(R.layout.dialog_lesson_details, null);

        TextView tvBeginTime = (TextView) content.findViewById(R.id.tv_begin_time);
        TextView tvEndTime = (TextView) content.findViewById(R.id.tv_end_time);
        LinearLayout llDetailsList = (LinearLayout) content.findViewById(R.id.ll_details_list);

        tvBeginTime.setText(details.getBeginTime());
        tvEndTime.setText(details.getEndTime());
        for (PairSerializable<String, String> row : details.getRows()) {
            View vRow = inflater.inflate(R.layout.details_row, llDetailsList, false);
            llDetailsList.addView(vRow);
            TextView tvShort = (TextView) vRow.findViewById(R.id.tv_short);
            TextView tvLong = (TextView) vRow.findViewById(R.id.tv_long);
            tvShort.setText(row.first);
            tvLong.setText(row.second);
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(details.getSubject())
                .setView(content)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
