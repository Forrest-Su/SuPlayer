package com.example.forrestsu.suplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.forrestsu.suplayer.R;

public class SettingFragment extends Fragment {

    private static final String TAG = "SettingFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }
}
