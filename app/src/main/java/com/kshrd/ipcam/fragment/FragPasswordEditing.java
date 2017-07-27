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
 * Created by User on 1/16/2017.
 */

public class FragPasswordEditing extends MyFragment {

    EditText edOldPassword, edNewPassword, edConNewPassword;
    TextView textViewDes;
    String oldPassword, newPassword, ConfirmNewPassword;
    Button btnSavePassword;
    static Bundle bundle;
    private String TAG  = "FragPasswordEditing";
    public static boolean isUserInfoChange;

    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private ImageView imageViewBackNav;

    private static FragPasswordEditing instance = null;

    public static FragPasswordEditing getInstance(){
        try {
            if (instance == null){
                instance = new FragPasswordEditing();

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
        View rootView = inflater.inflate(R.layout.frag_profile_password_edited, container, false);
        setToolbar(rootView);
        isUserInfoChange = false;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return rootView;
    }

    public void setToolbar(View view){
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_with_title_and_navigation);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        tvToolbarTitle= (TextView) view.findViewById(R.id.toolbar_with_title_and_navigation_title);
        imageViewBackNav = (ImageView) view.findViewById(R.id.navigation_back_action_bar_with_title_and_navigation);
        tvToolbarTitle.setText(R.string.Password);
        imageViewBackNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEditText();
                hideSoftKeyboard(getActivity());
                getActivity().onBackPressed();
            }
        });
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSavePassword = (Button) view.findViewById(R.id.btnSavePassword);

        edOldPassword = (EditText) view.findViewById(R.id.edOldPassword);
        edNewPassword = (EditText) view.findViewById(R.id.edNewPassword);
        edConNewPassword = (EditText) view.findViewById(R.id.edReEnterPassword);
        textViewDes = (TextView) view.findViewById(R.id.textViewDes);

        if(getArguments()!=null){
            bundle = getArguments();
            Log.d(TAG, "onViewCreated: "+bundle.getString("PASSWORD"));
        }
        btnSavePassword.setOnClickListener(v -> {
                if(validation()==true){
                    Call<JsonObject> call = getUserService().updateUserPassword(newPassword,bundle.getInt("USER_ID"));
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Toast.makeText(getActivity(),R.string.Saved,Toast.LENGTH_SHORT).show();
                            clearEditText();
                            isUserInfoChange = true;
                            getActivity().onBackPressed();


                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
        });
    }

    public boolean validation(){
        oldPassword= edOldPassword.getText().toString();
        newPassword= edNewPassword.getText().toString();
        ConfirmNewPassword= edConNewPassword.getText().toString();
        if(oldPassword.isEmpty() && newPassword.isEmpty() && ConfirmNewPassword.isEmpty() ){
            edOldPassword.setError(getActivity().getResources().getString(R.string.PleaseTypeOldPassword));
            edNewPassword.setError(getActivity().getResources().getString(R.string.PleaseTypeNewPassword));
            edConNewPassword.setError(getActivity().getResources().getString(R.string.PleaseReTypeNewPassword));
            return false;
        }
        else {
            if (oldPassword.isEmpty()) {
                edOldPassword.setError(getActivity().getResources().getString(R.string.PleaseTypeOldPassword));
                return false;
            } else if (!oldPassword.equals(bundle.getString("PASSWORD"))) {
                edOldPassword.setError(getActivity().getResources().getString(R.string.PasswordIsNotMatched));
                return false;
            }

            if (newPassword.isEmpty()) {
                edNewPassword.setError(getActivity().getResources().getString(R.string.PleaseReTypeNewPassword));
                return false;
            } else if (newPassword.length() < 7) {
                edNewPassword.setError(getActivity().getResources().getString(R.string.PasswordRequiredAtLeastLength8));
                return false;
            }

            if (ConfirmNewPassword.isEmpty()) {
                edConNewPassword.setError(getActivity().getResources().getString(R.string.PleaseReTypeNewPassword));
                return false;
            } else if (!ConfirmNewPassword.equals(newPassword)) {
                edConNewPassword.setError(getActivity().getResources().getString(R.string.PasswordIsNotMatched));
                return false;
            }
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
    private void clearEditText(){
        edOldPassword.setText("");
        edNewPassword.setText("");
        edConNewPassword.setText("");
        edOldPassword.setError(null);
        edNewPassword.setError(null);
        edConNewPassword.setError(null);
    }

    public void changeAppStyle(){
        setToolbarNoOptionMenuStyle(toolbar,imageViewBackNav,tvToolbarTitle);
        setEditTextStyle(edOldPassword,edNewPassword,edConNewPassword);
        setTextViewColorAsTextHintColor(textViewDes);
        setGradientButton(btnSavePassword);
    }

    @Override
    public void onResume() {
        changeAppStyle();
        super.onResume();
    }


}
