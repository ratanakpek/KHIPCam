package com.kshrd.ipcam.fragment;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.adapter.ListImageAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragImage extends Fragment {
    private String path = Environment.getExternalStorageDirectory()+"/DCIM/ipcam/";
    private File listImages;
    private File[] files;
    private RecyclerView recyclerImage;
    private ListImageAdapter listImageAdapter;
    private ImageView imgView;
    private static  FragImage instance=null;
    public static final int ROW_ITEMS = 3;
    public static final int COLUMN_ITEMS = 3;
    public static boolean isImageAdded;
    public FragImage() {}
    public static FragImage getInstance(){
        try {
            if (instance == null){
                instance = new FragImage();
                return instance;

            }else {
                return instance;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.frag_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerImage = (RecyclerView)view.findViewById(R.id.recyclerListImage);
        recyclerImage.setLayoutManager(new GridLayoutManager(getActivity(), COLUMN_ITEMS));
        listImageAdapter= new ListImageAdapter(imgView,getActivity());

        getImage();
    }

    public void getImage(){
        listImages = new File(path);
        if(listImages !=null){
            files = listImages.listFiles();
            if(files != null){

                listImageAdapter = new ListImageAdapter();

                for(File file  : files){
                    if(file.getAbsoluteFile().toString().contains(".jpg")){
                        listImageAdapter.add(file.getAbsolutePath());
                    }
                }

                recyclerImage.setAdapter(listImageAdapter);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isImageAdded){
            isImageAdded = false;
            getImage();
        }
    }
}
