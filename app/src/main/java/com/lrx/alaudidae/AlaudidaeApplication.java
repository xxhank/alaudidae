package com.lrx.alaudidae;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
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
        FormatStrategy formatStrategy = SingleLineFormatStrategy
                .newBuilder()
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        LeakCanary.install(this);
        sShared = this;
    }
}
