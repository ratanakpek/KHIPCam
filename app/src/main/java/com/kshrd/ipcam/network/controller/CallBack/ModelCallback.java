package com.kshrd.ipcam.network.controller.CallBack;

import com.kshrd.ipcam.entities.camera.Model;

import java.util.ArrayList;

/**
 * Created by ppsc08 on 10-Jan-17.
 */

public interface ModelCallback {
    void loadModelName(ArrayList<Model> modelName);
    void loadAllModel(ArrayList<Model> modelName);
    void loadModelById(Model model);
}
