package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawGrade;

import java.util.ArrayList;

public class Grade {
    private int id;
    private int num;
    private RawGrade.Degree degree;

    public Grade(RawGrade rawGrade) {
        id = rawGrade.getId();
        num = rawGrade.getNum();
        degree = rawGrade.getDegree();
    }

    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public RawGrade.Degree getDegree() {
        return degree;
    }

    @Override
    public String toString() {
        return degree.toString() + " " + num + " курс";
    }

    public static class List extends ArrayList<Grade> { }
}
