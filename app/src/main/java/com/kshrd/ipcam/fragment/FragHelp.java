package com.kshrd.ipcam.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.main.MyFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragHelp extends MyFragment {

    private static FragHelp instance = null;
    TextView helpContent;

    public FragHelp() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.frag_help, container, false);

        helpContent = (TextView) rootView.findViewById(R.id.helpContent);
        return rootView;
    }

    public static FragHelp getInstance(){
        try {
            if (instance == null){
                Log.i("Help", "create new instance: ");
                instance = new FragHelp();

                Bundle bundle = new Bundle();
                //    bundle.putString("","");
                instance.setArguments(bundle);

                return instance;

            }else {
                Log.i("Help", "get old instance: ");
                return instance;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onResume() {
        setTextViewColorAsTextHintColor(helpContent);
        super.onResume();
    }
}
