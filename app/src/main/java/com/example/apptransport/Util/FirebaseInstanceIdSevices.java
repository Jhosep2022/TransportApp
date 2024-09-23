package com.example.apptransport.Util;

import android.os.Build;

import android.util.Log;

import com.apreciasoft.mobile.asremis.BuildConfig;
import com.apreciasoft.mobile.asremis.Entity.token;
import com.apreciasoft.mobile.asremis.Entity.tokenFull;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.Services.ServicesLoguin;
import com.google.firebase.iid.FirebaseInstanceId;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.RequiresApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge gutierrez on 13/02/2017.
 */

public class FirebaseInstanceIdSevices extends FirebaseMessagingService {

    public static final String TAG = "NOTICIAS";
    static ServicesLoguin apiService = null;
    public static GlovalVar gloval;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "***Refreshed token*** " + token);
        enviarTokenAlServidor(token);
    }

    public static void enviarTokenAlServidor(String _str_token) {
        // Enviar token al servidor
        if(gloval != null) {
            apiService = HttpConexion.getUri().create(ServicesLoguin.class);

            token T = new token();

            T.setToken(new tokenFull(_str_token, gloval.getGv_user_id(), gloval.getGv_id_driver(), BuildConfig.VERSION_NAME));

            Call<Boolean> call = apiService.token(T);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            System.out.println(gson.toJson(T));

            call.enqueue(new Callback<Boolean>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                }

                public void onFailure(Call<Boolean> call, Throwable t) {


                    Log.d("**", t.getMessage());
                }
            });
        }


    }
}
