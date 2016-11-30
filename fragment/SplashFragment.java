package com.example.fanvlad.superremindergood.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fanvlad.superremindergood.R;

import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {


    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SplashTask splashTask = new SplashTask();
        splashTask.execute();

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
    // показует сплешскрин
    class SplashTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //время показа, сплешскрина 2 сек.
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // для доступа к активити
            if(getActivity() != null) {
                getActivity().getFragmentManager().popBackStack();
            }
            return null;
        }
    }
}
