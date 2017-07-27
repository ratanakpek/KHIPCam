package com.kshrd.ipcam.activitys;

import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.entities.camera.Model;
import com.kshrd.ipcam.entities.camera.Vender;
import com.kshrd.ipcam.entities.form.IPCameraInputer;
import com.kshrd.ipcam.network.controller.CallBack.ModelCallback;
import com.kshrd.ipcam.network.controller.CallBack.VenderCallback;
import com.kshrd.ipcam.network.controller.CameraController;
import com.kshrd.ipcam.network.controller.ModelController;
import com.kshrd.ipcam.network.controller.VenderController;
import com.kshrd.ipcam.main.IPCamTheme;
import com.kshrd.ipcam.main.MyActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ConfigCameraActivity extends MyActivity implements NavigationView.OnNavigationItemSelectedListener, ModelCallback, VenderCallback {

    private static final String TAG = "ConfigCameraActivity";
    private MaterialSpinner spinnerVendor, spinnerModel;
    private EditText tvCameraName, tvSerial, tvIPAddress, tvWebPort, tvStreamPort, tvUsername, tvPassword;
    private ArrayList<Integer> modelID;
    private ArrayList<String> modelNames;
    private ArrayList<String> venderName;
    private Bundle bundle = null;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private ImageView imageViewBackNav;
    private ImageView imageOptionMenu;

    public static boolean isDataCameraChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage(this);
        setContentView(R.layout.activity_config_camera);

        spinnerVendor = (MaterialSpinner) findViewById(R.id.spinner_vendor);
        spinnerModel = (MaterialSpinner) findViewById(R.id.spinner_model);
        tvCameraName = (EditText) findViewById(R.id.et_camera_name);
        tvSerial = (EditText) findViewById(R.id.et_serial);
        tvIPAddress = (EditText) findViewById(R.id.et_ip_address);
        tvWebPort = (EditText) findViewById(R.id.et_web_port);
        tvStreamPort = (EditText) findViewById(R.id.et_stream_port);
        tvUsername = (EditText) findViewById(R.id.et_username);
        tvPassword = (EditText) findViewById(R.id.et_password);

        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
        }

        isDataCameraChanged = false;

        setToolbarAndNavigationDrawer();
        new VenderController().getAllVender(this);

        changeAppStyle();

    }

    private void setSpinnerVendor(ArrayList vendorData) {
        if (vendorData.size() > 0) {
            if(vendorData.size() <= 1){
               // spinnerVendor.setEnabled(false);
                spinnerVendor.setArrowColor(IPCamTheme.windowColor);
            }else {
                spinnerVendor.setArrowColor(IPCamTheme.config_SpinnerArrowColor);
            }
            spinnerVendor.setItems(vendorData);
            spinnerVendor.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    new ModelController().getModelNameByVenderName(item, ConfigCameraActivity.this);
                }
            });
        }else {
            spinnerVendor.setArrowColor(IPCamTheme.windowColor);
        }
    }

    private void setSpinnerModel(ArrayList modelData) {
        if (modelData.size() > 0) {

            sortArrayList(modelData);

            spinnerModel.setItems(modelData);
            if(modelData.size() <= 1){
            //    spinnerModel.setEnabled(false);
                spinnerModel.setArrowColor(IPCamTheme.windowColor);
            }else {
                spinnerModel.setArrowColor(IPCamTheme.config_SpinnerArrowColor);
            }
        } else {
            String[] arr = {""};
            spinnerModel.setItems(arr);
            spinnerModel.setArrowColor(IPCamTheme.windowColor);
        }
    }


    public void setToolbarAndNavigationDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_with_full_option);
        tvToolbarTitle = (TextView) findViewById(R.id.title_toolbar_with_full_option);
        imageViewBackNav = (ImageView) findViewById(R.id.back_nav_with_full_option);
        imageOptionMenu = (ImageView) findViewById(R.id.menu_toolbar_with_full_option);

        setSupportActionBar(toolbar);
        tvToolbarTitle.setText(R.string.Configuration);
        imageViewBackNav.setImageResource(R.drawable.arrow_back_navigation);
        imageViewBackNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageOptionMenu.setImageResource(R.drawable.ic_save_black_24dp);
        imageOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IPCameraInputer ipCameraInputer = new IPCameraInputer();

                if (!validate()) {
                    return;
                } else {
                    ipCameraInputer.setName(tvCameraName.getText().toString());
                    ipCameraInputer.setSerial_number(tvSerial.getText().toString());
                    ipCameraInputer.setIp_address(tvIPAddress.getText().toString());
                    ipCameraInputer.setWeb_port(Integer.parseInt(tvWebPort.getText().toString()));
                    ipCameraInputer.setRtsp_port(Integer.parseInt(tvStreamPort.getText().toString()));
                    ipCameraInputer.setUsername(tvUsername.getText().toString());
                    ipCameraInputer.setPassword(tvPassword.getText().toString());
                    ipCameraInputer.setModel_id(modelID.get(spinnerModel.getSelectedIndex()));
                    ipCameraInputer.setUser_id(bundle.getInt("USER_ID"));

                    new CameraController().addCamera(ConfigCameraActivity.this, ipCameraInputer);
                }
            }
        });
    }

    // @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void loadModelName(ArrayList<Model> modelName) {

        if (modelName != null) {
            modelNames = new ArrayList<>();
            modelID = new ArrayList<>();

            for (Model model : modelName) {
                modelNames.add(model.getName());
                modelID.add(model.getModel_id());
            }

            setSpinnerModel(modelNames);
        } else {
            Log.i(TAG, "loadModelName:  ==================================>>>>>>    Model cannot load !");
        }

    }

    @Override
    public void loadAllModel(ArrayList<Model> modelName) {

    }

    @Override
    public void loadModelById(Model model) {

    }

    @Override
    public void loadAllVender(ArrayList<Vender> venderArrayList) {

        if (venderArrayList != null) {
            venderName = new ArrayList<>();
            for (Vender vender : venderArrayList) {
                venderName.add(vender.getName());
            }

            sortArrayList(venderName);

            setSpinnerVendor(venderName);
            if (venderName.size() > 0) {
                new ModelController().getModelNameByVenderName(venderName.get(0), ConfigCameraActivity.this);
            }
        } else {
            Toast.makeText(this, R.string.VendorCannotLoad, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "loadAllVender: ==================================>>>>>>    Vender cannot load ");
        }
    }

    public void sortArrayList(ArrayList<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
    }

    public boolean validate() {
        spinnerVendor.setError(null);
        spinnerModel.setError(null);

        if (TextUtils.isEmpty(spinnerVendor.getText().toString())) {
            spinnerVendor.setError(getResources().getString(R.string.PleaseFillTheBlank));
            return false;
        }

        if (TextUtils.isEmpty(spinnerModel.getText().toString())) {
            spinnerModel.setError(getResources().getString(R.string.PleaseFillTheBlank));
            return false;
        }

        if (TextUtils.isEmpty(tvCameraName.getText().toString())) {
            tvCameraName.setError(getResources().getString(R.string.PleaseFillTheBlank));
            return false;
        }

        if (TextUtils.isEmpty(tvSerial.getText().toString())) {
            tvSerial.setError(getResources().getString(R.string.PleaseFillTheBlank));
            return false;
        }


        int ipDotCount = tvIPAddress.getText().toString().length() - tvIPAddress.getText().toString().replace(".", "").length();
        if (TextUtils.isEmpty(tvIPAddress.getText().toString())) {
            tvIPAddress.setError(getResources().getString(R.string.PleaseFillTheBlank));
            return false;
        } else if (tvIPAddress.getText().toString().length() > 15 || ipDotCount < 3 || ipDotCount > 3) {
            tvIPAddress.setError(getResources().getString(R.string.ItLookLikeNotIP));
            return false;
        }

        if (TextUtils.isEmpty(tvWebPort.getText().toString())) {
            tvWebPort.setError(getResources().getString(R.string.PleaseFillTheBlank));
            return false;
        }

        if (TextUtils.isEmpty(tvStreamPort.getText().toString())) {
            tvStreamPort.setError(getResources().getString(R.string.PleaseFillTheBlank));
            return false;
        }

        if (TextUtils.isEmpty(tvUsername.getText().toString())) {
            tvUsername.setError(getResources().getString(R.string.PleaseFillTheBlank));
            return false;
        }

        if (TextUtils.isEmpty(tvPassword.getText().toString())) {
            tvPassword.setError(getResources().getString(R.string.PleaseFillTheBlank));
            return false;
        }
        return true;
    }

    public void clearText() {
        tvCameraName.setText("");
        tvSerial.setText("");
        tvIPAddress.setText("");
        tvWebPort.setText("");
        tvStreamPort.setText("");
        tvUsername.setText("");
        tvPassword.setText("");
    }

    public void changeAppStyle(){
        getWindow().getDecorView().setBackgroundColor(IPCamTheme.windowColor);
        setToolbarWithFullOptionStyle(toolbar,imageViewBackNav,tvToolbarTitle,imageOptionMenu);
        setSpinnerStyle(spinnerModel,spinnerVendor);
        setEditTextStyle(tvCameraName,tvSerial,tvIPAddress,tvWebPort,tvStreamPort,tvUsername,tvPassword);
    }

}
