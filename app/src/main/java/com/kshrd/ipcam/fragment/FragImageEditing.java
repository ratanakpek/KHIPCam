package com.kshrd.ipcam.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.Callback.UserProfilePasser;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.services.UserService;
import com.kshrd.ipcam.main.IPCamTheme;
import com.kshrd.ipcam.main.MyFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 1/5/2017.
 */

public class FragImageEditing extends MyFragment implements UserProfilePasser {

    /**
     * @Filed interface UserImagePasser
     */
    private UserProfilePasser userProfilePasser;
    public static String WORD;
    private Button browseCamera, browseGallery, no_thank;
    private CircleImageView imgview;
    private TextView textDes;
    private String picturePath;
    private int user_id;
    private int CAMERA_PICKER = 0, PICKER_GALLERY = 1;
    private Bundle mbundle;
    private String profile_pic;
    private static FragImageEditing instance = null;
    private MultipartBody.Part user_profile;
    public static boolean isUserInfoChange;

    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private ImageView imageViewBackNav;
    private ImageView imageOptionMenu;

    public static FragImageEditing getInstance() {
        try {
            if (instance == null) {
                instance = new FragImageEditing();

                Bundle bundle = new Bundle();
                //    bundle.putString("","");
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
        View rootView = inflater.inflate(R.layout.frag_profile_picture, container, false);
        verifyStorageePermission(getActivity());
        setToolbar(rootView);
        isUserInfoChange = false;
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_with_full_option);
        tvToolbarTitle = (TextView) view.findViewById(R.id.title_toolbar_with_full_option);
        imageViewBackNav = (ImageView) view.findViewById(R.id.back_nav_with_full_option);
        imageOptionMenu = (ImageView) view.findViewById(R.id.menu_toolbar_with_full_option);

        imageViewBackNav.setImageResource(R.drawable.arrow_back_navigation);
        imageOptionMenu.setImageResource(R.drawable.ic_save_black_24dp);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        tvToolbarTitle.setText(R.string.ProfilePicture);
        imageViewBackNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUserInfoChange = true;
                getActivity().onBackPressed();
            }
        });

        imageOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_profile == null) {
                    getActivity().onBackPressed();
                    Toast.makeText(view.getContext(), R.string.Saved, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onOptionsItemSelected: " + user_profile);
                    Call<JsonObject> call = getUserService().updateUserImage(user_profile, user_id);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.d(TAG, "onResponse: " + "Succeessful");
                            isUserInfoChange = true;
                            getActivity().onBackPressed();
                            Toast.makeText(view.getContext(), R.string.Saved, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mbundle = getArguments();
        setToolbar(view);
        browseCamera = (Button) view.findViewById(R.id.camera);
        browseGallery = (Button) view.findViewById(R.id.img_gallery);
        no_thank = (Button) view.findViewById(R.id.no_thank);
        textDes = (TextView) view.findViewById(R.id.text_des);
        imgview = (CircleImageView) view.findViewById(R.id.profile_pic);
        profile_pic = mbundle.getString("IMAGE");
        if (mbundle != null) {
            if (profile_pic.contains("https://graph.facebook.com/"))
                loadUserProfile(imgview, profile_pic);
            else
                loadUserProfile(imgview, ApiGenerator.BASE_URL + profile_pic);

        }

        if (getArguments() != null) {
            user_id = getArguments().getInt("USER_ID");
        }

        browseCamera.setOnClickListener(v -> {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, CAMERA_PICKER);//zero can be replaced with any action code
        });

        browseGallery.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.setType("image/*");
            startActivityForResult(pickPhoto, PICKER_GALLERY);//one can be replaced with any action code
        });

        no_thank.setOnClickListener(v -> {
            WORD = getActivity().getResources().getString(R.string.Cancel);
            getActivity().onBackPressed();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pick imgage from gallary and add to the imageView
        if (requestCode == PICKER_GALLERY && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    //Convert Image Extension to .jpg
                 /*
                    String[] converImagePath=picturePath.split("\\.");
                    String newImagePath = converImagePath[0]+"."+"jpg";*/

                    imgview.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    user_profile = getImage(new File(picturePath));
                    /*userProfilePasser.userProfilePasser(user_id,user_profile);*/


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //take imgae and add to imageView
        else if (requestCode == CAMERA_PICKER && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                try {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imgview.setImageBitmap(photo);
                    Uri selectedImage = getImageUri(getActivity(), photo);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    user_profile = getImage(new File(picturePath));
                    userProfilePasser.userProfilePasser(user_id, user_profile);
                    Log.i(TAG, "onActivityResult: " + getImage(new File((picturePath))));
                    /*user_profile = getImage(new File(picturePath));*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    //Convert image bitmap to Uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    String TAG = "FragImageEditing";

    public MultipartBody.Part getImage(File file) {
        MultipartBody.Part bodyImage = null;
        RequestBody requestBody;
        Image im = null;
        requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        bodyImage = MultipartBody.Part.createFormData("IMAGE", file.getName(), requestBody);
        Log.d(TAG, "getImage: " + bodyImage);
        return bodyImage;
    }


    /**
     * @Method verifyStorageePermission
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStorageePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userProfilePasser = (UserProfilePasser) context;
    }

    public void loadUserProfile(ImageView imageView, String URL) {
        final Bitmap[] theBitmap = {null};

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                try {
                    theBitmap[0] = Glide.
                            with(FragImageEditing.this).
                            load(URL).
                            asBitmap().
                            into(-1, -1).
                            get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void dummy) {
                if (null != theBitmap[0]) {
                    // The full bitmap should be available here
                    imageView.setImageBitmap(theBitmap[0]);
                    Log.d("Image", "Image loaded");
                }
                ;
            }
        }.execute();
    }

    public UserService getUserService() {
        UserService userService = ApiDataCompromiser.createService(UserService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return userService;
    }

    @Override
    public void userProfilePasser(int user_id, MultipartBody.Part user_profile) {
        this.user_profile = user_profile;
        this.user_id = user_id;

        Log.i("ImageChange", "userProfilePasser: " + user_profile);
    }

    public void changeAppStyle() {

        setToolbarWithFullOptionStyle(toolbar, imageViewBackNav, tvToolbarTitle, imageOptionMenu);
        setTextViewCurrentMainColor(textDes);

        imgview.setBorderColor(IPCamTheme.profile_CircleImageBorderColor);
        setButtonBackgroundAndTextColor(browseCamera, browseGallery, no_thank);
        no_thank.setBackground(IPCamTheme.imageEdit_ButtonCancelBackground);
    }

    public void setButtonBackgroundAndTextColor(Button... buttons) {
        for (int i = 0; i < buttons.length; i++) {
            //  buttons[i].setTextColor(IPCamTheme.currentMainColor);
            setTextViewCurrentMainColor(buttons[i]);
            buttons[i].setBackground(IPCamTheme.imageEdit_ButtonBackground);
        }
    }

    @Override
    public void onResume() {
        changeAppStyle();
        super.onResume();
    }
}