package com.kshrd.ipcam.activitys.customerAdapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.main.IPCamTheme;

/**
 * Created by ppsc08 on 04-Jan-17.
 */

public class ViewHolderListCamera extends RecyclerView.ViewHolder {

    public TextView tvCameraName;
    public TextView tvPortNumber;
    public ImageView btnMenuCameraList,imageViewCameraImage;
    public CardView cardView;
    private  String TAG = "ViewHolderListCamera";


    public ViewHolderListCamera(View view) {
        super(view);
        cardView = (CardView)view.findViewById(R.id.cdViewListCamera);
        tvCameraName = (TextView)view.findViewById(R.id.tvCameraName);
        tvPortNumber = (TextView)view.findViewById(R.id.tvPortNumber);
        btnMenuCameraList = (ImageView) view.findViewById(R.id.btnMenuCameraList);
        imageViewCameraImage = (ImageView) view.findViewById(R.id.imageViewCameraImage);

        cardView.setBackgroundColor(IPCamTheme.windowColor);
        imageViewCameraImage.setColorFilter(IPCamTheme.currentMainColor);
        tvCameraName.setTextColor(IPCamTheme.currentMainColor);
        tvPortNumber.setTextColor(IPCamTheme.currentMainColor);
        btnMenuCameraList.setColorFilter(IPCamTheme.currentMainColor);

    }


}
