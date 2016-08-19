package com.nolan.mmcs_schedule;

import com.nolan.mmcs_schedule.repository.ScheduleRepository;
import com.nolan.mmcs_schedule.ui.BaseActivity;
import com.nolan.mmcs_schedule.utils.UtilsPreferences;

public class Injector {
    private static UtilsPreferences preferences;

    public static UtilsPreferences injectPreferences() {
        if (preferences == null) {
            preferences = new UtilsPreferences();
        }
        return preferences;
    }

    public static ScheduleRepository injectRepository(BaseActivity activity) {
        return activity.getScheduleRepository();
    }
}
