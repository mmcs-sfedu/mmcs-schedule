package com.nolan.mmcs_schedule.ui.pick_schedule_activity;

import com.nolan.mmcs_schedule.repository.primitives.Group;
import com.nolan.mmcs_schedule.ui.schedule_activity.CustomArrayAdapter;

public class GroupAdapter extends CustomArrayAdapter<Group> {
    @Override
    protected String str(Group group) {
        String result = "";
        if (group.name != null) {
            result += group.name + ", ";
        }
        result += group.num + " группа";
        return result;
    }
}
