package com.kshrd.ipcam.main;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Chivorn on 2/4/2017.
 */

public class MyFragment extends Fragment {


    public void setEditTextStyle(EditText... editTexts){
        for(int i=0;i<editTexts.length;i++){
            editTexts[i].setTextColor(IPCamTheme.inputTextColor);
            editTexts[i].setHintTextColor(IPCamTheme.inputTextHint);
            editTexts[i].getBackground().setColorFilter(IPCamTheme.inputBackgrountTint, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void setTextViewColorAsTextHintColor(TextView... textViews){
        for(int i=0;i<textViews.length;i++){
            textViews[i].setTextColor(IPCamTheme.inputTextHint);
        }
    }

    public void setGradientButton(Button... buttons){
        for(int i=0;i<buttons.length;i++){
            buttons[i].setBackground(IPCamTheme.buttonGradient);
        }
    }

    public void setTextViewCurrentMainColor(TextView... textViews){
        for(int i=0;i<textViews.length;i++){
            textViews[i].setTextColor(IPCamTheme.currentMainColor);
        }
    }

    public void setToolbarWithFullOptionStyle(Toolbar toolbar, ImageView backNav,TextView title,ImageView optionMenu){
        toolbar.setBackgroundColor(IPCamTheme.toolbarBackground);
        backNav.setColorFilter(IPCamTheme.backNavColor);
        title.setTextColor(IPCamTheme.toolbarTitleColor);
        optionMenu.setColorFilter(IPCamTheme.optionMenuColor);
    }

    public void setToolbarNoOptionMenuStyle(Toolbar toolbar, ImageView backNav,TextView title){
        toolbar.setBackgroundColor(IPCamTheme.toolbarBackground);
        backNav.setColorFilter(IPCamTheme.backNavColor);
        title.setTextColor(IPCamTheme.toolbarTitleColor);
    }

    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

}
