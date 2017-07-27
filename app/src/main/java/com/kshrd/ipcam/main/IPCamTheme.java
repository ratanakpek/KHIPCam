package com.kshrd.ipcam.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import com.kshrd.ipcam.R;

/**
 * Created by Chivorn on 2/2/2017.
 */

public class IPCamTheme {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private final String CURRENT_THEME = "CURRENT_SELECTED_THEME";

    private static IPCamTheme instance = null;

    public static final int DEFAULT_THEME = 0;
    public static final int BLUE_THEME = 1;
    public static final int NIGHT_MODE_THEME = 2;


    public static int currentMainColor = 0;

    public static int toolbarBackground = 0;
    public static int backNavColor = 0;
    public static int toolbarTitleColor = 0;
    public static int optionMenuColor = 0;

    public static int navHeaderColor = 0;
    public static int usernameColor = 0;
    public static int userEmailColor = 0;

    public static int navBackground = 0;
    public static int navItemsColor = 0;

    public static int bottomNavBackground = 0;
    public static int bottomNavigationText = 0;
    public static int bottomNavigationTextInActive = 0;

    public static int windowColor = 0;

    public static int btnSwitchCameraLayoutContainer = 0;
    public static int btnSwitchCameraLayoutTextColor = 0;
    public static Drawable btnSwitchCameraLayoutBackgroundInActive;
    public static Drawable btnSwitchCameraLayoutBackgroundActive;
    public static int btnDot;
    public static int card_camera_background;



    /*CONFIGURATION SCREEN*/


    public static Drawable config_SpinnerPopupWindowBackground;
    public static int config_SpinnerArrowColor = 0;
    public static int inputTextColor = 0;
    public static int inputTextHint = 0;
    public static Drawable spinnerBottomBorderColor;
    public static int inputBackgrountTint = 0;

    /*PROFILE*/
    public static Drawable profile_ButtonBorderArround;
    public static int profile_CircleImageBorderColor = 0;
    public static Drawable imageEdit_ButtonBackground;
    public static Drawable imageEdit_ButtonCancelBackground;
    public static Drawable buttonGradient;

    /*Help*/
    /*public static int contentHelpTextHintColor = 0;*/


