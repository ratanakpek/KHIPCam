package com.kshrd.ipcam.adapter;

import com.kshrd.ipcam.entities.camera.IPCam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chivorn on 1/2/2017.
 */

public interface Communicator {
    public void onAddMoreCamera(int position);
    public void onStreamFullScreen(int position,IPCam ipCam);
}
