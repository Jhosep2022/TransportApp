package com.example.apptransport.Fragments;

import androidx.fragment.app.Fragment;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Adapter.PayAdapter;
import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.DriverCurrentAcountEntity;
import com.apreciasoft.mobile.asremis.Entity.LiquidationEntity;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesDriver;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge gutierrez on 13/04/2017.
 */

public class AccountStatusFragment extends Fragment{


    ServicesDriver apiService = null;
    View myView;
    DriverCurrentAcountEntity currentAcountDriver = null;
    PayAdapter adapter = null;

    private LinearLayout layoutLoading;
    private ConstraintLayout constraintLayoutContenido;
    private TextView textViewNoMovimiento;
    private RecyclerView recyclerViewMovimientoCuenta = null;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_account_status,container,false);
        return myView;
    }



    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.apiService = HttpConexion.getUri().create(ServicesDriver.class);
        addView();
        serviceAllTravel();

    }

    private void addView() {
        layoutLoading= myView.findViewById(R.id.conten_loagin);
        constraintLayoutContenido= myView.findViewById(R.id.conten_payment);
        constraintLayoutContenido.setVisibility(View.GONE);

        textViewNoMovimiento= myView.findViewById(R.id.text_no_moviemiento);

        recyclerViewMovimientoCuenta = myView.findViewById(R.id.recycler_view_list);
    }


    public void  loadTable()
    {


        recyclerViewMovimientoCuenta.setHasFixedSize(true);

         List<LiquidationEntity> LIST_NEW = new ArrayList<LiquidationEntity>();
        if(currentAcountDriver.getLiquidation() != null ) {
            LIST_NEW.addAll(currentAcountDriver.getLiquidation());
        }


        if(currentAcountDriver.getAdvance() != null) {
             LIST_NEW.addAll(currentAcountDriver.getAdvance());
         }

        if(currentAcountDriver.getPay() != null){
            LIST_NEW.addAll(currentAcountDriver.getPay());
        }

        if (LIST_NEW.size()>0){
            textViewNoMovimiento.setVisibility(View.GONE);

            adapter = new PayAdapter(LIST_NEW);
            recyclerViewMovimientoCuenta.setAdapter(adapter);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            recyclerViewMovimientoCuenta.setLayoutManager(llm);
        }



    }



    public void serviceAllTravel() {

        GlovalVar gloval = ((GlovalVar)getActivity().getApplicationContext());
        apiService.listLiquidationDriver(gloval.getGv_id_driver()).enqueue(new Callback<DriverCurrentAcountEntity>() {
            @Override
            public void onResponse(Call<DriverCurrentAcountEntity> call, Response<DriverCurrentAcountEntity> response) {
                try {
                    Currency currency = Utils.getCurrency(getActivity().getApplicationContext());
                    constraintLayoutContenido.setVisibility(View.VISIBLE);
                    layoutLoading.setVisibility(View.GONE);

                    currentAcountDriver =  response.body();

                    Log.e("RESPUESTA ESTADO CUENTA",response.body().makeJson());

                    System.out.println(currentAcountDriver.getTotal().getIngreso());

                    TextView txt_totalpagar =  myView.findViewById(R.id.txt_totalpagar);
                    TextView txt_totalfavor  =  myView.findViewById(R.id.txt_totalfavor);
                    TextView saldo =  myView.findViewById(R.id.txt_saldo);



                    txt_totalpagar.setText(currency.getSymbol().concat(" ").concat(currentAcountDriver.getTotal().getIngreso()));
                    txt_totalfavor.setText(currency.getSymbol().concat(" ").concat(currentAcountDriver.getTotal().getEgreso()));


                    double saldoValue =
                            Double.parseDouble(currentAcountDriver.getTotal().getIngreso())-
                                    Double.parseDouble(currentAcountDriver.getTotal().getEgreso());
                    saldo.setText(currency.getSymbol().concat(" ").concat(String.valueOf(saldoValue)));


                    if(currentAcountDriver.getLiquidation() != null  || currentAcountDriver.getAdvance() != null || currentAcountDriver.getPay() != null )
                    {
                        loadTable();
                    }

                }catch (Exception e){
                    showMessage(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<DriverCurrentAcountEntity> call, Throwable t) {
                showMessage(getString(R.string.fallo_general),3);
            }
        });


    }


    private void showMessage(String mensaje,int status){
        SnackCustomService.show(getActivity(),mensaje,status);
    }
}