    public void setNewTheme(Activity context){

        switch (getCurrentTheme(context)) {
            case DEFAULT_THEME:

                currentMainColor = context.getResources().getColor(R.color.DEFAULT_MAIN_COLOR);

                toolbarBackground = context.getResources().getColor(R.color.DEFAULT_ACTION_BAR_COLOR);
                backNavColor = context.getResources().getColor(R.color.DEFAULT_BACK_NAVIGATION_COLOR);
                toolbarTitleColor = context.getResources().getColor(R.color.DEFAULT_TOOLBAR_TITLE_COLOR);
                optionMenuColor = context.getResources().getColor(R.color.DEFAULT_OPTION_MENU_COLOR);

                navHeaderColor = context.getResources().getColor(R.color.DEFAULT_NAVIGATION_HEADER_COLOR);
                usernameColor = context.getResources().getColor(R.color.DEFAULT_USERNAME_COLOR);
                userEmailColor = context.getResources().getColor(R.color.DEFAULT_USER_EMAIL_COLOR);

                navBackground = context.getResources().getColor(R.color.DEFAULT_NAVIGATION_BODY_MENU_COLOR);
                navItemsColor = context.getResources().getColor(R.color.DEFAULT_NAVIGATION_ITEM_COLOR);

                bottomNavBackground = context.getResources().getColor(R.color.DEFAULT_BOTTOM_NAVIGATION_BACKGROUND);
                bottomNavigationText = context.getResources().getColor(R.color.DEFAULT_BOTTOM_NAVIGATION_TEXT_COLOR);
                bottomNavigationTextInActive = context.getResources().getColor(R.color.DEFAULT_BOTTOM_NAVIGATION_TEXT_INACTIVE_COLOR);

                windowColor = context.getResources().getColor(R.color.DEFAULT_WINDOW_COLOR);

                btnSwitchCameraLayoutContainer = context.getResources().getColor(R.color.DEFAULT_BUTTON_SWITCH_LAYOUT_CONTAINER_COLOR);
                btnSwitchCameraLayoutTextColor = context.getResources().getColor(R.color.DEFAULT_BUTTON_SWITCH_LAYOUT_TEXT_COLOR);
                btnSwitchCameraLayoutBackgroundInActive = context.getResources().getDrawable(R.drawable.default_button_switch_layout_background_inactive);
                btnSwitchCameraLayoutBackgroundActive = context.getResources().getDrawable(R.drawable.default_button_switch_layout_background_active);
                btnDot = context.getResources().getColor(R.color.DEFAULT_MAIN_COLOR);
                card_camera_background = context.getResources().getColor(R.color.DEFAULT_CARD_CAMERA_BACKGROUND_COLOR);


                /*Configuration Screen*/

                spinnerBottomBorderColor = context.getResources().getDrawable(R.drawable.default_spinner_bottom_border_color);
                config_SpinnerPopupWindowBackground = context.getResources().getDrawable(R.drawable.default_spinner_popup_window_background);
                config_SpinnerArrowColor = context.getResources().getColor(R.color.DEFAULT_CONFIG_SPINNER_ARROW_COLOR);
                inputTextColor = context.getResources().getColor(R.color.DEFAULT_TEXT_COLOR);
                inputTextHint = context.getResources().getColor(R.color.DEFAULT_TEXT_HINT_COLOR);
                inputBackgrountTint = context.getResources().getColor(R.color.DEFAULT_MAIN_COLOR);

                /*Profile*/
                profile_CircleImageBorderColor =context.getResources().getColor(R.color.DEFAULT_PROFILE_CIRCLE_IMAGE_BORDER_COLOR);
                profile_ButtonBorderArround = context.getResources().getDrawable(R.drawable.default_profile_button_border_arround);
                buttonGradient = context.getResources().getDrawable(R.drawable.default_button_gradient);

                imageEdit_ButtonBackground = context.getResources().getDrawable(R.drawable.default_image_edit_button_background);
                imageEdit_ButtonCancelBackground = context.getResources().getDrawable(R.drawable.default_image_edit_cancel_button_background);

                /*Help*/
                /*contentHelpTextHintColor = context.getResources().getColor(R.color.DEFAULT_HELP_TEXT_HINT_COLOR);*/

                break;
            case BLUE_THEME:

                currentMainColor = context.getResources().getColor(R.color.BLUE_LIGHT_MAIN_COLOR);

                toolbarBackground = context.getResources().getColor(R.color.BLUE_ACTION_BAR_COLOR);
                backNavColor = context.getResources().getColor(R.color.BLUE_BACK_NAVIGATION_COLOR);
                toolbarTitleColor = context.getResources().getColor(R.color.BLUE_TOOLBAR_TITLE_COLOR);
                optionMenuColor = context.getResources().getColor(R.color.BLUE_OPTION_MENU_COLOR);

                navHeaderColor = context.getResources().getColor(R.color.BLUE_NAVIGATION_HEADER_COLOR);
                usernameColor = context.getResources().getColor(R.color.BLUE_USERNAME_COLOR);
                userEmailColor = context.getResources().getColor(R.color.BLUE_USER_EMAIL_COLOR);

                navBackground = context.getResources().getColor(R.color.BLUE_NAVIGATION_BODY_MENU_COLOR);
                navItemsColor = context.getResources().getColor(R.color.BLUE_NAVIGATION_ITEM_COLOR);

                bottomNavBackground = context.getResources().getColor(R.color.BLUE_BOTTOM_NAVIGATION_BACKGROUND);
                bottomNavigationText = context.getResources().getColor(R.color.BLUE_BOTTOM_NAVIGATION_TEXT_COLOR);
                bottomNavigationTextInActive = context.getResources().getColor(R.color.BLUE_BOTTOM_NAVIGATION_TEXT_INACTIVE_COLOR);

                windowColor = context.getResources().getColor(R.color.BLUE_WINDOW_COLOR);

                btnSwitchCameraLayoutContainer = context.getResources().getColor(R.color.BLUE_BUTTON_SWITCH_LAYOUT_CONTAINER_COLOR);
                btnSwitchCameraLayoutTextColor = context.getResources().getColor(R.color.BLUE_BUTTON_SWITCH_LAYOUT_TEXT_COLOR);
                btnSwitchCameraLayoutBackgroundInActive = context.getResources().getDrawable(R.drawable.blue_light_button_switch_layout_background_inactive);
                btnSwitchCameraLayoutBackgroundActive = context.getResources().getDrawable(R.drawable.blue_light_button_switch_layout_background_active);
                btnDot = context.getResources().getColor(R.color.BLUE_LIGHT_MAIN_COLOR);
                card_camera_background = context.getResources().getColor(R.color.BLUE_CARD_CAMERA_BACKGROUND_COLOR);

                /*Configuration Screen*/

                spinnerBottomBorderColor = context.getResources().getDrawable(R.drawable.blue_light_spinner_bottom_border_color);
                config_SpinnerPopupWindowBackground = context.getResources().getDrawable(R.drawable.blue_light_spinner_popup_window_background);
                config_SpinnerArrowColor = context.getResources().getColor(R.color.BLUE_CONFIG_SPINNER_ARROW_COLOR);
                inputTextColor = context.getResources().getColor(R.color.BLUE_TEXT_COLOR);
                inputTextHint = context.getResources().getColor(R.color.BLUE_TEXT_HINT_COLOR);
                inputBackgrountTint = context.getResources().getColor(R.color.BLUE_LIGHT_MAIN_COLOR);

                /*Profile*/
                profile_CircleImageBorderColor =context.getResources().getColor(R.color.BLUE_PROFILE_CIRCLE_IMAGE_BORDER_COLOR);
                profile_ButtonBorderArround = context.getResources().getDrawable(R.drawable.blue_light_profile_button_border_arround);
                buttonGradient = context.getResources().getDrawable(R.drawable.blue_light_button_gradient);

                imageEdit_ButtonBackground = context.getResources().getDrawable(R.drawable.blue_light_image_edit_button_background);
                imageEdit_ButtonCancelBackground = context.getResources().getDrawable(R.drawable.blue_light_image_edit_cancel_button_background);


                /*Help*/
                /*contentHelpTextHintColor = context.getResources().getColor(R.color.BLUE_HELP_TEXT_HINT_COLOR);*/


                break;

            case NIGHT_MODE_THEME:

                currentMainColor = context.getResources().getColor(R.color.NIGHT_MODE_MAIN_COLOR);

                toolbarBackground = context.getResources().getColor(R.color.NIGHT_MODE_ACTION_BAR_COLOR);
                backNavColor = context.getResources().getColor(R.color.NIGHT_MODE_BACK_NAVIGATION_COLOR);
                toolbarTitleColor = context.getResources().getColor(R.color.NIGHT_MODE_TOOLBAR_TITLE_COLOR);
                optionMenuColor = context.getResources().getColor(R.color.NIGHT_MODE_OPTION_MENU_COLOR);

                navHeaderColor = context.getResources().getColor(R.color.NIGHT_MODE_NAVIGATION_HEADER_COLOR);
                usernameColor = context.getResources().getColor(R.color.NIGHT_MODE_USERNAME_COLOR);
                userEmailColor = context.getResources().getColor(R.color.NIGHT_MODE_USER_EMAIL_COLOR);

                navBackground = context.getResources().getColor(R.color.NIGHT_MODE_NAVIGATION_BODY_MENU_COLOR);
                navItemsColor = context.getResources().getColor(R.color.NIGHT_MODE_NAVIGATION_ITEM_COLOR);

                bottomNavBackground = context.getResources().getColor(R.color.NIGHT_MODE_BOTTOM_NAVIGATION_BACKGROUND);
                bottomNavigationText = context.getResources().getColor(R.color.NIGHT_MODE_BOTTOM_NAVIGATION_TEXT_COLOR);
                bottomNavigationTextInActive = context.getResources().getColor(R.color.NIGHT_MODE_BOTTOM_NAVIGATION_TEXT_INACTIVE_COLOR);

                windowColor = context.getResources().getColor(R.color.NIGHT_MODE_WINDOW_COLOR);

                btnSwitchCameraLayoutContainer = context.getResources().getColor(R.color.NIGHT_MODE_BUTTON_SWITCH_LAYOUT_CONTAINER_COLOR);
                btnSwitchCameraLayoutTextColor = context.getResources().getColor(R.color.NIGHT_MODE_BUTTON_SWITCH_LAYOUT_TEXT_COLOR);
                btnSwitchCameraLayoutBackgroundInActive = context.getResources().getDrawable(R.drawable.night_mode_button_switch_layout_background_inactive);
                btnSwitchCameraLayoutBackgroundActive = context.getResources().getDrawable(R.drawable.night_mode_button_switch_layout_background_active);
                btnDot = context.getResources().getColor(R.color.NIGHT_MODE_MAIN_COLOR);
                card_camera_background = context.getResources().getColor(R.color.NIGHT_MODE_CARD_CAMERA_BACKGROUND_COLOR);

                /*Configuration Screen*/

                spinnerBottomBorderColor = context.getResources().getDrawable(R.drawable.night_mode_spinner_bottom_border_color);
                config_SpinnerPopupWindowBackground = context.getResources().getDrawable(R.drawable.night_mode_spinner_popup_window_background);
                config_SpinnerArrowColor = context.getResources().getColor(R.color.NIGHT_MODE_CONFIG_SPINNER_ARROW_COLOR);
                inputTextColor = context.getResources().getColor(R.color.NIGHT_MODE_TEXT_COLOR);
                inputTextHint = context.getResources().getColor(R.color.NIGHT_MODE_TEXT_HINT_COLOR);
                inputBackgrountTint = context.getResources().getColor(R.color.NIGHT_MODE_MAIN_COLOR);

                /*Profile*/
                profile_CircleImageBorderColor =context.getResources().getColor(R.color.NIGHT_MODE_PROFILE_CIRCLE_IMAGE_BORDER_COLOR);
                profile_ButtonBorderArround = context.getResources().getDrawable(R.drawable.night_mode_profile_button_border_arround);
                buttonGradient = context.getResources().getDrawable(R.drawable.night_mode_button_gradient);

                imageEdit_ButtonBackground = context.getResources().getDrawable(R.drawable.night_mode_image_edit_button_background);
                imageEdit_ButtonCancelBackground = context.getResources().getDrawable(R.drawable.night_mode_image_edit_cancel_button_background);


                break;

            default:

        }
    }

    public static IPCamTheme getInstance(){
        try {
            if (instance == null){
                instance = new IPCamTheme();
                return instance;
            }else {
                return instance;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void setCurrentTheme(Activity activity,int newTheme){

        if(sharedPref == null){
            sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        }

        if(editor == null){
            editor = sharedPref.edit();
        }

        editor.putInt(CURRENT_THEME, newTheme);
        editor.commit();
    }

    public int getCurrentTheme(Activity activity){

        if(sharedPref == null){
            sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        }

        return sharedPref.getInt(CURRENT_THEME, DEFAULT_THEME);
    }
}
