package com.nolan.mmcs_schedule.ui.pick_schedule_activity;

import com.nolan.mmcs_schedule.repository.primitives.Grade;
import com.nolan.mmcs_schedule.ui.schedule_activity.CustomArrayAdapter;

public class GradeAdapter extends CustomArrayAdapter<Grade> {
    @Override
    protected String str(Grade grade) {
        String result;
        switch (grade.degree) {
            case BACHELOR:
                result = "Бакалавриат ";
                break;
            case MASTER:
                result = "Магистратура ";
                break;
            case SPECIALIST:
                result = "Специалитет ";
                break;
            default:
                throw new Error("unreachable statement");
        }
        result += grade.num;
        result += " курс";
        return result;
    }
}
