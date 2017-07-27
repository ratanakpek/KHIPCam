package com.kshrd.ipcam.activitys;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.adapter.Communicator;
import com.kshrd.ipcam.adapter.MyPagerAdapter;
import com.kshrd.ipcam.entities.camera.IPCam;
import com.kshrd.ipcam.entities.user.User;
import com.kshrd.ipcam.fragment.FragHomeScreen;
import com.kshrd.ipcam.fragment.FragImageEditing;
import com.kshrd.ipcam.fragment.FragPasswordEditing;
import com.kshrd.ipcam.fragment.FragUsernameEditing;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.controller.CallBack.UserCallback;
import com.kshrd.ipcam.network.controller.UserController;
import com.kshrd.ipcam.main.IPCamTheme;
import com.kshrd.ipcam.main.MyActivity;

import org.parceler.Parcels;


public class HomeActivity extends MyActivity implements NavigationView.OnNavigationItemSelectedListener, Communicator, UserCallback {
    private static final int IO_BUFFER_SIZE = 10;
    private String TAG = "HOME_ACTIVITY";
    private Intent intentActivity = null;
    private FragmentTransaction ft = null;
    private TextView tvToolbarTitle;
    private AHBottomNavigation bottomNavigation = null;
    private AHBottomNavigationItem bnHome, bnVideo, bnImage, bnHelp;
    private ViewPager vpPager;
    private MyPagerAdapter myPagerAdapter;
    private Toolbar toolbar;
    private static final String PREFS_NAME = "USER_PREF";
    ImageView imUserProfile;
    TextView tvUsername;
    TextView tvUserEmail;
    NavigationView navigationView;
    View navHeader;
    Bundle bundle = null;
    public static final String CAMERA_PARCELABLE_HOME = "CAMERA_OBJECT_FROM_HOME";
    private User myUser;
    private ImageView imageOptionMenu;
    private ImageView imageViewBackNav;
    public static boolean isThemeChange;
    public static boolean isLanguageChange, isConfigurationChange;
    private MaterialDialog confimrChangeStyleDialog;

    public static final int HOME_TAB = 0;
    public static final int VIDEO_TAB = 1;
    public static final int IMAGE_TAB = 2;
    public static final int HELP_TAB = 3;

    private static final String USRE_PROFILE_KEY = "PROFILE";
    private static final String USRE_NAME_KEY = "USERNAME";
    private static final String USRE_EMAIL_KEY = "USER_EMAIL";

    public static int oldTheme;

    public void changeAppStyle(){

        IPCamTheme.getInstance().setNewTheme(this);
        getWindow().getDecorView().setBackgroundColor(IPCamTheme.windowColor);

        //AppBar
        setToolbarWithFullOptionStyle(toolbar,imageViewBackNav,tvToolbarTitle,imageOptionMenu);

        // Navigation Header
        setNavigationDrawerStyle(navHeader,tvUsername,tvUserEmail,navigationView);

        setBottomNavigationStyle(bottomNavigation);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage(this);
        setContentView(R.layout.activity_home);

        navigationView = (NavigationView) findViewById(R.id.nav_view_home);
        navHeader = navigationView.getHeaderView(0);
        imUserProfile = (ImageView) navHeader.findViewById(R.id.imageUserProfile);
        tvUsername = (TextView) navHeader.findViewById(R.id.tvUsername);
        tvUserEmail = (TextView) navHeader.findViewById(R.id.tvUserEmail);

        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            Log.i(TAG, "onCreate: savedInstanceState " + savedInstanceState);
            new UserController().getUserByEmail(bundle.get("EMAIL").toString(), this);
        }

        setToolbarAndNavigationDrawer();

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setOffscreenPageLimit(3);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        vpPager.setAdapter(myPagerAdapter);
        setSelectedTab();

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

                changeLanguage(HomeActivity.this);

                if (position == HOME_TAB) {
                    imageOptionMenu.setVisibility(View.VISIBLE);
                    FragHomeScreen.getInstance().onResume();
                } else {
                    imageOptionMenu.setVisibility(View.INVISIBLE);
                    FragHomeScreen.getInstance().onPause();
                }

                switch (position) {
                    case HOME_TAB:
                        tvToolbarTitle.setText(R.string.Home);
                        break;
                    case VIDEO_TAB:
                        tvToolbarTitle.setText(R.string.Video);
                        break;
                    case IMAGE_TAB:
                        tvToolbarTitle.setText(R.string.Image);
                        break;
                    case HELP_TAB:
                        tvToolbarTitle.setText(R.string.Help);
                        break;
                    default:

                }

