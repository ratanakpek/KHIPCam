
package com.kshrd.ipcam.activitys;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;
import com.kshrd.ipcam.R;
import com.kshrd.ipcam.entities.camera.IPCam;
import com.kshrd.ipcam.fragment.FragImage;
import com.kshrd.ipcam.fragment.FragVideo;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.Internet;
import com.kshrd.ipcam.recorder.SequenceEncoder;
import com.kshrd.ipcam.main.IPCamTheme;
import com.longdo.mjpegviewer.MjpegView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.parceler.Parcels;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Author Rina
 * @Class Streaming
 * @Purpose Real View
 */

public class CameraViewerActivity extends AppCompatActivity {

    private ImageView btnBack,btnZoomIn, btnZoomOut,btnCapture,btnRecord;
    private CircularImageView joystickControll;
    private boolean isRecording;
    private MjpegView view;
    private Joystick joystick;
    private int stateCmd = 0;  // cmdState limit cmd state as integer
    private CircularProgressBar circularProgressBar;
    private IPCam ipCam;
    private String TAG = "CameraViewerActivity";
    private String STREAMING_URL;
    private ProgressBar progressBar;
    private ImageView imgRecord;
    private Animation fadeOut, fadeIn;
    private SequenceEncoder se = null;
    private File folder, fileRecord;
    private RecordStream recordStream;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_viewer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//

        progressBar = (ProgressBar) findViewById(R.id.progress_stream_viewer);
        btnCapture = (ImageView) findViewById(R.id.btnCapture);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnZoomIn = (ImageView) findViewById(R.id.btnZoomIn);
        btnZoomOut = (ImageView) findViewById(R.id.btnZoomOut);
        imgRecord = (ImageView) findViewById(R.id.imgRecord);
        btnRecord = (ImageView) findViewById(R.id.button_start_stop_record);
        view = (MjpegView) findViewById(R.id.mjpegview);
        joystickControll = (CircularImageView) findViewById(R.id.joystick_controll);

        ipCam = getIpcamInfo();
        STREAMING_URL = ApiGenerator.URL_STREAMING + "/test?id=" + ipCam.getCamera_id() + "&addr=rtsp://" + ipCam.getUsername()
                + ":" + ipCam.getPassword() + "@" + ipCam.getIp_address() + ipCam.getModel().getStream_url();

        Log.i(TAG, ipCam.getCamera_id() + "onCreate: " + STREAMING_URL);

    //   STREAMING_URL = "http://webcams.hotelcozumel.com.mx:6003/axis-cgi/mjpg/video.cgi?resolution=320x240&dummy=1458771208837";
        Log.d(TAG, "onCreate: "+STREAMING_URL);
        setJoystick();



        view.setAdjustHeight(true);
        view.setMode(MjpegView.MODE_STRETCH);
        view.includeProgressBar(progressBar);
        view.setUrl(STREAMING_URL);

        btnRecord.setOnClickListener(v -> actionRecord());

