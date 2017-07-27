package com.kshrd.ipcam.activitys;

import android.os.AsyncTask;

import com.kshrd.ipcam.network.Internet;
import com.kshrd.ipcam.network.controller.CameraController;

/**
 * Created by rina on 1/3/17.
 */

public class CommandThread extends AsyncTask<Object,Void,Void> {

    @Override
    protected Void doInBackground(Object ... object) {
        new CameraController().doCommand((String)object[0],(Integer)object[1],(String)object[2]);
        return  null;
    }
}
