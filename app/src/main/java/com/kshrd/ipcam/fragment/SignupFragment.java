package com.kshrd.ipcam.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.REQUEST_INTENT;
import com.kshrd.ipcam.entities.UserPreference;
import com.kshrd.ipcam.entities.form.UserInputer;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.EmailSender;
import com.kshrd.ipcam.network.Internet;
import com.kshrd.ipcam.network.services.UserService;
import com.kshrd.ipcam.main.MyFragment;

import org.parceler.Parcels;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Miss Chea Navy on 1/1/2017.
 */

/*
String username, String email, String password,
        MultipartBody.Part image, int id
*/
public class SignupFragment extends MyFragment/* implements signupCommunication*/ {

    private EditText editUsername, editEmail, editPassword, editConfirmPassword;
    private ImageView imgProfile;
    private Button btnSignup;
    private Uri data;
    private File pictureDir;
    private String picturePath="";
    private static int request_image = 192;
    private String userName, email, password, confirmPassword;
    private UserPreference userPreference = new UserPreference();
    private String TAG = "SignupFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_signup, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        verifyStoragePermissions(getActivity());
        imgProfile = (ImageView) view.findViewById(R.id.
                img_profile);
        editUsername = (EditText)view.findViewById(R.id.edit_username);
        editEmail = (EditText) view.findViewById(R.id.edit_email);
        editPassword = (EditText) view.findViewById(R.id.edit_password);
        editConfirmPassword = (EditText) view.findViewById(R.id.edit_confirm_password);
        btnSignup = (Button) view.findViewById(R.id.btn_login);

        imageUserProfile();

        btnSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(editUsername.getText()!=null && editEmail.getText()!=null && editPassword !=null
                    && editConfirmPassword != null) {
                    userName = editUsername.getText().toString();
                    email = editEmail.getText().toString();
                    password = editPassword.getText().toString();
                    confirmPassword = editConfirmPassword.getText().toString();

                    if (validation() == true) {
                        try {
                            //new UserController().addUser(userName, email, password, getImage(new File(picturePath)), 2);
                            UserInputer userInputer = new UserInputer();
                               userInputer.setUsername(userName);
                               userInputer.setEmail(email);
                               userInputer.setPassword(password);
                               userInputer.setImage(picturePath);
                            Parcelable  parcel = Parcels.wrap(userInputer);

                            Log.d("SignupFragment", "onClick: "+userInputer.getEmail());
                            EmailSender emailSender = new EmailSender();
                            Bundle bundle = new Bundle();
                                 bundle.putParcelable("ADD_USER",parcel);
                                 bundle.putString("CONFIRM_KEY", emailSender.getGen());
                                emailSender.execute(userInputer.getEmail());

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragConfirm  fragConfirm = new FragConfirm();
                                fragConfirm.setArguments(bundle);

                            fragmentTransaction.replace(R.id.activity_welcome_screen, fragConfirm, "Log_In");
                            fragmentTransaction.addToBackStack("FGSU");
                            fragmentTransaction.commit();

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), R.string.PleaseSelectYourImageProfile, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
    }



    Bitmap bitmap;
    Intent imageIntent;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: "+requestCode);
        if (resultCode != Activity.RESULT_CANCELED) {
            if (data != null) {
                imageIntent=data;
                Log.i(TAG, "onActivityResult: "+imageIntent);
                if (requestCode == REQUEST_INTENT.IMAGE_PICKER_REQUEST && resultCode == getActivity().RESULT_OK ) {
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        imgProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }




    private void imageUserProfile() {
        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            pictureDir.getParentFile().mkdirs();
            picturePath = pictureDir.getPath();
            data = Uri.parse(picturePath);
            intent.setDataAndType(data, "image/*");
            startActivityForResult(intent, REQUEST_INTENT.IMAGE_PICKER_REQUEST);
        });
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * @return true if validate is match, otherwise false
     * @Methd Validate data
     */
    boolean validation() {
        final boolean[] emailState = {true};
        //usernmae
        if (new Internet().isAvailable(getActivity())) {
            //Image Checker
            if (TextUtils.isEmpty(userName)) {
                editUsername.setError(getActivity().getResources().getString(R.string.PleaseFillTheBlank));
                emailState[0] = false;
            } else {
                if (userName.length() <= 3) {
                    editUsername.setError(getActivity().getResources().getString(R.string.InvalidUsername));
                    emailState[0] = false;
                }
            }

            //Email validation
            //  Log.d("jhhjhhj", "validation: "+email.substring(email.indexOf("@") + 1, email.indexOf(".")));
            //Server email checker
            Call<Boolean> call = getUserService().emailChecker(email);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    emailState[0] = response.body().booleanValue();
                    Log.i(TAG, "onResponse: "+response.body().booleanValue());
                    if(emailState[0] == true){
                        editEmail.setError(getActivity().getResources().getString(R.string.EmailIsExisted));
                        emailState[0] = false;
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });


            if (email.isEmpty()) {
                editEmail.setError(getActivity().getResources().getString(R.string.EmailIsRequired));
                emailState[0] = false;
            } else {
                if (email == null || !email.contains("@") ||
                        email.substring(0, email.lastIndexOf("@")).isEmpty() ||
                        email.substring(0, email.lastIndexOf("@")).length() <= 3) {
                    editEmail.setError(getActivity().getResources().getString(R.string.IncorrectEmailAddress));
                    emailState[0] = false;
                } else if (!email.contains(".")) {
                    editEmail.setError(getActivity().getResources().getString(R.string.Missing)+" . ?");
                    emailState[0] = false;
                } else if (!email.substring(email.indexOf(".") + 1).equals("com")) {
                    editEmail.setError(getActivity().getResources().getString(R.string.Missing)+" .com ?");
                    emailState[0] = false;
                } else if (!email.substring(email.indexOf("@") + 1, email.indexOf(".")).equals("gmail")
                        && !email.substring(email.indexOf("@") + 1, email.indexOf(".")).equals("yahoo")
                        && !email.substring(email.indexOf("@") + 1, email.indexOf(".")).equals("live")
                        && !email.substring(email.indexOf("@") + 1, email.indexOf(".")).equals("hotmail")) {

                    editEmail.setError(getActivity().getResources().getString(R.string.PleaseIndentifyEmailService));
                    emailState[0] = false;
                } else if (!email.substring(0, email.lastIndexOf("@")).contains(".") || email.substring(0, email.lastIndexOf("@")).contains(".")) {
                } else {
                    editEmail.setError(getActivity().getResources().getString(R.string.ErrorMissingDotBeforeCom));
                    emailState[0] = false;
                }

            }



            //Password validation
            if (password.isEmpty()) {
                editPassword.setError(getActivity().getResources().getString(R.string.PasswordIsRequired));
                emailState[0] = false;
            } else {
                if (password.length() <= 7) {
                    editPassword.setError(getActivity().getResources().getString(R.string.PasswordRequiredAtLeastLength8));
                    emailState[0] = false;
                }
            }

            //Confirm Password validation
            if (confirmPassword.isEmpty()) {
                editConfirmPassword.setError(getActivity().getResources().getString(R.string.RetypYourPasswordHereToConfirm));
                emailState[0] = false;
            } else {
                if (!password.equals(confirmPassword)) {
                    editConfirmPassword.setError(getActivity().getResources().getString(R.string.PasswordIsNotMatched));
                    emailState[0] = false;
                }
            }
        }
        else {
            Toast.makeText(getActivity(),R.string.NetworkIsNotAvailable,Toast.LENGTH_SHORT).show();
            return  false;
        }

        if(imageIntent == null){
            Toast.makeText(getActivity(),R.string.PleaseUploadImageByClickAvatarIcon,Toast.LENGTH_SHORT).show();
            return  false;
        }

        return emailState[0];
    }


    @Override
    public void onResume() {
        changeAppStyle();
        super.onResume();

        if("" != picturePath){
            imgProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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
        setEditTextStyle(editUsername,editEmail,editPassword,editConfirmPassword);
        setGradientButton(btnSignup);
    }
}
