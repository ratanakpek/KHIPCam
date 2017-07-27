package com.kshrd.ipcam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.services.UserService;
import com.kshrd.ipcam.main.MyFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 1/5/2017.
 */

public class FragUsernameEditing extends MyFragment {
    public static String username;
    String TAG = "FragUsernameEditing";
    Button btnSaveUsername;
    EditText edUsername;
    private TextView textViewDes;
    public static boolean isUserInfoChange;
    static Bundle bundle;

    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private ImageView imageViewBackNav;
    private String uname;


    private static FragUsernameEditing instance = null;

    public static FragUsernameEditing getInstance() {
        try {
            if (instance == null) {
                instance = new FragUsernameEditing();

                Bundle bundle = new Bundle();
                instance.setArguments(bundle);
                return instance;

            } else {
                return instance;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile_username_edited, container, false);
        bundle = getArguments();
        isUserInfoChange = false;
        setToolbar(view);
        return view;
    }

    public void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_with_title_and_navigation);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        tvToolbarTitle = (TextView) view.findViewById(R.id.toolbar_with_title_and_navigation_title);
        imageViewBackNav = (ImageView) view.findViewById(R.id.navigation_back_action_bar_with_title_and_navigation);
        tvToolbarTitle.setText(R.string.Username);
        imageViewBackNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUsername.setText(bundle.getString("USER_NAME"));
                hideSoftKeyboard(getActivity());
                edUsername.setError(null);
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        edUsername = (EditText) view.findViewById(R.id.editText2);
        textViewDes = (TextView) view.findViewById(R.id.textViewDes);
        btnSaveUsername = (Button) view.findViewById(R.id.btnSaveUsername);
        edUsername.setText(bundle.getString("USER_NAME"));
        int user_id = bundle.getInt("USER_ID");

        btnSaveUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()==true) {
                    Call<JsonObject> call = getUserService().updateUserName(edUsername.getText().toString(), user_id);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            isUserInfoChange = true;
                            Toast.makeText(getActivity(), R.string.Saved, Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                            Log.d(TAG, "onResponse: " + response.body());
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });
    }
    private boolean validation(){
        uname= edUsername.getText().toString();
        if(uname.isEmpty()){
            edUsername.setError(getActivity().getResources().getString(R.string.PleaseTypeUsername));
            return false;
        }
        return true;
    }

    private UserService getUserService() {
        UserService userService = ApiDataCompromiser.createService(UserService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return userService;
    }

    @Override
    public void onResume() {
        changeAppStyle();
        super.onResume();
    }

    public void changeAppStyle() {
        setToolbarNoOptionMenuStyle(toolbar, imageViewBackNav, tvToolbarTitle);
        setEditTextStyle(edUsername);
        setTextViewColorAsTextHintColor(textViewDes);
        setGradientButton(btnSaveUsername);
    }
}