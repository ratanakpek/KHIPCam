package com.kshrd.ipcam.video;

import android.net.Uri;

/**
 * Created by Chivorn on 2/7/17.
 */
public interface MyVideoViewCallback {

    void onStarted(MyVideoView player);

    void onPaused(MyVideoView player);

    void onPreparing(MyVideoView player);

    void onPrepared(MyVideoView player);

    void onBuffering(int percent);

    void onError(MyVideoView player, Exception e);

    void onCompletion(MyVideoView player);

    void onRetry(MyVideoView player, Uri source);

    void onSubmit(MyVideoView player, Uri source);

    void onClickVideoFrame(MyVideoView player);
}