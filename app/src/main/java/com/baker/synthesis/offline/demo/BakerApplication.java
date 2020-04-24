package com.baker.synthesis.offline.demo;

import android.app.Application;
import android.content.Context;

/**
 * Create by hsj55
 * 2020/4/23
 */
public class BakerApplication extends Application {
    private static BakerApplication application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
    }

    public static BakerApplication getInstance() {
        return application;
    }
}
