package com.kshrd.ipcam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.HomeActivity;
import com.kshrd.ipcam.entities.UserPreference;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.Internet;
import com.kshrd.ipcam.network.services.UserService;
import com.kshrd.ipcam.main.MyFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragChangePassword extends MyFragment {
    private String TAG = "FragChangePassword";
    private Button btnDone;
    private EditText edPassword,edRePassword;
    public FragChangePassword() {
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
        View view = inflater.inflate(R.layout.fragment_frag_change_password, container, false);
        edPassword = (EditText)view.findViewById(R.id.edChPassword);
        edRePassword = (EditText)view.findViewById(R.id.edChRePassword);
        btnDone = (Button)view.findViewById(R.id.btnChDone);
        btnDone.setOnClickListener(v -> {
            if(validation()==true){
                Bundle bundle =  getArguments();
                int user_id = Integer.valueOf(bundle.get("FG_USER_ID").toString());
                Call<JsonObject> call = getUserService().updateUserPassword(edPassword.getText().toString(),user_id);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d(TAG, "onResponse: "+response.body());
                        openHomeScreen(bundle.get("FG_MAIL").toString());
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
        return view;
    }
    private UserService getUserService(){
        UserService userService = ApiDataCompromiser.createService(UserService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return userService;
    }

    /**
     * @return true if validate is match, otherwise false
     * @Methd Validate data
     */
    boolean validation() {
         boolean status = true;
        //usernmae
        if (new Internet().isAvailable(getActivity())) {

            //Password validation
            if (edPassword.getText().toString().isEmpty()) {
                edPassword.setError(getActivity().getResources().getString(R.string.PasswordIsRequired));
               return false;
            } else {
                if (edPassword.getText().toString().length() <= 7) {
                    edPassword.setError(getActivity().getResources().getString(R.string.PasswordRequiredAtLeastLength8));
                    return  false;
                }
            }

            //Confirm Password validation
            if (edRePassword.getText().toString().isEmpty()) {
                edRePassword.setError(getActivity().getResources().getString(R.string.RetypYourPasswordHereToConfirm));
                return  false;
            } else {
                if (!edPassword.getText().toString().equals(edRePassword.getText().toString())) {
                    edRePassword.setError(getActivity().getResources().getString(R.string.PasswordIsNotMatched));
                    return  false;
                }
            }
        }
        else {
            Toast.makeText(getActivity(),R.string.NetworkIsNotAvailable,Toast.LENGTH_SHORT).show();
            return  false;
        }
        return status;
    }

    private void  openHomeScreen(String email){
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("EMAIL",email);
        new UserPreference().setPreference(getActivity(),"EMAIL",email);
        Log.i("Email", "openHomeScreen: "+email);
        startActivity(intent);
        getActivity().finish();
    }

    public void changeAppStyle(){
      //  getActivity().getWindow().getDecorView().setBackgroundColor(IPCamTheme.windowColor);
        setEditTextStyle(edPassword,edRePassword);
        setGradientButton(btnDone);
    }

    @Override
    public void onResume() {
        changeAppStyle();
        super.onResume();
    }
}
