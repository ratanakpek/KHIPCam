package com.kshrd.ipcam.activitys.customerAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.CameraViewerActivity;
import com.kshrd.ipcam.entities.camera.IPCam;
import com.kshrd.ipcam.entities.camera.Model;
import com.kshrd.ipcam.entities.camera.Vender;
import com.kshrd.ipcam.entities.form.IPCameraModifier;
import com.kshrd.ipcam.network.controller.CallBack.ModelCallback;
import com.kshrd.ipcam.network.controller.CallBack.VenderCallback;
import com.kshrd.ipcam.network.controller.CameraController;
import com.kshrd.ipcam.network.controller.ModelController;
import com.kshrd.ipcam.network.controller.VenderController;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by ppsc08 on 04-Jan-17.
 */

public class CustomizeListCamera extends RecyclerView.Adapter<ViewHolderListCamera> implements ModelCallback, VenderCallback {

    protected ArrayList<IPCam> ipCamArrayList;
    private Context context;
    private String TAG = "CustomizeListCamera";
    private ViewHolderListCamera viewHolderListCamera;
    public static boolean isDataCameraChanged;
    static ArrayList<Model> listModelName;

    public CustomizeListCamera(Context context, ArrayList<IPCam> ipCamArrayList) {
        this.context = context;
        this.ipCamArrayList = ipCamArrayList;

    }