        actionRecord();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    void actionRecord() {

        if(isRecording){
            isRecording = false;
            btnRecord.setImageResource(R.drawable.stop_record_video_button);
            FragVideo.isVideoAdded = true;
            try{
             //   if (se == null) {
                    Date now = new Date();
                    CharSequence date = DateFormat.format("yyyy_MM_dd_hh_mm_ss", now);
                    folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/ipcam");
                    if(!folder.exists()){
                        folder.mkdir();
                    }

                    if(fileRecord == null){
                        fileRecord = new File(folder.getAbsolutePath()+"/"+ date + "IPCAM.mp4");
                    }

                    se = new SequenceEncoder(fileRecord);

                    if(recordStream == null){
                        recordStream = new RecordStream();
                    }

                    recordStream.start();
                    recordAnimation();
            //    }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            isRecording = true;
            btnRecord.setImageResource(R.drawable.start_record_video_button);

            try {
                if (se != null) {
                    recordStream.cancel();
                    imgRecord.setVisibility(View.INVISIBLE);
                    clearAnimation();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void recordAnimation() {
        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(500);

        fadeOut = new AlphaAnimation(1.f, 0.0f);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setStartOffset(500);
        fadeOut.setDuration(500);

        imgRecord.setVisibility(View.VISIBLE);
        imgRecord.startAnimation(fadeIn);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgRecord.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgRecord.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void clearAnimation() {
        imgRecord.clearAnimation();
        fadeIn.setAnimationListener(null);
        fadeOut.setAnimationListener(null);
    }

    @Override
    protected void onResume() {
        view.startStream();

        changeAppStyle();

        super.onResume();
    }

    @Override
    protected void onPause() {
        view.stopStream();
        HomeActivity.isConfigurationChange = true;
        ListCameraActivity.isConfigurationChange = true;
        super.onPause();
    }

   /* @Override
    protected void onStop() {
        view.stopStream();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
*/
    /**
     * @return ipcam information
     * @Method getIpcamInfo
     */


    public IPCam getIpcamInfo() {
        Bundle bundle = getIntent().getExtras();
        IPCam ipCam = null;

        if (bundle != null) {
            if (bundle.getParcelable(HomeActivity.CAMERA_PARCELABLE_HOME) != null) {
                ipCam = Parcels.unwrap((bundle).getParcelable(HomeActivity.CAMERA_PARCELABLE_HOME));
            } else {
                ArrayList<IPCam> ipCams = Parcels.unwrap((bundle).getParcelable("list_camera"));
                if (ipCams != null) {
                    ipCam = ipCams.get(0);
                }
            }
        }
        return ipCam;
    }


    /**
     * @JoyStick Method
     */

    void setJoystick() {
        if (new Internet().isAvailable(this)) {
            joystick = (Joystick) findViewById(R.id.joystick);

            joystick.setJoystickListener(new JoystickListener() {
                @Override
                public void onDown() {

//               new CommandThread().execute(ipCam.getUser().getEmail(), ipCam.getCamera_id(), "stop");
                }

                @Override
                public void onDrag(float degrees, float distance) {
                    if (degrees >= 45 && degrees <= 135) {
                        // Log.d("UP", offset+"Up"+degrees);
                        if (distance == 1) {
                            stateCmd++;
                            if (stateCmd == 1) {
                                Log.d("UP", distance + "Up" + degrees);
                                new CommandThread().execute(ipCam.getUser().getEmail(), ipCam.getCamera_id(), "up");
                            } else if (stateCmd >= 3) {
                                stateCmd = 0;
                                new CommandThread().execute(ipCam.getUser().getEmail(), ipCam.getCamera_id(), "stop");
                            }
                        }
                    } else if (degrees >= -130 && degrees <= -50) {
                        if (distance == 1) {
                            stateCmd++;
                            if (stateCmd == 1) {
                                Log.d("DOWN", distance + "DOWN" + degrees);
                                new CommandThread().execute(ipCam.getUser().getEmail(), ipCam.getCamera_id(), "down");
                            } else if (stateCmd >= 3) {
                                stateCmd = 0;

                            }
                        }
                    } else if (degrees <= 25 && degrees >= -25) {
                        if (distance == 1) {
                            stateCmd++;
                            if (stateCmd == 1) {
                                Log.d("RIGHT", distance + "RIGHT" + degrees);
                                new CommandThread().execute(ipCam.getUser().getEmail(), ipCam.getCamera_id(), "right");
                            } else if (stateCmd >= 3) {
                                stateCmd = 0;
                                new CommandThread().execute(ipCam.getUser().getEmail(), ipCam.getCamera_id(), "stop");

                            }
                        }
                    } else if (degrees >= 165 || degrees <= -165) {
                        if (distance == 1) {
                            stateCmd++;
                            if (stateCmd == 1) {
                                Log.d("Left", distance + "Left" + degrees);
                                new CommandThread().execute(ipCam.getUser().getEmail(), ipCam.getCamera_id(), "left");
                            } else if (stateCmd >= 3) {
                                stateCmd = 0;
                                Log.d(TAG, "Left: stop ");
                            }
                        }
                    }
                }

                @Override
                public void onUp() {
                    new CommandThread().execute(ipCam.getUser().getEmail(), ipCam.getCamera_id(), "stop");
                }
            });
        }
    }

    /**
     * @param view
     * @Method backButton
     */

    public void backPressed(View view) {
        this.finish();
    }

    /**
     * @param view widget view button
     * @Method capture image
     */
    public void capture(View view) {
        verifyStoragePermissions(this);
        hideImageView(btnBack,btnZoomIn, btnZoomOut,btnCapture,btnRecord);
        joystick.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        takeScreenshot();

        showImageView(btnBack,btnZoomIn, btnZoomOut,btnCapture,btnRecord);
        joystick.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        FragImage.isImageAdded = true;
    }


    /**
     * @Methdo main Screenshot and then save it
     */
    private void takeScreenshot() {
        Date now = new Date();
        CharSequence s = DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory() + "/DCIM/ipcam/";
            File folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/ipcam/");
            if (!folder.exists()) {
                folder.mkdir();
            }
            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());

            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath + now + ".jpg");


            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    /**
     * @param imageFile
     * @Method open file image
     */
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    public void zoomIn(View view) {
        new CommandThread().execute(ipCam.getUser().getEmail(), ipCam.getCamera_id(), "zoomin");
    }

    public void zoomOut(View view) {
        new CommandThread().execute(ipCam.getUser().getEmail(), ipCam.getCamera_id(), "zoomout");
    }


    /**
     * Created by rina on 1/30/17.
     */


    /*opencv_core.IplImage img =  opencv_core.IplImage.create(500, 500, IPL_DEPTH_32F, 4);
    Frame frame;
    OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();*/
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CameraViewer Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    class RecordStream extends Thread {

        String tag = "MjpegDownloader";
        private boolean run = true;

        public void cancel() {
            run = false;
        }

        public boolean isRunning() {
            return run;
        }

        @Override
        public void run() {
            while (run) {

                HttpURLConnection connection = null;
                BufferedInputStream bis = null;
                URL serverUrl = null;
                try {
                    serverUrl = new URL(STREAMING_URL);

                    connection = (HttpURLConnection) serverUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();

                    //determine boundary pattern
                    //use the whole header as separator in case boundary locate in difference chunks
                    Pattern pattern = Pattern.compile("--[_a-zA-Z0-9]*boundary\\s+(.*)\\r\\n\\r\\n", Pattern.DOTALL);
                    Matcher matcher;

                    bis = new BufferedInputStream(connection.getInputStream());
                    byte[] image = new byte[0], read = new byte[4096], tmpCheckBoundry;
                    int readByte, boundaryIndex;
                    String checkHeaderStr, boundary;

                    //always keep reading images from server
                    while (run) {
                        try {
                            readByte = bis.read(read);
                            //no more data
                            if (readByte == -1) {
                                break;
                            }

                            tmpCheckBoundry = addByte(image, read, 0, readByte);
                            checkHeaderStr = new String(tmpCheckBoundry, "ASCII");

                            matcher = pattern.matcher(checkHeaderStr);
                            if (matcher.find()) {
                                //boundary is found
                                boundary = matcher.group(0);
                                boundaryIndex = checkHeaderStr.indexOf(boundary);
                                boundaryIndex -= image.length;

                                if (boundaryIndex > 0) {
                                    image = addByte(image, read, 0, boundaryIndex);
                                } else {
                                    image = delByte(image, -boundaryIndex);
                                }

                                Bitmap outputImg = BitmapFactory.decodeByteArray(image, 0, image.length);
                                if (outputImg != null) {
                                    if (run) {
                                        newFrame(outputImg);
                                    }
                                } else {
                                    Log.e(tag, "Read image error");
                                }

                                int headerIndex = boundaryIndex + boundary.length();

                                image = addByte(new byte[0], read, headerIndex, readByte - headerIndex);
                            } else {
                                image = addByte(image, read, 0, readByte);
                            }
                        } catch (Exception e) {
                            Log.e(tag, e.getMessage());
                            break;
                        }
                    }
                   if(isRunning()==false){
                       se.finish();
                       Log.d(TAG, "Record is stop");
                   }
                } catch (Exception e) {
                    Log.e(tag, e.getMessage());
                }

                try {

                    try {
                        bis.close();                        // HERE IS CHIVORN IMPROVE CODE . JUST CATCH WITH NULL POINTER. BECAUSE HAVE ERROR SOMETIMES.
                    } catch (NullPointerException e) {
                    }

                    connection.disconnect();
                    Log.i(tag, "disconnected with " + STREAMING_URL);
                } catch (Exception e) {
                    Log.e(tag, e.getMessage());
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Log.e(tag, e.getMessage());
                }
            }
        }

        private byte[] addByte(byte[] base, byte[] add, int addIndex, int length) {
            byte[] tmp = new byte[base.length + length];
            System.arraycopy(base, 0, tmp, 0, base.length);
            System.arraycopy(add, addIndex, tmp, base.length, length);
            return tmp;
        }

        private byte[] delByte(byte[] base, int del) {
            byte[] tmp = new byte[base.length - del];
            System.arraycopy(base, 0, tmp, 0, tmp.length);
            return tmp;
        }

        private void newFrame(Bitmap bitmap) {

            Log.d(TAG, "FILE DOWNLOAD: "+bitmap);
            try {

                se.encodeNativeFrame(fromBitmap(bitmap));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void changeAppStyle(){
        getWindow().getDecorView().setBackgroundColor(IPCamTheme.windowColor);
        changeImageViewColor(btnBack,btnZoomIn, btnZoomOut,btnCapture,btnRecord);
        joystickControll.setBorderColor(IPCamTheme.currentMainColor);
    }

    public void changeImageViewColor(ImageView... imageViews) {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setColorFilter(IPCamTheme.currentMainColor);
        }
    }


    public void showImageView(ImageView... imageViews) {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setVisibility(View.VISIBLE);
        }
    }

    public void hideImageView(ImageView... imageViews) {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setVisibility(View.INVISIBLE);
        }
    }


    /*New Code to record video*/

    // convert from Bitmap to Picture (jcodec native structure)
    public Picture fromBitmap(Bitmap src) {
        Picture dst = Picture.create((int)src.getWidth(), (int)src.getHeight(), ColorSpace.RGB);
        fromBitmap(src, dst);
        return dst;
    }

    public void fromBitmap(Bitmap src, Picture dst) {
        int[] dstData = dst.getPlaneData(0);
        int[] packed = new int[src.getWidth() * src.getHeight()];

        src.getPixels(packed, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());

        for (int i = 0, srcOff = 0, dstOff = 0; i < src.getHeight(); i++) {
            for (int j = 0; j < src.getWidth(); j++, srcOff++, dstOff += 3) {
                int rgb = packed[srcOff];
                dstData[dstOff]     = (rgb >> 16) & 0xff;
                dstData[dstOff + 1] = (rgb >> 8) & 0xff;
                dstData[dstOff + 2] = rgb & 0xff;
            }
        }
    }
}