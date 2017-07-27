package com.kshrd.ipcam.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.kshrd.ipcam.fragment.FragHelp;
import com.kshrd.ipcam.fragment.FragHomeScreen;
import com.kshrd.ipcam.fragment.FragImage;
import com.kshrd.ipcam.fragment.FragVideo;

/**
 * Created by Chivorn on 1/9/2017.
 */

// Extend from SmartFragmentStatePagerAdapter now instead for more dynamic ViewPager items
public  class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
    private static int NUM_ITEMS = 4;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragHomeScreen.getInstance();
            case 1:
                return FragVideo.getInstance();
            case 2:
                return FragImage.getInstance();
            case 3:
                return FragHelp.getInstance();
            default:
                return null;
        }
    }
}
