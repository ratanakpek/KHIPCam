package com.kshrd.ipcam.activitys;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.main.IPCamTheme;
import com.kshrd.ipcam.main.MyActivity;

import java.util.ArrayList;

public class SettingActivity extends MyActivity {

    private Toolbar toolbar;
    private TextView tvToolbarTitle,tvLanguage;
    private ImageView imageViewBackNav;
    private ImageView imageOptionMenu;
    private MaterialSpinner spinnerLanguage;

    private final String ENGLISH = "English";
    private final String KHMER = "ភាសាខ្មែរ";


    String TAG = "SettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage(this);
        setContentView(R.layout.activity_setting);

        spinnerLanguage = (MaterialSpinner) findViewById(R.id.spinner_language);
        tvLanguage = (TextView) findViewById(R.id.tvLanguage);

        setSpinnerLanguage();

        setToolbar();

        changeAppStyle();
    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_with_full_option);
        tvToolbarTitle = (TextView) findViewById(R.id.title_toolbar_with_full_option);
        imageViewBackNav = (ImageView) findViewById(R.id.back_nav_with_full_option);
        imageOptionMenu = (ImageView) findViewById(R.id.menu_toolbar_with_full_option);

        setSupportActionBar(toolbar);
        tvToolbarTitle.setText(R.string.Setting);
        imageViewBackNav.setImageResource(R.drawable.arrow_back_navigation);
        imageViewBackNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageOptionMenu.setVisibility(View.INVISIBLE);
    }

    public void changeAppStyle() {
        getWindow().getDecorView().setBackgroundColor(IPCamTheme.windowColor);
        setToolbarWithFullOptionStyle(toolbar, imageViewBackNav, tvToolbarTitle, imageOptionMenu);
        setSpinnerStyle(spinnerLanguage);
        tvLanguage.setTextColor(IPCamTheme.inputTextColor);
    }

    private void setSpinnerLanguage() {
        ArrayList languageData = new ArrayList();

        languageData.add(ENGLISH);
        languageData.add(KHMER);

        spinnerLanguage.setItems(languageData);

        if (SettingActivity.getInstance().getCurrentLanguage(SettingActivity.this).equals(MyActivity.DEFAULT_LANGUAGE)) {
            spinnerLanguage.setSelectedIndex(0);
        } else {
            spinnerLanguage.setSelectedIndex(1);
        }

        spinnerLanguage.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                switch (item) {
                    case ENGLISH:
                        MyActivity.getInstance().setCurrentLanguage(SettingActivity.this, SettingActivity.DEFAULT_LANGUAGE);
                        break;
                    case KHMER:
                        MyActivity.getInstance().setCurrentLanguage(SettingActivity.this, SettingActivity.KHMER_LANGUAGE);
                        break;
                    default:

                }

                HomeActivity.isLanguageChange = true;
                restartActivity(SettingActivity.this);
            }
        });
    }
}
