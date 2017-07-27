package com.kshrd.ipcam.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.entities.user.User;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.controller.CallBack.UserCallback;
import com.kshrd.ipcam.network.controller.UserController;
import com.kshrd.ipcam.main.IPCamTheme;
import com.kshrd.ipcam.main.MyFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 1/5/2017.
 */

public class FragProfileEditing extends MyFragment implements UserCallback{
    private Button btnUserName,btnPassword;
    private Bundle mbundle ;
    private String userprofile;
    private CircleImageView profile_image;
    FragmentManager manager;
    FragmentTransaction transaction;

    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private ImageView imageViewBackNav;

    private static FragProfileEditing instance = null;

    public static FragProfileEditing getInstance(){
        try {
            if (instance == null){
                instance = new FragProfileEditing();
                Bundle bundle = new Bundle();
                //    bundle.putString("","");
                instance.setArguments(bundle);
                return instance;
            }else {
                return instance;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile_edited, container, false);
        btnUserName = (Button)view.findViewById(R.id.username);
        btnPassword= (Button) view.findViewById(R.id.password);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        setToolbar(view);

        btnUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditingFrag();
            }
        });
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEditingFrag();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProfileEditingFrag();
            }
        });

        mbundle = getArguments();
        userprofile=mbundle.getString("IMAGE");

        if(mbundle != null){
            btnUserName.setText(mbundle.getString("USER_NAME"));
            if(userprofile.contains("https://graph.facebook.com/"))
                loadUserProfile(profile_image,userprofile);
            else
                loadUserProfile(profile_image, ApiGenerator.BASE_URL+userprofile);

        }

        return view;
    }

    public void setToolbar(View view){
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_with_title_and_navigation);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        tvToolbarTitle= (TextView) view.findViewById(R.id.toolbar_with_title_and_navigation_title);
        imageViewBackNav = (ImageView) view.findViewById(R.id.navigation_back_action_bar_with_title_and_navigation);
        tvToolbarTitle.setText(R.string.UserProfile);
        imageViewBackNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void loadUserByEmail(User user) {
        if(user != null){
            btnUserName.setText(user.getUsername());
            mbundle.putString("PASSWORD",user.getPassword());
            mbundle.putString("IMAGE",user.getImage());
            mbundle.putString("USER_NAME",user.getUsername());
        }

        if(FragImageEditing.isUserInfoChange){
            if(user.getImage().contains("https://graph.facebook.com/"))
                loadUserProfile(profile_image,user.getImage());
            else
                loadUserProfile(profile_image, ApiGenerator.BASE_URL+user.getImage());
        }

        Log.i("UserChange", "loadUserById: user:  "+user.getUsername());
        Log.i("UserChanges", "loadUserById: user:  "+user.getPassword());
    }

    @Override
    public void loadUserById(User user) {
    }

    @Override
    public void emailCheckerState(Boolean status) {
    }

    @Override
    public void onResume() {
        super.onResume();
        changeAppStyle();
        if(mbundle != null){
            if(FragUsernameEditing.isUserInfoChange || FragPasswordEditing.isUserInfoChange || FragImageEditing.isUserInfoChange){
                Log.i("UserChange", "onResume:   UserChange ");
                new UserController().getUserByEmail(mbundle.getString("USER_EMAIL"),this);
            }
        }
    }
    public void loadUserProfile(ImageView imageView, String URL) {
        final Bitmap[] theBitmap = {null};

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (Looper.myLooper() == null)
                {
                    Looper.prepare();
                }
                try {
                    theBitmap[0] = Glide.
                            with(FragProfileEditing.this).
                            load(URL).
                            asBitmap().
                            into(-1, -1).
                            get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void dummy) {
                if (null != theBitmap[0]) {
                    // The full bitmap should be available here
                    imageView.setImageBitmap(theBitmap[0]);
                    Log.d("Image", "Image loaded");
                }
                ;
            }
        }.execute();
    }
    public void imageProfileEditingFrag(){
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();

        FragImageEditing.getInstance().setArguments(mbundle);

        transaction.replace(R.id.user_edit_container, FragImageEditing.getInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void passwordEditingFrag(){
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();


        FragPasswordEditing.getInstance().setArguments(mbundle);

        transaction.replace(R.id.user_edit_container,FragPasswordEditing.getInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void usernameEditingFrag(){
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();

        FragUsernameEditing.getInstance().setArguments(mbundle);

        transaction.replace(R.id.user_edit_container, FragUsernameEditing.getInstance(),"username_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void changeAppStyle(){
        setToolbarNoOptionMenuStyle(toolbar,imageViewBackNav,tvToolbarTitle);
        profile_image.setBorderColor(IPCamTheme.profile_CircleImageBorderColor);
        setButtonBorderAndTextHintColor(btnUserName,btnPassword);
    }

    public void setButtonBorderAndTextHintColor(Button... buttons){
        for(int i=0;i<buttons.length;i++){
            buttons[i].setHintTextColor(IPCamTheme.inputTextHint);
            buttons[i].setBackground(IPCamTheme.profile_ButtonBorderArround);
        }
    }

}
