package com.nolan.mmcs_schedule;

import android.app.Application;

import com.nolan.mmcs_schedule.utils.PrefUtils;

import java.io.File;

public class ScheduleApplication extends Application{
    private static ScheduleApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        PrefUtils prefs = Injector.injectPreferences();
        if (prefs.getCacheVersion() != BuildConfig.VERSION_CODE) {
            prefs.setCacheVersion(BuildConfig.VERSION_CODE);
            clearCache();
        }
    }

    public static ScheduleApplication get() {
        return context;
    }

    public void clearCache() {
        File cacheDirectory = getCacheDir();
        File applicationDataDirectory = new File(cacheDirectory.getParent());
        if (applicationDataDirectory.exists()) {
            String[] fileNames = applicationDataDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDataDirectory, fileName));
                }
            }
        }
    }

    private static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (String aChildren : children) {
                    deletedAll = deleteFile(new File(file, aChildren)) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }
        return deletedAll;
    }
}
