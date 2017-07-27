package com.kshrd.ipcam.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.main.IPCamTheme;
import com.kshrd.ipcam.video.MyVideoViewCallback;
import com.kshrd.ipcam.video.MyVideoView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rina on 1/31/17.
 */

public class ListMyVideoRecyclerViewHolder extends RecyclerView.ViewHolder implements MyVideoViewCallback {

    MyVideoView player;
    Context tempContext;
    ImageView imagePlay;
    public static int parentWeight,parentHeight;

    public ListMyVideoRecyclerViewHolder(View itemView, Context context, ArrayList<File> files) {
        super(itemView);
        tempContext = context;
        player = (MyVideoView) itemView.findViewById(R.id.player);
        imagePlay = (ImageView) itemView.findViewById(R.id.play_btn);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(parentWeight, parentHeight);
        player.setLayoutParams(params);

        player.disableControls();
        player.setAutoFullscreen(true);
        assert player != null;
        player.setCallback(this);

        changeAppStyle();
    }

    public void changeAppStyle() {
     //   player.setBackgroundColor(IPCamTheme.card_camera_background);  if apply not cute.
        imagePlay.setColorFilter(IPCamTheme.currentMainColor);
    }

    @Override
    public void onStarted(MyVideoView player) {
    }

    @Override
    public void onPaused(MyVideoView player) {
    }

    @Override
    public void onPreparing(MyVideoView player) {
        Log.d("EVP-Sample", "onPreparing()");
    }

    @Override
    public void onPrepared(MyVideoView player) {
        Log.d("EVP-Sample", "onPrepared()");
    }

    @Override
    public void onBuffering(int percent) {
        Log.d("EVP-Sample", "onBuffering(): " + percent + "%");
    }

    @Override
    public void onError(MyVideoView player, Exception e) {
    }

    @Override
    public void onCompletion(MyVideoView player) {
        Log.d("EVP-Sample", "onCompletion()");
    }

    @Override
    public void onRetry(MyVideoView player, Uri source) {
    }

    @Override
    public void onSubmit(MyVideoView player, Uri source) {
    }

    @Override
    public void onClickVideoFrame(MyVideoView player) {
    }
}
