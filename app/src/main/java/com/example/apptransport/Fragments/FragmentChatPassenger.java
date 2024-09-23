package com.example.apptransport.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apreciasoft.mobile.asremis.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentChatPassenger  extends Fragment {
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_chat_passenger, container, false);
        return myView;
    }

}