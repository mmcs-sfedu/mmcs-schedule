package com.nolan.mmcs_schedule.ui.pick_schedule_activity;

import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.ui.schedule_activity.CustomArrayAdapter;

public class GroupAdapter extends CustomArrayAdapter<Group> {
    @Override
    protected String str(Group group) {
        String result = "";
        if (group.getName() != null) {
            result += group.getName() + ", ";
        }
        result += group.getNum() + " группа";
        return result;
    }
}
