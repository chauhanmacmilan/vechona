package com.vechona.com.app;

import android.app.Application;

import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.ui.onboarding.views.OnboardingActivity;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initSDKs();
    }

    private void initSDKs() {

    }

    public void logoutUser() {
        PreferenceDataHelper.getInstance(this).clearUser();
        OnboardingActivity.start(this);
    }
}
