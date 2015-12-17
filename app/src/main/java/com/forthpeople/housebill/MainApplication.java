package com.forthpeople.housebill;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by dhiraj on 16-12-2015.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
