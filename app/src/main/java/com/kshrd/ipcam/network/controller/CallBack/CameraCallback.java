package com.kshrd.ipcam.network.controller.CallBack;

import com.kshrd.ipcam.entities.camera.IPCam;

import java.util.List;

/**
 * Created by rina on 1/2/17.
 */

public interface CameraCallback {
     void loadListCamera(List<IPCam> listIpCam) ;
     void loadOneCamera(IPCam ipCam);
     void loadListCameraByUser(List<IPCam> ipCam);
}
