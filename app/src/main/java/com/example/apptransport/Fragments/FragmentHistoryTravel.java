package com.example.apptransport.Fragments;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Adapter.HistoryAdapter;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesDriver;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 19/1/2017.
 */

public class FragmentHistoryTravel extends Fragment {

    public static GlovalVar gloval;
    ServicesDriver apiService = null;
    View myView;
    List<InfoTravelEntity> list = null;
    HistoryAdapter adapter = null;
    RecyclerView rv = null;
    private ConstraintLayout constraintLayoutContenido;
    private LinearLayout layoutLoding;
    private TextView textViewNoHistorial;

    public int ver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.apiService = HttpConexion.getUri().create(ServicesDriver.class);
        gloval = ((GlovalVar)getActivity().getApplicationContext());
        myView = inflater.inflate(R.layout.fragment_history, container, false);
        addView();

        if(gloval.getGv_id_profile() == 3){
            serviceAllTravel();
        }else {
            serviceAllTravelClient();
        }

        return myView;
    }

    private void addView() {
        layoutLoding= myView.findViewById(R.id.conten_loagin);
        constraintLayoutContenido= myView.findViewById(R.id.conten_historial);
        constraintLayoutContenido.setVisibility(View.GONE);
        textViewNoHistorial= myView.findViewById(R.id.text_no_historia);

        rv =  myView.findViewById(R.id.rv_recycler_view);
    }

    private void refreshContent(){


        if (list.size()>0){
            textViewNoHistorial.setVisibility(View.GONE);

            rv.setHasFixedSize(true);
            int PARAM_26_VER_PRECIO_EN_VIAJE =  Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_26_VER_PRECIO_EN_VIAJE_EN_APP).getValue());
            adapter = new HistoryAdapter(list, PARAM_26_VER_PRECIO_EN_VIAJE, gloval.getGv_id_profile());
            rv.setAdapter(adapter);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
        }

    }

    public void serviceAllTravel() {

        GlovalVar gloval = ((GlovalVar)getActivity().getApplicationContext());
        apiService.getAllTravel(gloval.getGv_id_driver()).enqueue(new Callback<List<InfoTravelEntity>>() {
            @Override
            public void onResponse(Call<List<InfoTravelEntity>> call, Response<List<InfoTravelEntity>> response) {
                layoutLoding.setVisibility(View.GONE);
                constraintLayoutContenido.setVisibility(View.VISIBLE);

                try {
                    if (response.body()!=null && response.body().size()>0){
                        Log.e("INFORACION",response.body().get(0).makeJson());

                        list = response.body();
                        refreshContent();
                    }
                }catch (Exception e){
                    Log.e("ERROR HITORIAL CHOFER",e.toString());
                    showMessage(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<List<InfoTravelEntity>> call, Throwable t) {
                Log.e("FALLA HITORIAL CHOFER",t.toString());
                showMessage(getString(R.string.fallo_general),3);
            }
        });

    }

    public void serviceAllTravelClient() {

        GlovalVar gloval = ((GlovalVar)getActivity().getApplicationContext());
        apiService.getAllTravelClient(gloval.getGv_user_id()).enqueue(new Callback<List<InfoTravelEntity>>() {
            @Override
            public void onResponse(Call<List<InfoTravelEntity>> call, Response<List<InfoTravelEntity>> response) {
                layoutLoding.setVisibility(View.GONE);
                constraintLayoutContenido.setVisibility(View.VISIBLE);

                try {
                    if (response.body()!=null && response.body().size()>0){
                        list = response.body();
                        if (list.size()>0) {
                            Collections.sort(list, new Comparator<InfoTravelEntity>(){
                                public int compare(InfoTravelEntity obj1, InfoTravelEntity obj2) {
                                    // ## Ascending order
                                    //return obj1.getIdTravel()>obj2.getIdTravel(); // To compare string values
                                     return Integer.valueOf(obj1.getIdTravel()).compareTo(Integer.valueOf(obj2.getIdTravel())); // To compare integer values
                                    // ## Descending order
                                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                                }
                            });
                            Collections.reverse(list);
                        }
                        refreshContent();
                    }
                }catch (Exception e){
                    Log.e("ERROR HISTORIAL CHOFER",e.toString());
                    showMessage(getString(R.string.fallo_general),3);
                }

            }

            @Override
            public void onFailure(Call<List<InfoTravelEntity>> call, Throwable t) {
                Log.e("FALLA HITORIAL CHOFER",t.toString());
                showMessage(getString(R.string.fallo_general),3);
            }
        });

    }


    private void showMessage(String mensaje,int status){
        SnackCustomService.show(getActivity(),mensaje,status);
    }


}
