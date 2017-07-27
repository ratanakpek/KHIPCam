package com.kshrd.ipcam.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kshrd.ipcam.R;

public class FragUserProfile extends android.support.v4.app.Fragment {

    private static FragUserProfile instance = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_user_profile, container, false);
    }

    public static FragUserProfile getInstance(){
        try {
            if (instance == null){
                Log.i("Profile", "create new instance: ");
                instance = new FragUserProfile();

                Bundle bundle = new Bundle();
                instance.setArguments(bundle);

                return instance;
            }else {
                Log.i("Profile", "get old instance: ");
                return instance;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
