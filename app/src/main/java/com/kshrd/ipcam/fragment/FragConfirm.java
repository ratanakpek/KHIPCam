package com.kshrd.ipcam.fragment;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.HomeActivity;
import com.kshrd.ipcam.entities.UserPreference;
import com.kshrd.ipcam.entities.form.UserInputer;
import com.kshrd.ipcam.entities.respone.ResponseObject;
import com.kshrd.ipcam.entities.user.User;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.controller.UserController;
import com.kshrd.ipcam.network.services.UserService;
import com.kshrd.ipcam.main.MyFragment;

import org.parceler.Parcels;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragConfirm extends MyFragment {
    private String mParam1;
    private String mParam2;
    private EditText edConfirm;
    private Button btnConfirm;
    private TextView textViewDes;
    private  String TAG = "FragConfirm";

    public FragConfirm() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_code, container, false);
        edConfirm = (EditText)view.findViewById(R.id.edConfirm);
        btnConfirm = (Button)view.findViewById(R.id.btnCodeConfirm);
        textViewDes = (TextView) view.findViewById(R.id.textViewDes);
        btnConfirm.setOnClickListener(v -> {
           Bundle bundle = getArguments();
            if(bundle !=null){
                if(bundle.containsKey("FG_MAIL")){

                    Log.d(TAG, "onCreateView: "+getArguments().get("FG_MAIL"));
                        if(bundle.get("CONFIRM_KEY") != null &&  bundle.get("CONFIRM_KEY").toString().equals(edConfirm.getText().toString())){
                            openChangePasswordFragment(getArguments().get("FG_MAIL").toString());
                        }else {
                            Toast.makeText(getActivity(),R.string.KeyIsNotCorrect,Toast.LENGTH_SHORT).show();
                        }

                }else if(bundle.containsKey("ADD_USER")) {
                    Log.d(TAG, "CONFIRM_KEY: "+getArguments().get("CONFIRM_KEY"));
                    if(getArguments().get("CONFIRM_KEY") != null && edConfirm.getText().toString().equals(getArguments().get("CONFIRM_KEY").toString())){

                         UserInputer user =   Parcels.unwrap((bundle).getParcelable("ADD_USER"));

                          new UserController().addUser(user.getUsername(),user.getEmail(),user.getPassword(),
                                  getImage(new File(user.getImage())),2);
                            new UserPreference().setPreference(getActivity(),user.getEmail(),"EMAIL");
                            openHomeActivity(user.getEmail());
                      }else {
                          Toast.makeText(getActivity(),R.string.KeyIsNotCorrect,Toast.LENGTH_SHORT).show();
                      }
                }
            }
        });

        return view;
    }
    public void openChangePasswordFragment(String email){
        Call<ResponseObject<User>> call = getUserService().getUserByEmail(email);
        call.enqueue(new Callback<ResponseObject<User>>() {
            @Override
            public void onResponse(Call<ResponseObject<User>> call, Response<ResponseObject<User>> response) {
                LinkedTreeMap t = (LinkedTreeMap) response.body().getData();
                if(t!=null){
                    changePasswordFragment(Double.valueOf(t.get("USER_ID").toString()).intValue()
                                          ,email);
                }
            }

            @Override
            public void onFailure(Call<ResponseObject<User>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(),R.string.MakeSureEverythingIsCorrect,Toast.LENGTH_SHORT).show();
            }
        });

    }

    void changePasswordFragment(int user_id,String email){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragChangePassword fragChangePassword = new FragChangePassword();
        Bundle bundle = new Bundle();
            bundle.putString("FG_MAIL",email);
            bundle.putString("FG_USER_ID",String.valueOf(user_id));
        fragChangePassword.setArguments(bundle);
        fragmentTransaction.replace(R.id.activity_welcome_screen, fragChangePassword, "Log In");
        fragmentTransaction.addToBackStack("FG");
        fragmentTransaction.commit();
    }

    /**
     * @Method Fragment Home
     * @param email
     */
    private void fragHome(String email){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragHomeScreen fragHomeScreen  = new FragHomeScreen();
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL",email);
        fragHomeScreen.setArguments(bundle);
        fragmentTransaction.replace(R.id.activity_welcome_screen, fragHomeScreen, "Home");
        fragmentTransaction.addToBackStack("FH");
        fragmentTransaction.commit();
    }

    /**
     * @Method GetImge
     * @Param File IO : create new File image
     */
    public MultipartBody.Part getImage(File file) {
        MultipartBody.Part bodyImage = null;
        RequestBody requestBody;
        Image im = null;
        requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        bodyImage = MultipartBody.Part.createFormData("IMAGE", file.getName(), requestBody);

        return bodyImage;
    }
    /**
     * @Method openHomeActivity
     */
    private void openHomeActivity(String email){
        Intent intent = new Intent(getActivity(),HomeActivity.class);
        Bundle bundle = new Bundle();
            bundle.putString("EMAIL",email);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    /**
     * @Mehtod getUserSerivice
     * @return UserService
     */
    private UserService getUserService(){
        UserService userService = ApiDataCompromiser.createService(UserService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return userService;
    }

    public void changeAppStyle(){
        setEditTextStyle(edConfirm);
        setTextViewColorAsTextHintColor(textViewDes);
        setGradientButton(btnConfirm);
    }

    @Override
    public void onResume() {
        changeAppStyle();
        super.onResume();
    }
}
