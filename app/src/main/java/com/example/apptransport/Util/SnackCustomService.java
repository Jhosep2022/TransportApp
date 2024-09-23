package com.example.apptransport.Util;

import android.app.Activity;

import com.jeevandeshmukh.glidetoastlib.GlideToast;

public class SnackCustomService {

    public static void show(Activity context, String mensage,int tipe, int mils){
        show(context,mensage,tipe,mils,false);
    }


    public static void show(Activity context, String mensage,int tipe){
        show(context,mensage,tipe,3500,true);
    }

    private static void show(Activity context, String mensage,int tipe, int mils, boolean isDefaultMils){
        try
        {
            if (tipe==0){
                new GlideCustomToast.makeToast(
                        context
                        ,mensage
                        ,mils
                        ,GlideToast.INFOTOAST)
                        .show();
            }

            if (tipe==1){
                new GlideCustomToast.makeToast(
                        context
                        ,mensage
                        ,mils
                        ,GlideToast.SUCCESSTOAST)
                        .show();
            }

            if (tipe==2){
                new GlideCustomToast.makeToast(
                        context
                        ,mensage
                        ,mils
                        ,GlideToast.FAILTOAST)
                        .show();
            }

            if (tipe==3){
                new GlideCustomToast.makeToast(
                        context
                        ,mensage
                        ,mils
                        ,GlideToast.WARNINGTOAST)
                        .show();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
