package com.nolan.mmcs_schedule.repository.api.primitives;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("SpellCheckingInspection")
public class RawGroupOfLesson
    implements Serializable {

    @SerializedName("uberid") private   int uberId   = -1;
    @SerializedName("groupnum") private int groupNum = -1;
    @SerializedName("gradenum") private int gradeNum = -1;

    private RawGrade.Degree degree = null;
    private String          name   = null;

    public RawGroupOfLesson(int uberId, int groupNum, int gradeNum, RawGrade.Degree degree,
                            String name) {
        this.uberId = uberId;
        this.groupNum = groupNum;
        this.gradeNum = gradeNum;
        this.degree = degree;
        this.name = name;
    }

    public int getUberId() {
        return uberId;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public int getGradeNum() {
        return gradeNum;
    }

    public RawGrade.Degree getDegree() {
        return degree;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "" + gradeNum + '.' + groupNum;
    }
}