                bottomNavigation.setCurrentItem(position);
                //  myPagerAdapter.getRegisteredFragment(position);
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        changeAppStyle();
        oldTheme = IPCamTheme.getInstance().getCurrentTheme(this);
    }

    public void setToolbarAndNavigationDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_with_full_option);
        tvToolbarTitle = (TextView) findViewById(R.id.title_toolbar_with_full_option);
        imageViewBackNav = (ImageView) findViewById(R.id.back_nav_with_full_option);
        imageOptionMenu = (ImageView) findViewById(R.id.menu_toolbar_with_full_option);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view_home);

        setSupportActionBar(toolbar);
        tvToolbarTitle.setText(R.string.Home);
        imageViewBackNav.setImageResource(R.drawable.menu_nav_icon);
        navigationView.setNavigationItemSelectedListener(this);

        imageViewBackNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        imageOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myUser != null){
                    intentActivity = new Intent(getApplicationContext(),ConfigCameraActivity.class);
                    intentActivity.putExtra("USER_ID",myUser.getUser_id());
                    startActivity(intentActivity);
                }
            }
        });
    }

    public void setSelectedTab() {
        int selectedTabIndex = getIntent().getIntExtra("SELECTED_TAB_EXTRA_KEY", HOME_TAB);
        if (selectedTabIndex != 0) {
            vpPager.setCurrentItem(selectedTabIndex);
            setBottomNavigation(selectedTabIndex);
        } else {
            setBottomNavigation(0);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_user_profile:
                if(myUser != null){
                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putInt("USER_ID", myUser.getUser_id());
                    bundle.putString("USER_NAME", myUser.getUsername());
                    bundle.putString("USER_EMAIL", myUser.getEmail());
                    bundle.putString("PASSWORD", myUser.getPassword());
                    bundle.putString("IMAGE", myUser.getImage());
                    intentActivity = new Intent(this, Profile.class);
                    intentActivity.putExtras(bundle);
                }else {
                    Toast.makeText(this, R.string.CannotGetUserData, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_manage_camera:
                if(myUser != null){
                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putInt("USER_ID", myUser.getUser_id());
                    intentActivity = new Intent(this, ListCameraActivity.class);
                    intentActivity.putExtras(bundle);
                }else {
                    Toast.makeText(this, R.string.CannotGetUserData, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_theme:
                intentActivity = new Intent(this, ThemeActivity.class);
              //  intentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.nav_setting:
                intentActivity = new Intent(this, SettingActivity.class);
                break;
            case R.id.nav_logout:
                this.getSharedPreferences(PREFS_NAME, 0).edit().clear().commit();
                LoginManager.getInstance().logOut();
                intentActivity = new Intent(this, WelcomeScreen.class);
                finish();
                break;
        }

        if (intentActivity != null) {
            startActivity(intentActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setBottomNavigation(int currentItem) {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        if (bnHelp == null && bnVideo == null && bnImage == null && bnHelp == null) {
            bnHome = new AHBottomNavigationItem(R.string.Home, R.drawable.ic_home_black_24dp, R.color.bottomNavigationAndActionBarColor);
            bnVideo = new AHBottomNavigationItem(R.string.Video, R.drawable.ic_videocam_black_24dp, R.color.bottomNavigationAndActionBarColor);
            bnImage = new AHBottomNavigationItem(R.string.Image, R.drawable.ic_photo_black_24dp, R.color.bottomNavigationAndActionBarColor);
            bnHelp = new AHBottomNavigationItem(R.string.Help, R.drawable.ic_help_black_24dp, R.color.bottomNavigationAndActionBarColor);
        }


        bottomNavigation.addItem(bnHome);
        bottomNavigation.addItem(bnVideo);
        bottomNavigation.addItem(bnImage);
        bottomNavigation.addItem(bnHelp);

        bottomNavigation.setCurrentItem(currentItem);
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.bottomNavigationAndActionBarColor));
        bottomNavigation.setAccentColor(getResources().getColor(R.color.AppTextColor));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.BottomNavigationInActiveTextColor));
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                changeLanguage(HomeActivity.this);
                switch (position) {
                    case 0:
                        vpPager.setCurrentItem(position);
                        tvToolbarTitle.setText(R.string.Home);
                        break;
                    case 1:
                        vpPager.setCurrentItem(position);
                        tvToolbarTitle.setText(R.string.Video);
                        break;
                    case 2:
                        vpPager.setCurrentItem(position);
                        tvToolbarTitle.setText(R.string.Image);
                        break;
                    case 3:
                        vpPager.setCurrentItem(position);
                        tvToolbarTitle.setText(R.string.Help);
                        break;
                }
                return true;
            }
        });
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
                            with(HomeActivity.this).
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
                    Log.d(TAG, "Image loaded");
                }
                ;
            }
        }.execute();
    }

    @Override
    public void loadUserByEmail(User user) {

        if (user != null) {

            myUser = new User();
            myUser.setUser_id(user.getUser_id());
            myUser.setUsername(user.getUsername());
            myUser.setPassword(user.getPassword());
            myUser.setEmail(user.getEmail());
            myUser.setImage(user.getImage());
            changeUserInfo(user.getUsername(), user.getEmail(), user.getImage());
        }
    }

    @Override
    public void loadUserById(User user) {

    }

    @Override
    public void emailCheckerState(Boolean status) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
