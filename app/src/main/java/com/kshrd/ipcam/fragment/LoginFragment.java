package com.kshrd.ipcam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.gson.internal.LinkedTreeMap;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.HomeActivity;
import com.kshrd.ipcam.entities.UserPreference;
import com.kshrd.ipcam.entities.respone.ResponseObject;
import com.kshrd.ipcam.entities.user.User;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.Internet;
import com.kshrd.ipcam.network.controller.CallBack.UserCallback;
import com.kshrd.ipcam.network.services.UserService;
import com.kshrd.ipcam.main.MyFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Miss Chea Navy on 1/3/2017.
 */


public class LoginFragment extends MyFragment {
    private UserCallback userCallback;
    private User user;
    private ArrayList<User> users;
    private Button btnLogin;
    private EditText edEmail ,edPassword;
    private TextView txt_forgot_password;
    private String email,password;
    private View view;
    private String TAG = "LoginFragment";
    private UserPreference userPreference = new UserPreference();
    private  CallbackManager callbackManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_login, null);

        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();

        btnLogin = (Button)view.findViewById(R.id.btn_login);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edEmail = (EditText) view.findViewById(R.id.edEmail);
        edPassword = (EditText) view.findViewById(R.id.edChPassword);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        txt_forgot_password = (TextView) view.findViewById(R.id.txt_forgot_password);
        btnLogin.setOnClickListener(v -> {

            try{
                email = edEmail.getText().toString();
                password = edPassword.getText().toString();
                Log.d(TAG, "onViewCreated: "+validation());
                Log.d(TAG, "onViewCreated: "+email);
                Log.d(TAG, "onViewCreated: "+password);
                if(validation()==true){

                    Call<ResponseObject<User>> call = getUserService().getUserByEmail(email);
                    call.enqueue(new Callback<ResponseObject<User>>() {
                        @Override
                        public void onResponse(Call<ResponseObject<User>> call, Response<ResponseObject<User>> response) {
                           try {
                               LinkedTreeMap t = (LinkedTreeMap) response.body().getData();
                               User user1 = new User();
                               user1.setEmail((String)t.get("EMAIL"));
                               user1.setPassword((String)t.get("PASSWORD"));

                               Log.d(TAG, "onViewCreated: "+user1.getPassword());
                               if(user1.getPassword().equals(password)){


                                   Intent intent = new Intent(getActivity(), HomeActivity.class);
                                   intent.putExtra("EMAIL", user1.getEmail());
                                   userPreference.setPreference(getActivity(), user1.getEmail(),"EMAIL");
                                    getActivity().finish();
                                   startActivity(intent);
                               }else{
                                   edPassword.setError("Password is not match,Sir !");
                               }
                           }
                           catch (Exception e){
                          //     Toast.makeText(getActivity(),"Login Fail",Toast.LENGTH_SHORT);
                               e.printStackTrace();
                           }
                        }

                        @Override
                        public void onFailure(Call<ResponseObject<User>> call, Throwable t) {
                            t.printStackTrace();
                            if(getActivity() != null){
                                Toast.makeText(getActivity(),R.string.MakeSureEverythingIsCorrect,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        changeAppStyle();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public boolean validation() {
        final boolean[] status = {true};
        //internet checker
        if (new Internet().isAvailable(getActivity())) {
           try {
               if (email.isEmpty() && edEmail == null) {
                   edEmail.setError(getActivity().getResources().getString(R.string.EmailIsRequired));
                   status[0] = false;
               }


               //Password validation
               if (password.isEmpty() && password == null) {
                   edPassword.setError(getActivity().getResources().getString(R.string.PasswordIsRequired));
                   status[0] = false;
               } else {
                   if (password.length() <= 7) {
                       edPassword.setError(getActivity().getResources().getString(R.string.PasswordRequiredAtLeastLength8));
                       status[0] = false;
                   }
               }

           }
           catch (Exception e){
               Toast.makeText(getActivity(),R.string.SomethingWentWrong,Toast.LENGTH_SHORT).show();
           }

        }
        else {
            Toast.makeText(getActivity(),R.string.NetworkIsNotAvailable,Toast.LENGTH_SHORT).show();
            status[0]=false;
            return  status[0];
        }


        Call<Boolean> call = getUserService().emailChecker(email);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
               Log.d("Checker", "onResponse: " + response.body());

                if (response.body() != null && response.body().booleanValue() == true) {
                    status[0] = true;
                } else {
                    edEmail.setError(getActivity().getResources().getString(R.string.EmailIsNotRegisterYetQuestion));
                    status[0] = false;
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
        Log.d("VD-D", "validation: "+status[0]);
        return status[0];
    }

    UserService getUserService(){
        UserService userService = ApiDataCompromiser.createService(UserService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return userService;
    }


    public void changeAppStyle(){
        setEditTextStyle(edEmail,edPassword);
        setTextViewColorAsTextHintColor(txt_forgot_password);
        setGradientButton(btnLogin);
    }
}

