package com.nolan.mmcs_schedule.ui.schedule_activity.lesson_dialog;


import com.nolan.mmcs_schedule.utils.PairSerializable;

import java.io.Serializable;
import java.util.ArrayList;

public class LessonDetails implements Serializable {
    private String beginTime;
    private String endTime;
    private String subject;
    private ArrayList<PairSerializable<String, String>> rows;

    public LessonDetails(String beginTime, String endTime, String subject,
                         ArrayList<PairSerializable<String, String>> rows) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.subject = subject;
        this.rows = rows;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSubject() {
        return subject;
    }

    public ArrayList<PairSerializable<String, String>> getRows() {
        return rows;
    }
}
