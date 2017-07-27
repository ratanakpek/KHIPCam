package com.kshrd.ipcam.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.customerAdapter.CustomizeListCamera;
import com.kshrd.ipcam.entities.camera.IPCam;
import com.kshrd.ipcam.network.controller.CallBack.CameraCallback;
import com.kshrd.ipcam.network.controller.CameraController;
import com.kshrd.ipcam.main.IPCamTheme;
import com.kshrd.ipcam.main.MyActivity;

import java.util.ArrayList;
import java.util.List;


public class ListCameraActivity extends MyActivity implements CameraCallback {

    private String TAG = "LOG";
    private RecyclerView recyclerView;
    private Intent intent;
    Bundle bundle;

    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private ImageView imageViewBackNav;
    private ImageView imageOptionMenu;
    public static boolean isConfigurationChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage(this);
        setContentView(R.layout.activity_list_camera);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerListCamera);
        bundle = getIntent().getExtras();
        if (bundle.get("USER_ID") != null) {
            new CameraController().getCameraByUser((Integer) bundle.get("USER_ID"), this);
        }

        setToolbar();
        changeAppStyle();
    }

    @Override
    public void loadListCamera(List<IPCam> listIpCam) {

    }

    @Override
    public void loadOneCamera(IPCam ipCam) {

    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_with_full_option);
        tvToolbarTitle = (TextView) findViewById(R.id.title_toolbar_with_full_option);
        imageViewBackNav = (ImageView) findViewById(R.id.back_nav_with_full_option);
        imageOptionMenu = (ImageView) findViewById(R.id.menu_toolbar_with_full_option);

        setSupportActionBar(toolbar);
        tvToolbarTitle.setText(R.string.ListCamera);
        imageViewBackNav.setImageResource(R.drawable.arrow_back_navigation);
        imageViewBackNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null) {
                    if (bundle.getInt("USER_ID") > 0) {
                        Intent intent = new Intent(ListCameraActivity.this, ConfigCameraActivity.class);
                        intent.putExtra("USER_ID", bundle.getInt("USER_ID"));
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.CannotGetUserData, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.SomethingWentWrong, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void loadListCameraByUser(List<IPCam> ipCam) {
        if (ipCam != null) {
            Log.d(TAG, "loadListCameraByUser: " + ipCam.get(0).getModel().getVender());
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
            recyclerView.setAdapter(new CustomizeListCamera(ListCameraActivity.this, (ArrayList<IPCam>) ipCam));
        } else {
            Log.d(TAG, "loadListCameraByUser: ============================>>>>>>>>>>> Cannot load camera by user in ListCameraActivity");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isConfigurationChange){
            isConfigurationChange = false;
          //  recreate();

            restartActivity(this);
        }

        if (ConfigCameraActivity.isDataCameraChanged) {
            if (bundle != null) {
                if (bundle.getInt("USER_ID") > 0) {
                    new CameraController().getCameraByUser(bundle.getInt("USER_ID"), this);
                }
            }
        }
    }

    public void changeAppStyle(){
        getWindow().getDecorView().setBackgroundColor(IPCamTheme.windowColor);
        setToolbarWithFullOptionStyle(toolbar,imageViewBackNav,tvToolbarTitle,imageOptionMenu);
    }
}