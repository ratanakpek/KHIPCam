package com.kshrd.ipcam.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.entities.camera.IPCam;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.main.IPCamTheme;
import com.longdo.mjpegviewer.MjpegView;

import java.util.ArrayList;

/**
 * Created by Chivorn on 1/2/2017.
 */

public class CustomHomeScreenAdapter extends RecyclerView.Adapter<CustomHomeScreenAdapter.DynamicCameraViewHolder> {
    private static final String TAG = "LOG";
    private ArrayList<IPCam> ipCamsDatas;
    private Communicator communicator;
    private LinearLayout linearLayout;
    private IPCam ipcamDataObject;
    private int parentHeight, parentWeight;
    private Context tempContext;
    private ArrayList<MjpegView> MjpegViewDatas;
    private String STREAMING_URL;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    private int row = 1;

    public CustomHomeScreenAdapter(ArrayList<IPCam> ipCamsDatas, Context context) {
        this.ipCamsDatas = ipCamsDatas;
        communicator = (Communicator) context;
        tempContext = context;
    }


    @Override
    public DynamicCameraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_show_camera_layout, parent, false);
        setRowPerScreen(getRow(), parent, v);
        return new DynamicCameraViewHolder(v);
    }

    public void setRowPerScreen(int rowPerScreen, ViewGroup parentView, View childView) {
        parentHeight = (parentView.getMeasuredHeight() / rowPerScreen);
        parentWeight = (parentView.getMeasuredWidth());
        //   parentWeight = parentHeight;
        childView.setMinimumHeight(parentHeight);

    }

    @Override
    public void onBindViewHolder(DynamicCameraViewHolder holder, int position) {
        ipcamDataObject = ipCamsDatas.get(position);

        if (ipcamDataObject != null) {
            bindMjpegViewData(ipcamDataObject, holder, position);
            setMjpegViewOnClickListener(ipcamDataObject, holder, position);
        }
    }

    // For Testing. It can remove.
    public String setTestURL(int position) {
        if (position == 0) {
            return "http://plazacam.studentaffairs.duke.edu/mjpg/video.mjpg";
        } else if (position == 1) {
            return "http://webcam.st-malo.com/axis-cgi/mjpg/video.cgi?resolution=640x480";
        } else if (position == 2) {
            return "http://webcams.hotelcozumel.com.mx:6003/axis-cgi/mjpg/video.cgi?resolution=320x240&dummy=1458771208837";
        } else if (position == 3) {
            return "http://iris.not.iac.es/axis-cgi/mjpg/video.cgi?resolution=320x240";
        } else if (position == 4) {
            return "http://bma-itic1.iticfoundation.org/mjpeg2.php?camid=61.91.182.114:1111";
        } else if (position == 5) {
            return "http://bma-itic1.iticfoundation.org/mjpeg2.php?camid=61.91.182.114:1112";
        } else {
            return "http://plazacam.studentaffairs.duke.edu/mjpg/video.mjpg";
        }
    }

    public void bindMjpegViewData(IPCam ipCam, DynamicCameraViewHolder holder, int position) {

        if (ipCam.getModel() != null) {
            STREAMING_URL = ApiGenerator.URL_STREAMING + "/test?id=" + ipCam.getCamera_id() + "&addr=rtsp://" + ipCam.getUsername()
                    + ":" + ipCam.getPassword() + "@" + ipCam.getIp_address() + ipCam.getModel().getStream_url();

            holder.mjpegView.setUrl(STREAMING_URL);

            // TESTING LINK.
            //  holder.mjpegView.setUrl(setTestURL(position));

            checkPlayAndStopStream(holder);
            holder.imageAddAndPlay.setImageResource(R.drawable.play_btn);

            if (ipcamDataObject != null) {
                setMjpegViewOnLongClickListener(ipcamDataObject, holder, position);
            }

        } else {
            holder.imageAddAndPlay.setImageResource(R.drawable.add_more_camera);
        }

        //  holder.imageAddAndPlay.setColorFilter(IPCamTheme.currentMainColor);
    }


    public void setMjpegViewOnClickListener(IPCam ipCam, DynamicCameraViewHolder holder, int position) {

        if (ipCam.getModel() == null) {
            holder.mjpegView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    communicator.onAddMoreCamera(position);
                }
            });

        } else {

            holder.mjpegView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPlayAndStopStream(holder);
                }
            });
        }
    }

    public void checkPlayAndStopStream(DynamicCameraViewHolder holder) {
        if (holder.mjpegView.isStreaming()) {
            stopStreamCurrentView(holder);
        } else {
            startStreamCurrentView(holder);
        }
    }

    public void setMjpegViewOnLongClickListener(IPCam ipcam, DynamicCameraViewHolder holder, int position) {
        holder.mjpegView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (holder.mjpegView.isStreaming()) {
                    stopStreamCurrentView(holder);
                    stopAllStream();
                    //   communicator.onStreamFullScreen(position, ipcam);     // if want to open when while playing only
                    //  return true;
                }

                communicator.onStreamFullScreen(position, ipcam);  // if playing not important to open.

                return false;
            }
        });
    }

    public void startStreamCurrentView(DynamicCameraViewHolder holder) {
        holder.imageAddAndPlay.setVisibility(View.INVISIBLE);
        MjpegViewDatas.add(holder.mjpegView);
        holder.mjpegView.startStream();
    }

    public void stopStreamCurrentView(DynamicCameraViewHolder holder) {
        MjpegViewDatas.remove(holder.mjpegView);
        holder.mjpegView.stopStream();
        holder.imageAddAndPlay.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return ipCamsDatas.size();
    }

    class DynamicCameraViewHolder extends RecyclerView.ViewHolder {

        MjpegView mjpegView;
        ImageView imageAddAndPlay;
        ProgressBar progressBar;
        CardView cardView;

        public DynamicCameraViewHolder(View itemView) {
            super(itemView);

            imageAddAndPlay = (ImageView) itemView.findViewById(R.id.image_add_and_stream_camera);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_stream_home);
            cardView = (CardView) itemView.findViewById(R.id.show_dynamic_camera_layout);

            mjpegView = (MjpegView) itemView.findViewById(R.id.MJPEG_VIEW_HOME);
            mjpegView.setMode(MjpegView.MODE_STRETCH);
            mjpegView.includeProgressBar(progressBar);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(parentWeight, parentHeight);
            mjpegView.setLayoutParams(params);

            changeAppStyle();
        }

        public void changeAppStyle() {
            cardView.setBackgroundColor(IPCamTheme.card_camera_background);
            imageAddAndPlay.setColorFilter(IPCamTheme.currentMainColor);
        }
    }

    public void stopAllStream() {
        if (isMjpegDataAvailable()) {
            Log.i(TAG, "***************************************  Stop Stream ***************************************");
            Log.i(TAG, "Streaming Object:  ===========================>>>>>  : " + MjpegViewDatas.size() + " :<<<<<===========================");
            for (int i = 0; i < MjpegViewDatas.size(); i++) {
                MjpegViewDatas.get(i).stopStream();
            }
        }
    }

    public void startOldStream() {
        if (isMjpegDataAvailable()) {
            Log.i(TAG, "***************************************  Start Stream ***************************************");
            Log.i(TAG, "Old Stream Object:  ===========================>>>>>  : " + MjpegViewDatas.size());
            for (int i = 0; i < MjpegViewDatas.size(); i++) {
                MjpegViewDatas.get(i).startStream();
            }
        }
    }

    public boolean isMjpegDataAvailable() {
        if (MjpegViewDatas != null) {
            if (MjpegViewDatas.size() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //  Log.i(TAG, "onAttachedToRecyclerView: ========================================>>>>>>>>>>>> Create new   ");
        stopAllStream();
        MjpegViewDatas = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        //  return super.getItemViewType(position);   // CANNOT USE THIS TO KEEP OLD BINDED DATA.
        return position;    // USE THIS FOR KEEP OLD VIEW DATA WHEN SCROLL.
    }
}