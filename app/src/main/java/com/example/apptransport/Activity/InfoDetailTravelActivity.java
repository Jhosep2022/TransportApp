package com.example.apptransport.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Fragments.ReservationsFrangment;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesTravel;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Marlon Viana
 * @email 92marlonViana@gmail.com
 */
public class InfoDetailTravelActivity extends AppCompatActivity {

    ServicesTravel apiService = null;
    public Button button1 = null;
    public Button bt_confirmar_reserva = null;
    public Button btn_init_reserva = null;
    public ServicesTravel daoTravel = null;
    public GlovalVar gloval;
    public ProgressDialog loading;
    private InfoTravelEntity travel;
    private boolean isClient;
    private LinearLayout linearLayoutButtons;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_detail_travel);

        Currency currency = Utils.getCurrency(getApplicationContext());
        this.apiService = HttpConexion.getUri().create(ServicesTravel.class);

        travel = (InfoTravelEntity) getIntent().getSerializableExtra("TRAVEL");
        isClient = (boolean) getIntent().getSerializableExtra("CLIENTE");

        final TextView txt_nr_travel =  findViewById(R.id.txt_nr_travel);
        final TextView txt_client_info =  findViewById(R.id.txt_client_info);
        final TextView txt_km_info =  findViewById(R.id.txt_km_info);
        final TextView txt_amount_info = findViewById(R.id.txt_amount_info);
        final TextView txt_date_info =  findViewById(R.id.txt_date_info);
        final TextView txt_origin_info =  findViewById(R.id.txt_origin_info);
        final TextView txt_destination_info =  findViewById(R.id.txt_destination_info);
        final TextView txt_observationFromDriver =  findViewById(R.id.txt_observationFromDriver);
        final TextView txt_pasajeros_info =  findViewById(R.id.txt_pasajeros_info);
        final TextView txt_lote = findViewById(R.id.txt_lote);
        final TextView txt_flete = findViewById(R.id.txt_flete);
        final TextView txt_piso_dialog = findViewById(R.id.txt_piso_dialog);
        final TextView txt_dpto_dialog = findViewById(R.id.txt_dpto_dialog);
        final TextView txt_calling_info = findViewById(R.id.txt_calling_info);

        linearLayoutButtons= findViewById(R.id.linearLayout15);

      //  if (isClient){
           // linearLayoutButtons.setVisibility(View.GONE);
     //   }

        ImageButton imageButtonCerrar= findViewById(R.id.imageButton_cerrar);
        imageButtonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // variable global //
        gloval = ((GlovalVar)getApplicationContext());

        bt_confirmar_reserva =  findViewById(R.id.bt_confirmar_reserva);
        btn_init_reserva =  findViewById(R.id.btn_init_reserva);

        txt_nr_travel.setText(travel.getCodTravel());

        if (isClient){
            txt_client_info.setText(travel.getDriver());
        }else {
            if(Utils.isDriver(gloval.getGv_id_profile())) {
                txt_client_info.setText(travel.getClient());
            }else{
                txt_client_info.setText(travel.getDriver());
            }
        }

        txt_km_info.setText(travel.getDistanceLabel());
        button1 =  findViewById(R.id.button1);

        if(Utils.isCliente(gloval.getGv_id_profile())){
            button1.setText(getString(R.string.cancelar));
        }
        else{
            button1.setText(getString(R.string.reject));
        }

        txt_lote.setText(travel.getLot());
        txt_flete.setText(String.valueOf(travel.getIsFleetTravelAssistance()));

        txt_dpto_dialog.setText(travel.getDepartment());
        txt_piso_dialog.setText(travel.getFLOOR());

        if(HomeChofer.PARAM_26_VER_PRECIO_EN_VIAJE_EN_APP == 1) {
            txt_amount_info.setText(currency.getSymbol().concat(" ").concat(travel.getAmountCalculate()));
        }else {
            txt_amount_info.setText(currency.getSymbol().concat(" 0"));
        }

        if(gloval.getGv_id_profile() != Globales.ProfileId.DRIVER) {
            txt_amount_info.setText(currency.getSymbol().concat(" ").concat(travel.getAmountCalculate()));
        }

        if(travel.getIdSatatusTravel() != Globales.StatusTravel.VIAJE_EN_CURSO && travel.getIdSatatusTravel() != Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER) {

            if (travel.getIsAceptReservationByDriver() == 1) {
                if (HomeChofer.currentTravel == null) {
                    if(gloval.getGv_id_profile() == Globales.ProfileId.DRIVER) {

                        btn_init_reserva.setVisibility(View.VISIBLE);
                    }
                    else{
                        btn_init_reserva.setVisibility(View.GONE);
                    }
                } else {

                    btn_init_reserva.setEnabled(false);
                }
                bt_confirmar_reserva.setVisibility(View.GONE);



            } else {
                btn_init_reserva.setVisibility(View.GONE);
                bt_confirmar_reserva.setVisibility(View.GONE);
                if(gloval.getGv_id_profile() == Globales.ProfileId.DRIVER) {
                    bt_confirmar_reserva.setVisibility(View.VISIBLE);
                }
            }
        }else
        {
            btn_init_reserva.setEnabled(false);
            bt_confirmar_reserva.setEnabled(false);
            button1.setEnabled(false);

        }

        txt_date_info.setText(travel.getMdate().toString());

        if(travel.getNameOrigin() != null) {
            txt_origin_info.setText(travel.getNameOrigin().toString());
        }

        if(travel.getNameDestination() != null) {
            txt_destination_info.setText(travel.getNameDestination().toString());
        }

        txt_observationFromDriver.setText(travel.getObservationFromDriver().toString());
        txt_pasajeros_info.setText(travel.getPasajero());

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Utils.isDriver(gloval.getGv_id_profile()))
                {
                    event_cancel_by_driver(travel.getIdTravel());
                }
                else {
                    event_cancel_by_client(gloval.getGv_user_id(), travel.getIdTravel());
                }
            }
        });

        bt_confirmar_reserva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                event_confirm(travel.getIdTravel());
            }
        });

        btn_init_reserva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                aceptTravel(travel.getIdTravel());
            }
        });


        if(isClient)
        {
            if(!TextUtils.isEmpty(travel.getPhoneNumberDriver()) && travel.isAceptReservationByDriver==1) {
                txt_calling_info.setText(travel.getPhoneNumberDriver());
            }
        }
        else{
            if(!TextUtils.isEmpty(travel.getPhoneNumber())) {
                txt_calling_info.setText(travel.getPhoneNumber());
            }
        }

        //if(gloval.getGv_id_profile() == 3){

        //}
    }

    private void loadingDismiss()
    {
        try{
            if(loading!=null && loading.isShowing()){
                loading.dismiss();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /* METODO PARA ACEPATAR EL VIAJE*/
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            public  void  aceptTravel(int idTravel)
    {

        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }

        loading = ProgressDialog.show(this, getString(R.string.enviado), getString(R.string.espere_unos_segundos), true, false);

        daoTravel.accept(idTravel).enqueue(new Callback<InfoTravelEntity>() {
            @Override
            public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                loadingDismiss();

                try {
                    showMessage(getString(R.string.viaje_aceptado),1);
                    gloval.setGv_travel_current(response.body());

                    Handler handler= new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(InfoDetailTravelActivity.this,HomeChofer.class);
                            startActivity(i);
                            finish();
                        }
                    },4000);


                }catch (Exception e){
                    loadingDismiss();
                    Log.e("CONFIRM_VIA_ERROR",e.toString());
                    showMessage(getString(R.string.fallo_general),3);
                }

            }

            @Override
            public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                loadingDismiss();
                Log.e("CONFIR_ON_FAILURE",t.toString());
                showMessage(getString(R.string.fallo_general),3);
            }


        });

    }

    public  void event_confirm(int idTravel) {

        button1.setEnabled(false);
        bt_confirmar_reserva.setEnabled(false);

        final GlovalVar gloval = ((GlovalVar) getApplicationContext());

        loading = ProgressDialog.show(this, getString(R.string.enviado), getString(R.string.espere_unos_segundos), true, false);

        apiService.readReservation(idTravel,gloval.getGv_id_driver()).enqueue(new Callback<List<InfoTravelEntity>>() {
            @Override
            public void onResponse(Call<List<InfoTravelEntity>> call, Response<List<InfoTravelEntity>> response) {
                loadingDismiss();
                try {
                    showMessage(getString(R.string.reserva_acaptada),1);
                    comeBack();
                }catch (Exception e){
                    Log.e("CONFIRMAR RESERVA ERROR",e.toString());
                    showMessage(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<List<InfoTravelEntity>> call, Throwable t) {
                loadingDismiss();
                Log.e("CONFIRM_RES_ON_FAILURE",t.toString());
                showMessage(getString(R.string.fallo_general),3);

            }
        });
    }

    public void event_cancel_by_client(int idClient, int idTravel){
        button1.setEnabled(false);
        bt_confirmar_reserva.setEnabled(false);
        final GlovalVar gloval = ((GlovalVar)getApplicationContext());
        loading = ProgressDialog.show(this, getString(R.string.enviado), getString(R.string.espere_unos_segundos), true, false);
        int motivo = 0;

        apiService.cancelByClient(idClient, motivo, idTravel).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                loadingDismiss();
                try {
                    showMessage(getString(R.string.reserva_cancelada),Globales.StatusToast.SUCCESS);
                    comeBack();
                }catch (Exception e){
                    Log.e("CANCELAR RESERVA ERROR",e.toString());
                    showMessage(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                loadingDismiss();
                Log.e("CANCEL_RES_ON_FAILURE",t.toString());
                showMessage(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
            }
        });
    }

    public  void event_cancel_by_driver(int idTravel) {
        button1.setEnabled(false);
        bt_confirmar_reserva.setEnabled(false);

        final GlovalVar gloval = ((GlovalVar)getApplicationContext());

        loading = ProgressDialog.show(this, getString(R.string.enviado), getString(R.string.enviado), true, false);


        apiService.cacelReservation(idTravel,gloval.getGv_id_driver()).enqueue(new Callback<List<InfoTravelEntity>>() {
            @Override
            public void onResponse(Call<List<InfoTravelEntity>> call, Response<List<InfoTravelEntity>> response) {
                loadingDismiss();
                try {
                    showMessage(getString(R.string.reserva_cancelada),1);
                    comeBack();
                }catch (Exception e){
                    Log.e("CANCELAR RESERVA ERROR",e.toString());
                    showMessage(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<List<InfoTravelEntity>> call, Throwable t) {
                loadingDismiss();
                Log.e("CANCEL_RES_ON_FAILURE",t.toString());
                showMessage(getString(R.string.fallo_general),3);
            }
        });

    }

    private void showMessage(String mensaje,int status){
        SnackCustomService.show(this,mensaje,status,2000);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void comeBack(){
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //finish();

                 Intent returnIntent = new Intent();
                    returnIntent.putExtra("result","OK");
                    setResult(ReservationsFrangment.RESULT_OK,returnIntent);
                    finish();
            }
        },3000);
    }

}
