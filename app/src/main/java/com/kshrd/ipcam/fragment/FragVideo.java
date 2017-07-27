package com.kshrd.ipcam.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.adapter.ListVideoRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragVideo extends Fragment {
    private String path = Environment.getExternalStorageDirectory()+"/DCIM/ipcam/";
    private ArrayList<File> listFiles;
    private File fileVideo;
    private File[] fileVideos;
    private static FragVideo instance = null;
    private RecyclerView recyclerView;
    public static final int ROW_ITEMS = 3;
    public static final int COLUMN_ITEMS = 2;
    public static boolean isVideoAdded;

    public FragVideo() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerListVideo);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),COLUMN_ITEMS));

        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

        getVideo();
    }

    public static FragVideo getInstance(){
        try {
            if (instance == null){
                instance = new FragVideo();
                return instance;
            }else {
                return instance;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void getVideo(){

        if(fileVideo == null){
            fileVideo = new File((path));
        }

        if(fileVideo !=null){

            fileVideos = fileVideo.listFiles();
            if(fileVideos != null){

                listFiles = new ArrayList<>();

                for(File file  : fileVideos){
                    if(file.getAbsoluteFile().toString().contains(".3gp")  || file.getAbsoluteFile().toString().contains(".mp4") ){
                        listFiles.add(file);
                    }
                }
            }

            recyclerView.setAdapter(new ListVideoRecyclerAdapter(getActivity(),listFiles));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isVideoAdded){
            isVideoAdded = false;
            getVideo();
        }
    }
}
