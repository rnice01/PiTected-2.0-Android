package com.example.rnice01.pitected2;

import android.app.Application;
import android.content.Context;

/**
 * Created by rnice01 on 3/16/2016.
 */
public class ContextClass extends Application{
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }


}
