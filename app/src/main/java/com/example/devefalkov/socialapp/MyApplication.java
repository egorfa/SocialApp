package com.example.devefalkov.socialapp;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.vk.sdk.VKSdk;

/**
 * Created by Egor on 01/09/16.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
