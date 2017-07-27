package com.kshrd.ipcam.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.fragment.FragImage;

import java.util.ArrayList;

/**
 * Created by rina on 1/27/17.
 */

public class ListImageAdapter extends RecyclerView.Adapter<ListImageViewHolder> {
    private ImageView imgView;
    private ArrayList<String> imageListUrl = new ArrayList<>();;
    private static Context TempContext;  // need to be static to prevent null.

    public ListImageAdapter() {
    }

    public ListImageAdapter(ImageView imgView, Context context) {
        this.imgView = imgView;
        TempContext = context;
    }

    public void add(String path) {
        imageListUrl.add(path);
    }

    public void clear() {
//        imageListUrl.clear();
    }

    void remove(int index) {
        imageListUrl.remove(index);
    }

    @Override
    public ListImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_list_image, parent, false);
        setChildSize(parent, view);
        return new ListImageViewHolder(view, TempContext, imageListUrl);
    }

    public void setChildSize(ViewGroup parentView, View childView) {
        ListImageViewHolder.parentHeight = (parentView.getMeasuredHeight() / FragImage.ROW_ITEMS);
        ListImageViewHolder.parentWeight = (parentView.getMeasuredWidth() / FragImage.COLUMN_ITEMS);
        childView.setMinimumHeight(ListImageViewHolder.parentHeight);
    }

    @Override
    public void onBindViewHolder(ListImageViewHolder holder, int position) {
        holder.imgListView.setImageBitmap(decodeSampledBitmapFromUri(imageListUrl.get(position), 500, 500));
        holder.imgListView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public int getItemCount() {
        return imageListUrl.size();
    }


    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
                                             int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);


        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height
                        / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
