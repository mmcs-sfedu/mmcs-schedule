package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawGrade;

import java.util.ArrayList;

public class Grade {
    public final int id;
    public final int num;
    public final RawGrade.Degree degree;

    public Grade(RawGrade rawGrade) {
        id = rawGrade.getId();
        num = rawGrade.getNum();
        degree = rawGrade.getDegree();
    }

    public static class List extends ArrayList<Grade> {
        public List(RawGrade.List rawList) {
            ensureCapacity(rawList.size());
            for (RawGrade grade : rawList) {
                add(new Grade(grade));
            }
        }
    }


}
