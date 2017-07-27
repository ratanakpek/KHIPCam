package com.kshrd.ipcam.activitys;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.facebook.BuildConfig;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.REQUEST_INTENT;
import com.kshrd.ipcam.fragment.ForgotPasswordFragment;
import com.kshrd.ipcam.fragment.Frag_welcome_screen;
import com.kshrd.ipcam.fragment.LoginFragment;
import com.kshrd.ipcam.fragment.SignupFragment;
import com.kshrd.ipcam.main.IPCamTheme;
import com.kshrd.ipcam.main.MyActivity;

import java.security.MessageDigest;

public class WelcomeScreen extends MyActivity {
    String TAG = "WelcomeScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);
        keyHash();
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

        setContentView(R.layout.activity_welcome_screen);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Frag_welcome_screen fragWelcomeScreen = new Frag_welcome_screen();
            fragmentTransaction.add(R.id.activity_welcome_screen, fragWelcomeScreen, "hello");
            fragmentTransaction.commit();

        }

        IPCamTheme.getInstance().setNewTheme(this);

        changeAppStyle();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (requestCode == REQUEST_INTENT.IMAGE_PICKER_REQUEST) {
            fragmentManager.findFragmentByTag("Sign Up").onActivityResult(requestCode, resultCode, data);
        }
    }


    public void logIn(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.replace(R.id.activity_welcome_screen, loginFragment, "Log In");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void signUp(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SignupFragment signupFragment = new SignupFragment();
        fragmentTransaction.replace(R.id.activity_welcome_screen, signupFragment, "Sign Up");
        fragmentTransaction.addToBackStack("log");
        fragmentTransaction.commit();

    }

    public void fogotPassword(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
        fragmentTransaction.replace(R.id.activity_welcome_screen, forgotPasswordFragment, "Forgot Password");
        fragmentTransaction.addToBackStack("log");
        fragmentTransaction.commit();
    }

    /**
     * @Genderate Key for facbook key hash code
     */
    public void keyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.kshrd.ipcam",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * get current user facebook in app
     */

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void changeAppStyle() {
        getWindow().getDecorView().setBackgroundColor(IPCamTheme.windowColor);
    }
}
