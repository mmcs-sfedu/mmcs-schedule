package com.nolan.mmcs_schedule;

import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.ui.BaseActivity;
import com.nolan.mmcs_schedule.utils.PrefUtils;

public class Injector {
    private static PrefUtils preferences;

    public static PrefUtils injectPreferences() {
        if (preferences == null) {
            preferences = new PrefUtils();
        }
        return preferences;
    }

    public static ScheduleRepository injectRepository(BaseActivity activity) {
        return activity.getScheduleRepository();
    }
}
