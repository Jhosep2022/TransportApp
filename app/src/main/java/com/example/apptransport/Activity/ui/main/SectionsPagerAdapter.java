package com.example.apptransport.Activity.ui.main;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.apreciasoft.mobile.asremis.Entity.SocialLoginData;
import com.apreciasoft.mobile.asremis.Fragments.ListenerRegisterChofer;
import com.apreciasoft.mobile.asremis.Fragments.NewDriverDatosPersonalesFragment;
import com.apreciasoft.mobile.asremis.Fragments.NewDriverFotosExtendedFragment;
import com.apreciasoft.mobile.asremis.Fragments.NewDriverFotosFragment;
import com.apreciasoft.mobile.asremis.Fragments.NewDriverVehiculoFragment;
import com.apreciasoft.mobile.asremis.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_1_datos_personales, R.string.tab_2_datos_vehiculo, R.string.tab_3_documentacion_vehiculo};
    private final Context mContext;
    private ListenerRegisterChofer listenerRegisterChofer;
    private SocialLoginData socialLoginData;
    public SectionsPagerAdapter(Context context, FragmentManager fm, ListenerRegisterChofer listener, SocialLoginData socialLoginData) {
        super(fm);
        mContext = context;
        this.listenerRegisterChofer = listener;
        this.socialLoginData=socialLoginData;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            NewDriverDatosPersonalesFragment fragment = new NewDriverDatosPersonalesFragment();
            Bundle args = new Bundle();
            args.putString("firstName", socialLoginData.getFirstName());
            args.putString("lastName", socialLoginData.getLastName());
            args.putString("email", socialLoginData.getEmail());
            args.putString("socialid", socialLoginData.getSocialid());
            fragment.setArguments(args);
            fragment.setListenerRegisterChofer(listenerRegisterChofer);
            return fragment;
        }else if(position==1)
        {
            NewDriverVehiculoFragment fragment = new NewDriverVehiculoFragment();
            Bundle bundle = new Bundle();
            //bundle.putInt(ARG_SECTION_NUMBER, index);
            fragment.setArguments(bundle);
            fragment.setListenerRegisterChofer(listenerRegisterChofer);
            return fragment;
        }
        else if(position==2)
        {
            NewDriverFotosExtendedFragment fragment = new NewDriverFotosExtendedFragment();
            Bundle bundle = new Bundle();
            //bundle.putInt(ARG_SECTION_NUMBER, index);
            fragment.setArguments(bundle);
            fragment.setListenerRegisterChofer(listenerRegisterChofer);
            return fragment;
        }
        return null;
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}