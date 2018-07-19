package com.lrx.alaudidae;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;


/**
 * The type Alaudidae application.
 */
public class AlaudidaeApplication extends Application {
    private static AlaudidaeApplication sShared;

    /**
     * Shared alaudidae application.
     *
     * @return the alaudidae application
     */
    public static AlaudidaeApplication shared() {
        return sShared;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());
        LeakCanary.install(this);
        sShared = this;
    }
}
