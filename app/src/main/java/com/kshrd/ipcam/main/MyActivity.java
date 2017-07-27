package com.kshrd.ipcam.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Locale;

/**
 * Created by Chivorn on 2/4/2017.
 */

public class MyActivity extends AppCompatActivity {

    public static final String DEFAULT_LANGUAGE = "en";
    public static final String KHMER_LANGUAGE = "kh";
    private Locale locale;
    private Configuration config;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private final String CURRENT_LANGUAGE = "CURRENT_SELECTED_LANGUAGE";
    private static MyActivity instance = null;

    private ColorStateList colorStateList;

    public void setToolbarWithFullOptionStyle(Toolbar toolbar, ImageView backNav, TextView title, ImageView optionMenu) {
        toolbar.setBackgroundColor(IPCamTheme.toolbarBackground);
        backNav.setColorFilter(IPCamTheme.backNavColor);
        title.setTextColor(IPCamTheme.toolbarTitleColor);
        optionMenu.setColorFilter(IPCamTheme.optionMenuColor);
    }

    public void setSpinnerStyle(MaterialSpinner... spinners) {
        for (int i = 0; i < spinners.length; i++) {
            spinners[i].setBackground(IPCamTheme.spinnerBottomBorderColor);
            // spinners[i].getBackground().setColorFilter(IPCamTheme.inputBackgrountTint, PorterDuff.Mode.SRC_ATOP);
            spinners[i].getPopupWindow().setBackgroundDrawable(IPCamTheme.config_SpinnerPopupWindowBackground);
            spinners[i].setTextColor(IPCamTheme.inputTextColor);
            spinners[i].setHintTextColor(IPCamTheme.inputTextHint);
            spinners[i].setArrowColor(IPCamTheme.config_SpinnerArrowColor);
        }
    }

    public void setNavigationDrawerStyle(View navHeader, TextView tvUsername, TextView tvUserEmail, NavigationView navigationView) {
        navHeader.setBackgroundColor(IPCamTheme.navHeaderColor);
        tvUsername.setTextColor(IPCamTheme.usernameColor);
        tvUserEmail.setTextColor(IPCamTheme.userEmailColor);

        navigationView.setBackgroundColor(IPCamTheme.navBackground);

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}
        };

        int[] colors = new int[]{
                IPCamTheme.navItemsColor
        };

        colorStateList = new ColorStateList(states, colors);

        navigationView.setItemIconTintList(colorStateList);
        navigationView.setItemTextColor(colorStateList);
    }

    public void setBottomNavigationStyle(AHBottomNavigation bottomNavigation) {
        bottomNavigation.setDefaultBackgroundColor(IPCamTheme.bottomNavBackground);
        bottomNavigation.setAccentColor(IPCamTheme.bottomNavigationText);
        bottomNavigation.setInactiveColor(IPCamTheme.bottomNavigationTextInActive);
    }

    public void setEditTextStyle(EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].setTextColor(IPCamTheme.inputTextColor);
            editTexts[i].setHintTextColor(IPCamTheme.inputTextHint);
            editTexts[i].getBackground().setColorFilter(IPCamTheme.inputBackgrountTint, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void setTextViewColorAsTextHintColor(TextView... textViews) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setTextColor(IPCamTheme.inputTextHint);
        }
    }

    public void setRadioButtonStyle(RadioButton... radioButtons) {
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setTextColor(IPCamTheme.inputTextColor);

            if (Build.VERSION.SDK_INT >= 21) {
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_enabled}
                        },
                        new int[]{
                                IPCamTheme.currentMainColor
                        }
                );
                radioButtons[i].setButtonTintList(colorStateList);//set the color tint list
                radioButtons[i].invalidate(); //could not be necessary
            }
        }
    }

    public static MyActivity getInstance() {
        try {
            if (instance == null) {
                instance = new MyActivity();
                return instance;
            } else {
                return instance;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCurrentLanguage(Activity activity, String newLang) {

        if (sharedPref == null) {
            sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        }

        if (editor == null) {
            editor = sharedPref.edit();
        }

        editor.putString(CURRENT_LANGUAGE, newLang);
        editor.commit();
    }

    public String getCurrentLanguage(Activity activity) {

        if (sharedPref == null) {
            sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        }

        return sharedPref.getString(CURRENT_LANGUAGE, DEFAULT_LANGUAGE);
    }

    public void changeLanguage(Activity activity) {

        if (locale == null) {
            locale = new Locale(getInstance().getCurrentLanguage(activity));
        }

        Log.i("CURRENT_LANGUAGE", "changeLanguage: "+activity.getClass() +"   :  "+ getInstance().getCurrentLanguage(activity));

        Locale.setDefault(locale);

        if (config == null) {
            config = new Configuration();
        }

        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void restartActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            activity.recreate();

        } else {
            final Intent intent = activity.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.finish();
            activity.overridePendingTransition(0, 0);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
        }
    }
}
