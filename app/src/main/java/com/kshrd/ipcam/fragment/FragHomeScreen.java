package com.kshrd.ipcam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.ConfigCameraActivity;
import com.kshrd.ipcam.activitys.customerAdapter.CustomizeListCamera;
import com.kshrd.ipcam.adapter.CustomHomeScreenAdapter;
import com.kshrd.ipcam.entities.camera.IPCam;
import com.kshrd.ipcam.entities.user.User;
import com.kshrd.ipcam.network.controller.CallBack.CameraCallback;
import com.kshrd.ipcam.network.controller.CallBack.UserCallback;
import com.kshrd.ipcam.network.controller.CameraController;
import com.kshrd.ipcam.network.controller.UserController;
import com.kshrd.ipcam.main.IPCamTheme;

import java.util.ArrayList;
import java.util.List;

public class FragHomeScreen extends Fragment implements UserCallback,CameraCallback {
    private static FragHomeScreen instance = null;
    private CustomHomeScreenAdapter homeAdapter;
    private RecyclerView homeRecycler;
    private GridLayoutManager layoutManager;
    private Button btnShowOneCamera,btnShowTwoCamera,btnShowFourCamera,btnShowSixCamera;
    private ImageView dot_image;
    private ArrayList<IPCam> IPCameraDatas;
    private double totalItems = 5;

    public static final int LAYOUT_ONE = 1;
    public static final int LAYOUT_TWO = 2;
    public static final int LAYOUT_FOUR = 4;
    public static final int LAYOUT_SIX = 6;
    private int CURRENT_STATE_LAYOUT = LAYOUT_ONE;

    private int userID;
    private boolean isCameraLess;
    private LinearLayout linearLayoutBtnSwitchCamera;

