package com.kshrd.ipcam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.EmailSender;
import com.kshrd.ipcam.network.Internet;
import com.kshrd.ipcam.network.services.UserService;
import com.kshrd.ipcam.main.MyFragment;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Miss Chea Navy on 1/2/2017.
 */

public class ForgotPasswordFragment extends MyFragment {
    String TAG = "ForgotPasswordFragment";
    Button btnSender;
    EditText edEmail;
    TextView textViewDes;
    SpotsDialog spotsDialog ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_forgot_password, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spotsDialog = new SpotsDialog(getActivity(),getActivity().getResources().getString(R.string.SendingEmail), R.style.Custom);

        edEmail = (EditText)view.findViewById(R.id.edit_email_reset_pwd);
        textViewDes = (TextView) view.findViewById(R.id.textViewDes);
        btnSender = (Button)view.findViewById(R.id.btnSenderMail);
        btnSender.setOnClickListener(v -> {
            if (new Internet().isAvailable(getActivity()) == true) {
                spotsDialog.setIcon(R.drawable.ic_launcher);
                spotsDialog.show();
                Call<Boolean> call = getUserService().emailChecker(edEmail.getText().toString());
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Log.d(TAG, "onResponse: " + response.body().booleanValue());
                        if (!response.body().booleanValue() == false) {
                           // sendEmail(edEmail.getText().toString());

                          EmailSender  emailSender = new EmailSender();
                            emailSender.execute(edEmail.getText().toString());
                            Bundle bundle = new Bundle();
                            bundle.putString("FG_MAIL",edEmail.getText().toString());
                            bundle.putString("CONFIRM_KEY",emailSender.getGen());
                            Log.d(TAG, "sendEmail: "+emailSender.getGen());
                            spotsDialog.dismiss();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragConfirm fragConfirm = new FragConfirm();
                            fragConfirm.setArguments(bundle);
                            fragmentTransaction.replace(R.id.activity_welcome_screen, fragConfirm , "Confirm Code" );
                            fragmentTransaction.addToBackStack("log");
                            fragmentTransaction.commit();


                        } else {
                            Toast.makeText(getActivity(), R.string.MakeSureYouHadRegisteredAccount, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
            }else {
                Toast.makeText(getActivity(),R.string.NetworkIsNotAvailable,Toast.LENGTH_SHORT).show();
            }
        });
    }
    UserService getUserService(){
        UserService userService = ApiDataCompromiser.createService(UserService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return userService;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * @Method sendEmail
     * @param receiver person that  receive key
     */


    void sendEmail(String receiver){
            Bundle bundle = new Bundle();
            bundle.putString("FG_MAIL",receiver);/*
            bundle.putString("CONFIRM_KEY",emailSender.getGen());
            Log.d(TAG, "sendEmail: "+emailSender.getGen());*/
            spotsDialog.dismiss();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragConfirm fragConfirm = new FragConfirm();
            fragConfirm.setArguments(bundle);
            fragmentTransaction.replace(R.id.activity_welcome_screen, fragConfirm , "Confirm Code" );
            fragmentTransaction.addToBackStack("log");
            fragmentTransaction.commit();
    }

    public void changeAppStyle(){
        setEditTextStyle(edEmail);
        setTextViewColorAsTextHintColor(textViewDes);
        setGradientButton(btnSender);
    }

    @Override
    public void onResume() {
        changeAppStyle();
        super.onResume();
    }
}
