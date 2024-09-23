package com.example.apptransport.Fragments;

import androidx.fragment.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Activity.InfoDetailTravelActivity;
import com.apreciasoft.mobile.asremis.Adapter.ListenerReserva;
import com.apreciasoft.mobile.asremis.Adapter.ListenerReservationUpdate;
import com.apreciasoft.mobile.asremis.Adapter.ReservationsAdapter;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesTravel;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Marlon Viana
 * @email 92marlonViana@gmail.com
 */

public class ReservationsFrangment extends Fragment implements ListenerReserva {


    public ProgressDialog loading;
    public static final int INFO_ACTIVITY = 1;
    public static final int RESULT_OK = 2;
    ServicesTravel apiService = null;
    View myView;
    ReservationsAdapter adapter = null;
    RecyclerView rv = null;
    List<InfoTravelEntity> list;
    private ListenerReservationUpdate reservationUpdateListener = null;
    private ConstraintLayout constraintLayoutContenido;
    private LinearLayout layoutLoding;
    private TextView textViewNoReservas;
    private boolean isClient=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_reservations_driver, container, false);
        this.apiService = HttpConexion.getUri().create(ServicesTravel.class);

        addView();
        serviceAllNotification();

        return myView;
    }

    private void addView() {
        layoutLoding= myView.findViewById(R.id.conten_loagin);
        constraintLayoutContenido= myView.findViewById(R.id.conten_notifiaciones);
        constraintLayoutContenido.setVisibility(View.GONE);
        textViewNoReservas= myView.findViewById(R.id.text_no_notificaicones);
        rv =  myView.findViewById(R.id.rv_recycler_view);
    }

    public void attachReservationListener(ListenerReservationUpdate listener){
        this.reservationUpdateListener = listener;
    }



    private  void refreshContent(){

        if (list.size()>0){
            textViewNoReservas.setVisibility(View.GONE);

            rv.setHasFixedSize(true);
            adapter = new ReservationsAdapter(list,this,isClient);
            rv.setAdapter(adapter);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
        }else {
            rv.setVisibility(View.GONE);
            textViewNoReservas.setVisibility(View.VISIBLE);
        }

    }

    public void serviceAllNotification() {
        final GlovalVar gloval = ((GlovalVar)getActivity().getApplicationContext());

        Call<List<InfoTravelEntity>> call = null;

        if(gloval.getGv_id_profile() ==  3){
            call =  this.apiService.getReservations(gloval.getGv_id_driver(),0,3);

            Log.e("ID DRIVER",String.valueOf(gloval.getGv_id_driver()));
            Log.e("ID USER","0");
            Log.e("ID PROFILE","3");

        }
        else if(gloval.getGv_id_profile() ==  4 || Utils.isClienteCompany(gloval.getGv_id_profile())) {
            isClient=true;
            call =  this.apiService.getReservations(0,gloval.getGv_user_id(),gloval.getGv_id_profile()); //getGv_user_id()

            Log.e("ID DRIVER","0");
            Log.e("ID USER",String.valueOf(gloval.getGv_user_id()));
            Log.e("ID PROFILE",String.valueOf(gloval.getGv_id_profile()));

        } else if (Utils.isClienteParticular(gloval.getGv_id_profile())) {
            isClient=true;
            call =  this.apiService.getReservations(0,gloval.getGv_id_cliet(),gloval.getGv_id_profile());

            Log.e("ID DRIVER","0");
            Log.e("ID USER",String.valueOf(gloval.getGv_id_cliet()));
            Log.e("ID PROFILE",String.valueOf(gloval.getGv_id_profile()));
        }

        call.enqueue(new Callback<List<InfoTravelEntity>>() {
            @Override
            public void onResponse(Call<List<InfoTravelEntity>> call, Response<List<InfoTravelEntity>> response) {

                try {
                    layoutLoding.setVisibility(View.GONE);
                    constraintLayoutContenido.setVisibility(View.VISIBLE);
                    list = response.body()!= null ? response.body() : new ArrayList<InfoTravelEntity>();
                    gloval.setGv_lisReservations(list);
                    filterListCanceledByClient();
                    refreshContent();

                }catch (Exception e){
                    Log.e("ERROR RESERVAS", e.toString());
                    showMessage(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<List<InfoTravelEntity>> call, Throwable t) {
                Log.e("FAILURE RESERVAS", t.toString());
                showMessage(getString(R.string.fallo_general),3);

            }
        });
    }

    private void filterListCanceledByClient()
    {
        for(int i=list.size()-1; i>=0;i--)
        {
            if(list.get(i).getIdSatatusTravel()==Globales.StatusTravel.VIAJE_CANCELADO_POR_CLIENTE){
                list.remove(i);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.serviceAllNotification();
        if(reservationUpdateListener!=null)
        {
            reservationUpdateListener.updateAppBarNotifications();
        }
    }



    private void showMessage(String mensaje,int status){
        SnackCustomService.show(getActivity(),mensaje,status);
    }

    @Override
    public void reservaSelecion(InfoTravelEntity infoTravelEntity) {
        Intent intent = new Intent( myView.getContext(), InfoDetailTravelActivity.class);
        intent.putExtra("TRAVEL",infoTravelEntity);
        if (isClient){
            intent.putExtra("CLIENTE",true);
        }else {
            intent.putExtra("CLIENTE",false);
        }
        startActivityForResult(intent, INFO_ACTIVITY);
    }
}
