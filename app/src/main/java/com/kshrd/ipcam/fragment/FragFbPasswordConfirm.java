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

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.HomeActivity;
import com.kshrd.ipcam.entities.UserPreference;
import com.kshrd.ipcam.entities.form.UserInputer;
import com.kshrd.ipcam.network.Internet;
import com.kshrd.ipcam.network.controller.UserController;
import com.kshrd.ipcam.main.MyFragment;

import org.parceler.Parcels;


public class FragFbPasswordConfirm extends MyFragment {

    // TODO: Rename and change types of parameters
    private Button btnConfirm;
    private EditText edPassword;
    private EditText edRePassword;
    private TextView textViewDes;
    private UserPreference userPreference = new UserPreference();
    public FragFbPasswordConfirm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragFbPasswordConfirm.
     */
    // TODO: Rename and change types and number of parameters
    public static FragFbPasswordConfirm newInstance(String param1, String param2) {
        FragFbPasswordConfirm fragment = new FragFbPasswordConfirm();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_fb_password_confirm,container,false);
          btnConfirm = (Button) view.findViewById(R.id.btn_fb_confirm);
         edPassword= (EditText)view.findViewById(R.id.ed_fb_password);
         edRePassword = (EditText)view.findViewById(R.id.ed_confirm_facebook);
        textViewDes = (TextView) view.findViewById(R.id.textViewDes);
        Log.d("GOGO", "onCreateView: "+btnConfirm);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = getArguments();
                    UserInputer userInputer = Parcels.unwrap((bundle).getParcelable("USER_INPUTER"));
                    userPreference.setPreference(getActivity(), userInputer.getEmail(), "EMAIL");
                    confirm(userInputer);
                }
            });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * @Method puporse for insert facebook data to data center
     * @param userInputer reference of userInputer class
     */
    void confirm(UserInputer userInputer){
        Internet internet = new Internet();

       if(validation()==true){
           userInputer.setPassword(edPassword.getText().toString());
           new UserController().addUserFacebookAccount(userInputer.getUsername(),
                   userInputer.getEmail(),
                   userInputer.getPassword(),
                   userInputer.getImage(),
                   userInputer.getUser_facebook_id());
           openHomeScreen(userInputer.getEmail());
       }

    }

    boolean validation() {
         boolean status = true;
        //usernmae
        if (new Internet().isAvailable(getActivity())) {
            //Password validation
            if (edPassword.getText().toString().isEmpty()) {
                edPassword.setError(getActivity().getResources().getString(R.string.PasswordIsRequired));
               return  false;
            } else {
                if (edPassword.getText().toString().length() <= 7) {
                    edPassword.setError(getActivity().getResources().getString(R.string.PasswordRequiredAtLeastLength8));
                    return false;
                }
            }

            //Confirm Password validation
            if (edRePassword.getText().toString().isEmpty()) {
                edRePassword.setError(getActivity().getResources().getString(R.string.RetypYourPasswordHereToConfirm));
                status = false;
            } else {
                if (!edPassword.getText().toString().equals(edRePassword.getText().toString())) {
                    edRePassword.setError(getActivity().getResources().getString(R.string.PasswordIsNotMatched));
                    status = false;
                }
            }
            if(edRePassword.getText().toString().length()<=7){
                edRePassword.setText(R.string.PasswordRequiredAtLeastLength8);
            }
            if(edPassword.getText().toString().length()<=7){
                edPassword.setText(R.string.PasswordRequiredAtLeastLength8);
            }
        }
        else {
            Toast.makeText(getActivity(),R.string.NetworkIsNotAvailable,Toast.LENGTH_SHORT).show();
            status =false;
            return  status;
        }
        return status;
    }


    /**
     * @Method open home Activity
     * @param email
     */
    private void  openHomeScreen(String email){
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("EMAIL",email);
        Log.i("Email", "openHomeScreen: "+email);

        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public void changeAppStyle(){
        setEditTextStyle(edPassword,edRePassword);
        setTextViewColorAsTextHintColor(textViewDes);
        setGradientButton(btnConfirm);
    }

    @Override
    public void onResume() {
        changeAppStyle();
        super.onResume();
    }
}
