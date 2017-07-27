package com.kshrd.ipcam.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.fragment.FragVideo;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rina on 1/31/17.
 */

public class ListVideoRecyclerAdapter extends RecyclerView.Adapter<ListMyVideoRecyclerViewHolder> {

    private Intent intentView;
    private File currentClickFile;
    private String mimetype;
    private String extension;
    private ArrayList<File> listFiles;
    private Context context;
    private Uri videoSelectedUri;
    private String TAG = "ListVideo";

    public ListVideoRecyclerAdapter(Context context,ArrayList<File> listFiles){
        this.context = context;
        this.listFiles = listFiles;
    }

    @Override
    public ListMyVideoRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customize_list_video,parent,false);
        setChildSize(parent,view);
        return new ListMyVideoRecyclerViewHolder(view,context,listFiles);
    }

    public void setChildSize(ViewGroup parentView,View childView){
        ListMyVideoRecyclerViewHolder.parentHeight = (parentView.getMeasuredHeight() / FragVideo.ROW_ITEMS);
        ListMyVideoRecyclerViewHolder.parentWeight = (parentView.getMeasuredWidth()/ FragVideo.COLUMN_ITEMS);
        childView.setMinimumHeight(ListMyVideoRecyclerViewHolder.parentHeight);
    }

    @Override
    public void onBindViewHolder(ListMyVideoRecyclerViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.player.setSource(Uri.parse(listFiles.get(position).toString()));
        holder.player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.player.stop();
                if(intentView == null){
                    intentView = new Intent(android.content.Intent.ACTION_VIEW);
                }

                currentClickFile = new File(listFiles.get(position).getAbsolutePath());
                if (android.os.Build.VERSION.SDK_INT >= 24){      //  7.0 +
                    videoSelectedUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", currentClickFile);
                } else{
                    videoSelectedUri = Uri.fromFile(currentClickFile);
                }

                extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(videoSelectedUri.toString());
                mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                intentView.setDataAndType(videoSelectedUri,mimetype);
                intentView.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);     // New Code of if condition
                context.startActivity(intentView);
            }
        });
    }

    @Override
    public int getItemCount() {
     //   Log.d(TAG, "ListVideoRecyclerAdapter: "+arrayList.size());
        if(listFiles != null){
            return listFiles.size();
        }

        return 0;
    }
}