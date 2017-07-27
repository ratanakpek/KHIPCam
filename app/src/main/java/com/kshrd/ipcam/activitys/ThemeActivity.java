package com.kshrd.ipcam.activitys;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.main.IPCamTheme;
import com.kshrd.ipcam.main.MyActivity;

public class ThemeActivity extends MyActivity {

    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private ImageView imageViewBackNav;
    private ImageView imageOptionMenu;
    private RadioGroup radioGroupTheme;
    private RadioButton rdTheme_default,rdTheme_blue,rdTheme_night_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage(this);
        setContentView(R.layout.activity_theme);

        radioGroupTheme = (RadioGroup) findViewById(R.id.radioGroupTheme);
        rdTheme_default = (RadioButton) findViewById(R.id.theme_default);
        rdTheme_blue = (RadioButton) findViewById(R.id.theme_blue);
        rdTheme_night_mode = (RadioButton) findViewById(R.id.theme_night_mode);

        int currentTheme = IPCamTheme.getInstance().getCurrentTheme(this);

        if(currentTheme >= 0 && currentTheme <= radioGroupTheme.getChildCount()){
            ((RadioButton)radioGroupTheme.getChildAt(IPCamTheme.getInstance().getCurrentTheme(this))).setChecked(true);
        }else {
            IPCamTheme.getInstance().setCurrentTheme(this,IPCamTheme.DEFAULT_THEME);
            IPCamTheme.getInstance().setNewTheme(this);
        }


        setToolbar();
        changeAppStyle();
    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_with_full_option);
        tvToolbarTitle = (TextView) findViewById(R.id.title_toolbar_with_full_option);
        imageViewBackNav = (ImageView) findViewById(R.id.back_nav_with_full_option);
        imageOptionMenu = (ImageView) findViewById(R.id.menu_toolbar_with_full_option);

        setSupportActionBar(toolbar);
        tvToolbarTitle.setText(R.string.Theme);
        imageViewBackNav.setImageResource(R.drawable.arrow_back_navigation);
        imageViewBackNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageOptionMenu.setVisibility(View.INVISIBLE);
    }

    public void onRadioButtonClicked(View view) {
        switch(view.getId()) {
            case R.id.theme_default:
                IPCamTheme.getInstance().setCurrentTheme(this,IPCamTheme.DEFAULT_THEME);
                    break;

            case R.id.theme_blue:
                IPCamTheme.getInstance().setCurrentTheme(this,IPCamTheme.BLUE_THEME);
                break;

            case R.id.theme_night_mode:
                IPCamTheme.getInstance().setCurrentTheme(this,IPCamTheme.NIGHT_MODE_THEME);
                break;
        }

        IPCamTheme.getInstance().setNewTheme(this);
        changeAppStyle();
        HomeActivity.isThemeChange = true;
    }

    public void changeAppStyle(){
        getWindow().getDecorView().setBackgroundColor(IPCamTheme.windowColor);
        setToolbarWithFullOptionStyle(toolbar,imageViewBackNav,tvToolbarTitle,imageOptionMenu);
        setRadioButtonStyle(rdTheme_default,rdTheme_blue,rdTheme_night_mode);
    }
}
