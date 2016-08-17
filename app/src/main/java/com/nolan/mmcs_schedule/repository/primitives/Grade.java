package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawGrade;

import java.util.ArrayList;

public class Grade extends RawGrade {
    public Grade(RawGrade rawGrade) {
        super(rawGrade.getId(), rawGrade.getNum(), rawGrade.getDegree());
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