    public FragHomeScreen(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        final View rootView = inflater.inflate(R.layout.frag_home_screen, container, false);

        linearLayoutBtnSwitchCamera = (LinearLayout) rootView.findViewById(R.id.button_switch_camera_container);

        btnShowOneCamera = (Button) rootView.findViewById(R.id.btnShowOneCamera);
        btnShowTwoCamera = (Button) rootView.findViewById(R.id.btnShowTwoCamera);
        btnShowFourCamera = (Button) rootView.findViewById(R.id.btnShowFourCamera);
        btnShowSixCamera = (Button) rootView.findViewById(R.id.btnShowSixCamera);
        dot_image = (ImageView) rootView.findViewById(R.id.dot_image);


        homeRecycler = (RecyclerView) rootView.findViewById(R.id.home_recycler_layout);
     //   homeRecycler.getRecycledViewPool().setMaxRecycledViews(0,0);
      //  homeRecycler.setNestedScrollingEnabled(false);
        /*homeAdapter = new CustomHomeScreenAdapter(IPCameraDatas,getActivity());*/

        if(savedInstanceState !=null){
            CURRENT_STATE_LAYOUT = savedInstanceState.getInt("CURRENT_STATE_LAYOUT");
        }

        /*generateNewLayout(CURRENT_STATE_LAYOUT);*/

        btnShowOneCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCameraDataAvailable()){
                    CURRENT_STATE_LAYOUT = LAYOUT_ONE;
                    generateNewLayout(CURRENT_STATE_LAYOUT);
                }
            }
        });

        btnShowTwoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCameraDataAvailable()){
                    CURRENT_STATE_LAYOUT = LAYOUT_TWO;
                    generateNewLayout(CURRENT_STATE_LAYOUT);
                }
            }
        });

        btnShowFourCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCameraDataAvailable()){
                    CURRENT_STATE_LAYOUT = LAYOUT_FOUR;
                    generateNewLayout(CURRENT_STATE_LAYOUT);
                }
            }
        });

        btnShowSixCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCameraDataAvailable()){
                    CURRENT_STATE_LAYOUT = LAYOUT_SIX;
                    generateNewLayout(CURRENT_STATE_LAYOUT);
                }
            }
        });

        return rootView;
    }

    public void setAdapterAndAnimation(){
        // ScaleInAnimationAdapter animateAdapter = new ScaleInAnimationAdapter(homeAdapter);
        // homeRecycler.setItemAnimator(new SlideInLeftAnimator());
        //  homeRecycler.setAdapterAndAnimation(animateAdapter);
        homeRecycler.setAdapter(homeAdapter);
    }

    public void setRowAndColumnPerRow(int rows, int colunms){

        homeAdapter.setRow(rows);
        layoutManager = new GridLayoutManager(getActivity(),colunms);
        /*layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                    return 1;
            }
        });*/

        homeRecycler.setLayoutManager(layoutManager);
        setAdapterAndAnimation();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("CURRENT_STATE_LAYOUT",CURRENT_STATE_LAYOUT);
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_camera) {
            Intent intent = new Intent(getActivity(),ConfigCameraActivity.class);
            intent.putExtra("USER_ID",userID);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public static FragHomeScreen getInstance(){
        try {
            if (instance == null){
                instance = new FragHomeScreen();
                Bundle bundle = new Bundle();
                instance.setArguments(bundle);
                return instance;

            }else {
                return instance;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void changeActiveButton(Button activeButton){
        setInActiveButtonBackground(btnShowOneCamera,btnShowTwoCamera,btnShowFourCamera,btnShowSixCamera);
        activeButton.setBackground(IPCamTheme.btnSwitchCameraLayoutBackgroundActive);
    }

    public void generateNewLayout(int layoutType){

        switch (layoutType) {
            case LAYOUT_ONE:
                changeActiveButton(btnShowOneCamera);
                setRowAndColumnPerRow(1,1);
                break;
            case LAYOUT_TWO:
                changeActiveButton(btnShowTwoCamera);
                setRowAndColumnPerRow(2,1);
                break;
            case LAYOUT_FOUR:
                changeActiveButton(btnShowFourCamera);
                setRowAndColumnPerRow(2,2);
                break;
            case LAYOUT_SIX:
                changeActiveButton(btnShowSixCamera);
                setRowAndColumnPerRow(3,3);
                break;
            default:
                setRowAndColumnPerRow(1,1);
                changeActiveButton(btnShowOneCamera);
        }
    }

    @Override
    public void loadListCamera(List<IPCam> listIpCam) {

    }

    @Override
    public void loadOneCamera(IPCam ipCam) {

    }

    @Override
    public void loadListCameraByUser(List<IPCam> ipCam) {
        IPCameraDatas = new ArrayList<>();
        if(ipCam != null){

            for (IPCam ipCams : ipCam){
                IPCameraDatas.add(ipCams);
            }
            while (IPCameraDatas.size()<12){
                IPCameraDatas.add(new IPCam());
                isCameraLess = true;
            }
        }

        homeAdapter = new CustomHomeScreenAdapter(IPCameraDatas,getActivity());

        if(isCameraLess){
            homeAdapter.notifyItemInserted(IPCameraDatas.size()-1);
        }

        generateNewLayout(CURRENT_STATE_LAYOUT);

        ConfigCameraActivity.isDataCameraChanged = false;
        CustomizeListCamera.isDataCameraChanged = false;
    }

    @Override
    public void loadUserByEmail(User user) {

        if(user != null){
            userID = user.getUser_id();
            new CameraController().getCameraByUser(user.getUser_id(),this);
        }
    }

    @Override
    public void loadUserById(User user) {

    }

    @Override
    public void emailCheckerState(Boolean status) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i("Flow", "onActivityCreated:FragHome ");
        super.onActivityCreated(savedInstanceState);

        if(getActivity().getIntent().getExtras() != null){
            Bundle bundle = getActivity().getIntent().getExtras();
            Log.i("Ming Ming", "onCreateView: User Email m"+ bundle.get("EMAIL"));
            new UserController().getUserByEmail((String)bundle.get("EMAIL"),this);
        }
    }


    public boolean isCameraDataAvailable(){
        if(IPCameraDatas != null){
            return true;
        }else {
            Toast.makeText(getActivity(), R.string.CameraIsNotReady, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(homeAdapter != null){
            homeAdapter.stopAllStream();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(homeAdapter != null){
            homeAdapter.stopAllStream();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        changeAppStyle();

        if(homeAdapter != null){
            homeAdapter.startOldStream();
        }

        if(ConfigCameraActivity.isDataCameraChanged || CustomizeListCamera.isDataCameraChanged){
            new CameraController().getCameraByUser(userID,this);
        }
    }

    public void changeAppStyle(){
        linearLayoutBtnSwitchCamera.setBackgroundColor(IPCamTheme.btnSwitchCameraLayoutContainer);
        setButtonTextColor(btnShowOneCamera,btnShowTwoCamera,btnShowFourCamera,btnShowSixCamera);
        dot_image.setColorFilter(IPCamTheme.btnDot);

        switch (CURRENT_STATE_LAYOUT) {
            case LAYOUT_ONE:
                changeActiveButton(btnShowOneCamera);
                break;
            case LAYOUT_TWO:
                changeActiveButton(btnShowTwoCamera);
                break;
            case LAYOUT_FOUR:
                changeActiveButton(btnShowFourCamera);
                break;
            case LAYOUT_SIX:
                changeActiveButton(btnShowSixCamera);
                break;
            default:
                changeActiveButton(btnShowOneCamera);
        }
    }

    public void setInActiveButtonBackground(Button... buttons){
        for(int i=0;i<buttons.length;i++){
            buttons[i].setBackground(IPCamTheme.btnSwitchCameraLayoutBackgroundInActive);
        }
    }

    public void setButtonTextColor(Button... buttons){
        for(int i=0;i<buttons.length;i++){
            buttons[i].setTextColor(IPCamTheme.btnSwitchCameraLayoutTextColor);
        }
    }
}