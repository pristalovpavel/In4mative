package com.pristalovpavel.in4mative;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by anil on 09.01.17.
 */

public class In4mativeApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        MultiDex.install(this);
    }
}