/*

        if(outState ==null || outState.getString(USRE_NAME_KEY) == null){
            outState.putString(USRE_NAME_KEY,myUser.getUsername());
            outState.putString(USRE_EMAIL_KEY,myUser.getEmail());
            outState.putString(USRE_PROFILE_KEY,myUser.getImage());
        }
*/

    }

    /*@Override
    protected void onRestoreInstanceState(Bundle data) {
        super.onRestoreInstanceState(data);

        if(data.getString(USRE_NAME_KEY) != null){
            changeUserInfo(data.getString(USRE_NAME_KEY),data.getString(USRE_EMAIL_KEY),data.getString(USRE_PROFILE_KEY));
        }
    }*/

    public void changeUserInfo(String username, String userEmail, String profile) {
        tvUsername.setText(username);
        tvUserEmail.setText(userEmail);

        if (profile.contains("https://graph.facebook.com/")) {
            loadUserProfile(imUserProfile, profile);
        } else {
            loadUserProfile(imUserProfile, ApiGenerator.BASE_URL + profile);
        }
    }

    @Override
    public void onAddMoreCamera(int position) {
        intentActivity = new Intent(this, ConfigCameraActivity.class);
        startActivity(intentActivity);
    }

    @Override
    public void onStreamFullScreen(int position, IPCam ipCam) {
        intentActivity = new Intent(this, CameraViewerActivity.class);
        Parcelable parcelable = Parcels.wrap(ipCam);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CAMERA_PARCELABLE_HOME, parcelable);
        intentActivity.putExtras(bundle);
        startActivity(intentActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isThemeChange){
            isThemeChange = false;
          //  changeAppStyle();

            if(oldTheme!=IPCamTheme.getInstance().getCurrentTheme(this)){
                oldTheme = IPCamTheme.getInstance().getCurrentTheme(this);
              //  confirmStyleChange();

                restartActivity(this);
            }
        }

        if (bundle != null) {
            if (FragUsernameEditing.isUserInfoChange || FragPasswordEditing.isUserInfoChange || FragImageEditing.isUserInfoChange) {
                new UserController().getUserByEmail(bundle.get("EMAIL").toString(), this);
                FragUsernameEditing.isUserInfoChange = false;
                FragPasswordEditing.isUserInfoChange = false;
                FragImageEditing.isUserInfoChange = false;
            }
        }

        if(isLanguageChange || isConfigurationChange){
            isLanguageChange = false;
            isConfigurationChange = false;

            restartActivity(this);
        }

        Log.i("FlowApp", "onResume: ");
    }


    /*public void restartApp(){
        ActivityCompat.finishAffinity(this);
        this.startActivity(new Intent(this, MainActivity.class));
        this.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public void confirmStyleChange(){
        confimrChangeStyleDialog = new MaterialDialog.Builder(this)
                .title(R.string.RestarttoApplyTheme)
                .build();

        MaterialDialog.Builder mBuilder = confimrChangeStyleDialog.getBuilder();
        mBuilder.positiveText(R.string.OK)
                .onPositive((dialog1, which) -> {
                    restartApp();
                })
                .negativeText(R.string.Cancel);
        mBuilder.show();
    }*/
}