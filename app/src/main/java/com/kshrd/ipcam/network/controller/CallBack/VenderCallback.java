package com.kshrd.ipcam.network.controller.CallBack;

import com.kshrd.ipcam.entities.camera.Vender;

import java.util.ArrayList;

/**
 * Created by rina on 1/10/17.
 */

public interface VenderCallback {
    void loadAllVender(ArrayList<Vender> venderArrayList);
}
