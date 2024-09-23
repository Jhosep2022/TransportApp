package com.example.apptransport.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Adapter.ListenerNotification;
import com.apreciasoft.mobile.asremis.Adapter.NorificationAdapter;
import com.apreciasoft.mobile.asremis.Entity.notification;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesNotification;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by usario on 25/4/2017.
 */



public class NotificationsFrangment extends Fragment implements ListenerNotification {

    public static final int INFO_ACTIVITY = 1;
    public ProgressDialog loading;

    ServicesNotification apiService = null;
    View myView;
    List<notification> list = null;
    NorificationAdapter adapter = null;
    RecyclerView rv = null;

    private ConstraintLayout constraintLayoutContenido;
    private LinearLayout layoutLoding;
    private TextView textViewNoNotification;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.apiService = HttpConexion.getUri().create(ServicesNotification.class);
        myView = inflater.inflate(R.layout.fragment_notification, container, false);

        addView();
        serviceAllNotification();

        return myView;
    }

    private void addView() {
        layoutLoding= myView.findViewById(R.id.conten_loagin);
        constraintLayoutContenido= myView.findViewById(R.id.conten_notifiaciones);
        constraintLayoutContenido.setVisibility(View.GONE);
        textViewNoNotification= myView.findViewById(R.id.text_no_notificaicones);
        rv =  myView.findViewById(R.id.rv_recycler_view);
    }


    // this is just for demonstration, not real code!
    private void refreshContent(){

        if (list.size()>0){
            textViewNoNotification.setVisibility(View.GONE);

            rv.setHasFixedSize(true);
            adapter = new NorificationAdapter(list,NotificationsFrangment.this,myView.getContext(),this);
            rv.setAdapter(adapter);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
        }else {
            rv.setVisibility(View.GONE);
            textViewNoNotification.setVisibility(View.VISIBLE);
        }

    }

    public void serviceAllNotification() {
        final GlovalVar gloval = ((GlovalVar)getActivity().getApplicationContext());
        apiService.getNotifications(gloval.getGv_user_id()).enqueue(new Callback<List<notification>>() {
            @Override
            public void onResponse(Call<List<notification>> call, Response<List<notification>> response) {
                try {

                    if (response.body()!=null){
                        layoutLoding.setVisibility(View.GONE);
                        constraintLayoutContenido.setVisibility(View.VISIBLE);

                        list = response.body();
                        gloval.setGv_listNotifications(list);
                        refreshContent();
                    }else {
                        layoutLoding.setVisibility(View.GONE);
                        constraintLayoutContenido.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){
                    Log.e("ERROR NOTIFICAICON", e.toString());
                    showMessage(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<List<notification>> call, Throwable t) {
                Log.e("FAILURE NOTIFICAICON", t.toString());

                layoutLoding.setVisibility(View.GONE);
                constraintLayoutContenido.setVisibility(View.VISIBLE);
                showMessage(getString(R.string.fallo_general),3);
            }
        });


    }

    @Override
    public void marcarNotifiacionVista(final int idNotificacion) {
        final GlovalVar gloval = ((GlovalVar)getActivity().getApplicationContext());
        apiService.readNotifications(idNotificacion,gloval.getGv_user_id()).enqueue(new Callback<List<notification>>() {
            @Override
            public void onResponse(Call<List<notification>> call, Response<List<notification>> response) {
                try {

                    if (response.body()!=null){
                        showMessage(getString(R.string.notificacion_leida)+String.valueOf(idNotificacion),1);
                        list =  response.body();
                        gloval.setGv_listNotifications(list);
                        refreshContent();
                    }else {
                        showMessage(getString(R.string.notificacion_leida)+String.valueOf(idNotificacion),1);
                        list= null;
                        refreshContent();
                    }


                }catch (Exception e){
                    Log.e("ERROR NOTIFICAIC", e.toString());
                    showMessage(getString(R.string.fallo_general),3);
                }

            }

            @Override
            public void onFailure(Call<List<notification>> call, Throwable t) {
                Log.e("FAILURE NOTIFICAICON", t.toString());

                showMessage(getString(R.string.fallo_general),3);
            }
        });
    }


    private void showMessage(String mensaje,int status){
        SnackCustomService.show(getActivity(),mensaje,status);
    }

}
