package com.kshrd.ipcam.activitys;

import android.support.v4.app.FragmentManager;

import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.WindowManager;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.Callback.UserProfilePasser;
import com.kshrd.ipcam.fragment.FragProfileEditing;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.services.UserService;
import com.kshrd.ipcam.main.IPCamTheme;
import com.kshrd.ipcam.main.MyActivity;

import okhttp3.MultipartBody;


public class Profile extends MyActivity implements UserProfilePasser{

    String TAG = "Profile";
    public static MenuItem item;
    public static int FRAGMENT_STAE = 1;
    private Bundle mbundle ;
    FragmentManager manager;
    FragmentTransaction transaction;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage(this);
        FRAGMENT_STAE=1;
        setContentView(R.layout.activity_profile);
        mbundle = getIntent().getExtras();
        profileEditingFrag();

        changeAppStyle();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        FRAGMENT_STAE = savedInstanceState.getInt("Fragment");

    }

    public void profileEditingFrag(){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        FragProfileEditing.getInstance().setArguments(mbundle);
        transaction.add(R.id.user_edit_container, FragProfileEditing.getInstance(),"profile");
        transaction.commit();
    }

    private  MultipartBody.Part user_profile;
    private  int user_id;
    private String username;

    @Override
    public void userProfilePasser(int user_id, MultipartBody.Part user_profile) {
        this.user_profile = user_profile;
        this.user_id = user_id;
    }


    public UserService getUserService(){
        UserService userService = ApiDataCompromiser.createService(UserService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return userService;
    }

    public void changeAppStyle(){
        getWindow().getDecorView().setBackgroundColor(IPCamTheme.windowColor);
    }
}