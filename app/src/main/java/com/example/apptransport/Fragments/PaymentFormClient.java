package com.example.apptransport.Fragments;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apreciasoft.mobile.asremis.R;

/**
 * @author Marlon Viana
 * @email 92marlonViana@gmail.com
 */
public class PaymentFormClient  extends Fragment {

    private View myView;
    public static final int CREDIT_CAR_ACTIVITY = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.payment_form_client, container, false);
        return myView;
    }


}
