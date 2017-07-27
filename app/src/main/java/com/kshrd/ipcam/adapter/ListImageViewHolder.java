package com.kshrd.ipcam.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.activitys.ConfigCameraActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rina on 1/27/17.
 */

public class ListImageViewHolder extends RecyclerView.ViewHolder {
    protected ImageView imgListView;
    private Context tempContext;
    private ArrayList<String> arrayList;
    public static int parentWeight,parentHeight;
    private Intent intent;
    private Uri uri;

    public ListImageViewHolder(View view,Context context,ArrayList<String> arrayList) {
        super(view);
        this.tempContext = context;
        this.arrayList = arrayList;
        imgListView = (ImageView)view.findViewById(R.id.img_list);
        imgListView.setOnClickListener(v -> {
            openScreenshot(new File(arrayList.get(getAdapterPosition())));
        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(parentWeight, parentHeight);
        imgListView.setLayoutParams(params);

    }
    /**
     * @param imageFile
     * @Method open file image
     */

    private void openScreenshot(File imageFile) {
        if(intent == null){
            intent = new Intent();
        }

        intent.setAction(Intent.ACTION_VIEW);

        if (android.os.Build.VERSION.SDK_INT >= 24){      //  7.0 +
              uri = FileProvider.getUriForFile(tempContext, tempContext.getApplicationContext().getPackageName() + ".provider", imageFile);
        } else{
            uri = Uri.fromFile(imageFile);
        }

        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);     // New Code of if condition
        tempContext.startActivity(intent);
    }
}
