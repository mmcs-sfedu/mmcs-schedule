package com.nolan.mmcs_schedule.repository.api;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public class RetrofitSpiceService extends RetrofitGsonSpiceService {

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(ScheduleApi.class);
    }

    @Override
    protected String getServerUrl() {
        return "http://schedule.sfedu.ru";
    }
}
