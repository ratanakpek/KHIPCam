package com.kshrd.ipcam.fragment;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.HomeActivity;
import com.kshrd.ipcam.entities.UserPreference;
import com.kshrd.ipcam.entities.form.UserInputer;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.Internet;
import com.kshrd.ipcam.network.services.UserService;
import com.kshrd.ipcam.main.MyFragment;

import org.json.JSONObject;
import org.parceler.Parcels;
import java.net.URL;
import java.util.Arrays;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Frag_welcome_screen extends MyFragment {


    private UserPreference userPreference = new UserPreference();

    String TAG="WelcomeScreen";
    LoginButton btnLoginFacebook;
    Button btn_login;
    TextView textViewOr,textViewQuestion,textViewSignUp;
    private static final String PREFS_NAME ="USER_PREF";

    CallbackManager callbackManager;

    public Frag_welcome_screen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        if(new Internet().isAvailable(getActivity())==true){
            facebookSession();
        }else {
            Toast.makeText(getActivity(),R.string.NetworkIsNotAvailable,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_welcome_screen, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        btn_login = (Button) view.findViewById(R.id.btn_login);
        textViewOr = (TextView) view.findViewById(R.id.txt_or);
        textViewQuestion = (TextView) view.findViewById(R.id.txtQuestion);
        textViewSignUp = (TextView) view.findViewById(R.id.tvSignup);

        btnLoginFacebook = (LoginButton)view.findViewById(R.id.btn_login_facebook);
        btnLoginFacebook.setReadPermissions(Arrays.asList("email"));
        btnLoginFacebook.setFragment(this);
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        getFacebookData(object);

                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,last_name,first_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    /**
     *
     * @param object user for inserted to database
     * @return user Object
     */
    String fbPasssword;
    private void   getFacebookData(JSONObject object) {
        UserInputer userInputer = new UserInputer();
        String username="";
        try {
            String id = object.getString("id");
            URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
            if (object.has("first_name"))
                if (object.has("last_name"))
                    username = object.getString("last_name")+" "+object.getString("first_name");
            userInputer.setUsername(username);
            if (object.has("email"))
                userInputer.setEmail(object.get("email").toString());
            userInputer.setImage(profile_pic.toString());
            userInputer.setUser_facebook_id(object.getString("id"));
            //email checker compare email from facebook
            if(userInputer.getEmail() !=null){
                emailChecher(userInputer);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @Method emailChecker user for puporse check mail is existed or not , if not we insert direct to data center
     * @param userInputer reference of userInputer Classs
     */
    void emailChecher(UserInputer userInputer){
        if(new Internet().isAvailable(getActivity())==true){
            Call<Boolean> call = getUserService().emailChecker(userInputer.getEmail());
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.body() !=null && response.body().booleanValue() == true){
                        Log.i(TAG, "onCompleted: this account is register already");
                        userPreference.setPreference(getActivity(), userInputer.getEmail(), "EMAIL");
                        facebookSession();
                    }else {
                        Log.i(TAG, "onCompleted: this account is not existed");
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragFbPasswordConfirm fragFbPasswordConfirm = new FragFbPasswordConfirm();
                        Bundle bundle = new Bundle();
                        Parcelable parcelable = Parcels.wrap(userInputer);
                        bundle.putParcelable("USER_INPUTER",parcelable);
                        fragFbPasswordConfirm.setArguments(bundle);

                        fragmentTransaction.replace(R.id.activity_welcome_screen, fragFbPasswordConfirm, "Log In");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });
        }else {
            Toast.makeText(getActivity(),R.string.NetworkIsNotAvailable,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * get current user facebook in app
     */
    void facebookSession(){
        Log.d(TAG, "facebookSession: "+userPreference.getPreference(getActivity(),"EMAIL"));
        if(AccessToken.getCurrentAccessToken()!=null){
            if(AccessToken.getCurrentAccessToken().getToken() !=  null){
                if(userPreference.getPreference(getActivity(),"EMAIL") != null){
                    openHomeScreen(userPreference.getPreference(getActivity(),"EMAIL").toString());
                }
            }
        }
        else{
            if(!userPreference.getPreference(getActivity(),"EMAIL").isEmpty()){
                openHomeScreen(userPreference.getPreference(getActivity(),"EMAIL").toString());
            }
        }
    }

    UserService getUserService(){
        UserService userService = ApiDataCompromiser.createService(UserService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return userService;
    }

    public void changeAppStyle(){
        setTextViewColorAsTextHintColor(textViewOr,textViewQuestion);
        setGradientButton(btn_login);
        setTextViewCurrentMainColor(textViewSignUp);
    }

    @Override
    public void onResume() {
        changeAppStyle();
        super.onResume();
    }


}