    @Override
    public ViewHolderListCamera onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customize_list_camera, parent, false);

        return new ViewHolderListCamera(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderListCamera holder, int position) {

        this.viewHolderListCamera = holder;
        holder.tvCameraName.setText(ipCamArrayList.get(position).getName());
        String port_number = String.valueOf(ipCamArrayList.get(position).getRtsp_port());
        holder.tvPortNumber.setText(port_number);

        holder.btnMenuCameraList.setOnClickListener(view1 -> {
            isDataCameraChanged = false;
            PopupMenu popupMenu = new PopupMenu(context, holder.btnMenuCameraList);
            popupMenu.getMenuInflater().inflate(R.menu.sub_menu_list_camera, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.pop_view:
                        ArrayList<IPCam> ipCams = new ArrayList<IPCam>();
                        ipCams.add(ipCamArrayList.get(holder.getAdapterPosition()));

                        Intent intent = new Intent(context, CameraViewerActivity.class);
                        Parcelable listParcelable = Parcels.wrap(ipCams);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("list_camera", listParcelable);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        break;

                    case  R.id.pop_edit:
                        boolean wrapIconView = true;
                        MaterialDialog dialog;
                        dialog = new MaterialDialog.Builder(context)
                                .title(R.string.CameraModifier)
                                .customView(R.layout.customize_modifier_camera, true)
                                .positiveText(R.string.OK)
                                .build();
                        dialogComponent(dialog, holder);

                        final MaterialDialog.Builder mBuilder = dialog.getBuilder();
                        mBuilder.positiveText(R.string.Modifier)
                                .onPositive((dialog1, which) -> {
                                    Log.d(TAG, "positionSpModel: "+listModelName);

                                    if (ipCamArrayList.get(holder.getAdapterPosition()) != null && listModelName.get(positionSpModel) != null) {
                                        IPCameraModifier ipCameraModifier = new IPCameraModifier();
                                        ipCameraModifier.setCamera_id(ipCamArrayList.get(holder.getAdapterPosition()).getCamera_id());
                                        ipCameraModifier.setName(edCameraName.getText().toString());
                                        ipCameraModifier.setIp_address(edIp.getText().toString());
                                        ipCameraModifier.setWeb_port(Integer.valueOf(edWebPortNumber.getText().toString()));
                                        ipCameraModifier.setRtsp_port(Integer.valueOf(edRstpPort.getText().toString()));
                                        ipCameraModifier.setUsername(edUserName.getText().toString());
                                        ipCameraModifier.setPassword(edPassword.getText().toString());
                                        ipCameraModifier.setSerial_number(edSerialNumber.getText().toString());
                                        ipCameraModifier.setUser_id(ipCamArrayList.get(holder.getAdapterPosition()).getUser().getUser_id());
                                        ipCameraModifier.setModel_id(listModelName.get(positionSpModel).getModel_id());
                                        new CameraController().updateCamera(ipCameraModifier);

                                        //casting IPModifier to IPcam
                                        IPCam ipCam = new IPCam();
                                        ipCam.setCamera_id(ipCameraModifier.getCamera_id());
                                        ipCam.setName(ipCameraModifier.getName());
                                        ipCam.setIp_address(ipCameraModifier.getIp_address());
                                        ipCam.setWeb_port(ipCameraModifier.getWeb_port());
                                        ipCam.setRtsp_port(ipCameraModifier.getRtsp_port());
                                        ipCam.setUsername(ipCameraModifier.getUsername());
                                        ipCam.setPassword(ipCameraModifier.getPassword());
                                        ipCam.setSerial_number(ipCameraModifier.getSerial_number());
                                        ipCam.setUser(ipCamArrayList.get(holder.getAdapterPosition()).getUser());
                                        ipCam.setModel(listModelName.get(positionSpModel));

                                        ipCamArrayList.remove(holder.getAdapterPosition());    ////
                                        ipCamArrayList.add(ipCam);
                                        notifyDataSetChanged();
                                    }

                                })
                                .negativeText(R.string.Cancel)
                                .onNegative((dialog12, which) -> Log.d(TAG, "onClick: Negative"));
                        mBuilder.show();

                        break;

                    case R.id.pop_remove:
                        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(context);

                        Log.d(TAG, "onBindViewHolder: " + ipCamArrayList.get(holder.getAdapterPosition()).getCamera_id());
                        alerBuilder.setTitle(R.string.RemoveCamera)
                                .setIcon(R.drawable.ic_delete)
                                .setPositiveButton(R.string.Remove, (dialogInterface, i) -> {
                                    new CameraController().removeCamera(ipCamArrayList.get(holder.getAdapterPosition()).getCamera_id());
                                    ipCamArrayList.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());

                                })
                                .setNegativeButton(R.string.Dimmis, (dialogInterface, i) -> {

                                })
                                .show();
                        break;
                }
                return true;
            });
            popupMenu.show();
        });

        holder.cardView.setOnLongClickListener(v -> {
            ArrayList<IPCam> ipCams = new ArrayList<>();
            ipCams.add(ipCamArrayList.get(holder.getAdapterPosition()));

            Intent intent = new Intent(context, CameraViewerActivity.class);
            Parcelable listParcelable = Parcels.wrap(ipCams);
            Bundle bundle = new Bundle();
            bundle.putParcelable("list_camera", listParcelable);
            intent.putExtras(bundle);
            context.startActivity(intent);
            return true;
        });
    }

    Spinner spModel;
    Spinner spVender;
    EditText edCameraName;
    EditText edSerialNumber;
    EditText edIp;
    EditText edWebPortNumber;
    EditText edUserName;
    EditText edPassword;
    EditText edRstpPort;

    void dialogComponent(MaterialDialog dialog, ViewHolderListCamera holder) {

        spModel = (Spinner) dialog.findViewById(R.id.spModel);
        spVender = (Spinner) dialog.findViewById(R.id.spManufacturer);

        edCameraName = (EditText) dialog.findViewById(R.id.edCameraName);
        edSerialNumber = (EditText) dialog.findViewById(R.id.edSerialNumber);
        edIp = (EditText) dialog.findViewById(R.id.edIpAddress);
        edRstpPort = (EditText) dialog.findViewById(R.id.edRstpPort);
        edWebPortNumber = (EditText) dialog.findViewById(R.id.edWebPort);
        edUserName = (EditText) dialog.findViewById(R.id.edUsername);
        edPassword = (EditText) dialog.findViewById(R.id.edChPassword);

        edCameraName.setText(ipCamArrayList.get(holder.getAdapterPosition()).getName());
        edWebPortNumber.setText(String.valueOf(ipCamArrayList.get(holder.getAdapterPosition()).getWeb_port()));
        edRstpPort.setText(String.valueOf(ipCamArrayList.get(holder.getAdapterPosition()).getRtsp_port()));
        edSerialNumber.setText(String.valueOf(ipCamArrayList.get(holder.getAdapterPosition()).getSerial_number()));
        edIp.setText(ipCamArrayList.get(holder.getAdapterPosition()).getIp_address());
        edUserName.setText(ipCamArrayList.get(holder.getAdapterPosition()).getUsername());
        edPassword.setText(ipCamArrayList.get(holder.getAdapterPosition()).getPassword());

        new VenderController().getAllVender(this);  //load vender

        spVender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new ModelController().getModelNameByVenderName(spVender.getSelectedItem().toString(), CustomizeListCamera.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ipCamArrayList.size();
    }

    int positionSpModel;

    @Override
    public void loadModelName(ArrayList<Model> models) {

        this.listModelName = models;

        ArrayList<String> modelNames = new ArrayList<>();
        for (Model model : models) {
            modelNames.add(model.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, modelNames);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spModel.setAdapter(adapter);
        positionSpModel = adapter.getPosition(ipCamArrayList.get(viewHolderListCamera.getAdapterPosition()).getModel().getName());
        spModel.setSelection(positionSpModel);
    }

    @Override
    public void loadAllModel(ArrayList<Model> modelName) {

    }

    @Override
    public void loadModelById(Model model) {

    }

    @Override
    public void loadAllVender(ArrayList<Vender> venderArrayList) {
        ArrayList<String> venderName = new ArrayList<>();
        for (Vender vender : venderArrayList) {
            venderName.add(vender.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, venderName);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spVender.setAdapter(adapter);

        //set selected vender by current vender

        int positionSpVender = adapter.getPosition(ipCamArrayList.get(viewHolderListCamera.getAdapterPosition()).getModel().getVender().getName());
        spVender.setSelection(positionSpVender);
        //   Log.d(TAG, "loadAllVender: "+ipCamArrayList.get(getAdapterPosition()).getModel().getVender());
    }

    public void swap(ArrayList<IPCam> ipCams) {
        ipCamArrayList.clear();
        ipCamArrayList.addAll(ipCams);
        notifyDataSetChanged();
    }

}