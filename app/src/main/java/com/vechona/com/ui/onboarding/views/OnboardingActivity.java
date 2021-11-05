package com.vechona.com.ui.onboarding.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vechona.app.R;
import com.vechona.com.ui.activity.HomeActivity;

public class OnboardingActivity extends AppCompatActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, OnboardingActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        openSplashFragment();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    HomeActivity.start(OnboardingActivity.this);
                } else {
                    openLandingFragment();
                }
            }
        }, 1000);
    }

    private void openLandingFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            replaceFragment(LandingFragment.newInstance());
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, fragment, null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openSplashFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            Fragment splashFragment = new SplashFragment();
            replaceFragment(splashFragment);
        }
    }

}
